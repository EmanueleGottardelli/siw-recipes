package it.uniroma3.siw.controller;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.IngredientService;
import it.uniroma3.siw.service.RecipeService;
import it.uniroma3.siw.service.UserService;
import it.uniroma3.siw.validator.RecipeValidator;
import jakarta.validation.Valid;

@Controller
public class RecipeController {

	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	private RecipeService recipeService;

	@Autowired
	private RecipeValidator recipeValidator;

	@Autowired
	private UserService userService;

	@Autowired
	private IngredientService ingredientService;

	@GetMapping("/searchRecipes")
	public String searchRecipes(@RequestParam(required = false) String title,
			@RequestParam(required = false, defaultValue = "0") int difficulty,
			@RequestParam(required = false) String category,
			@RequestParam(required = false, defaultValue = "0") int prepTime, Model model) {

		Set<Recipe> result = new HashSet<>();

		if (title != null && !title.isBlank()) {
			result.addAll(recipeService.getRecipeByTitle(title));
		}
		if (difficulty != 0) {
			result.addAll(recipeService.getRecipeByDifficulty(difficulty));
		}
		if (category != null && !category.isBlank()) {
			result.addAll(recipeService.getRecipeByCategory(category));
		}
		if (prepTime != 0) {
			result.addAll(recipeService.getRecipeByPrepTime(prepTime));
		}

		model.addAttribute("result", result);

		return "searchRecipesResult";
	}

	@GetMapping("/recipe/{id}/edit")
	public String formEditRecipe(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails, Model model) {
		User currentUser = userService.getCurrentUser();
		Recipe recipe = recipeService.getRecipeById(id);
		if (recipe == null) {
			return "redirect:/recipe";
		}

		if (currentUser == null
				|| !recipe.getAuthor().getCredentials().getUsername().equals(userDetails.getUsername())) {
			return "redirect:/recipe/" + id;
		}

		model.addAttribute("recipe", recipe);
		return "formEditRecipe";
	}

	@PostMapping("/recipe/{id}/edit")
	public String updateRecipe(@PathVariable("id") Long id, @ModelAttribute("recipe") Recipe updatedRecipe,
			@AuthenticationPrincipal UserDetails userDetails) {

		User currentUser = userService.getCurrentUser();
		Recipe recipe = recipeService.getRecipeById(id);
		if (recipe == null) {
			return "redirect:/recipe";
		}

		if (currentUser == null
				|| !recipe.getAuthor().getCredentials().getUsername().equals(userDetails.getUsername())) {
			return "redirect:/recipe/" + id;
		}

		if (updatedRecipe.getCategory() != null) {
			recipe.setCategory(updatedRecipe.getCategory());
		}
		if (updatedRecipe.getDescription() != null && !updatedRecipe.getDescription().isBlank()) {
			recipe.setDescription(updatedRecipe.getDescription());
		}
		if (updatedRecipe.getProcedimento() != null && !updatedRecipe.getProcedimento().isBlank()) {
			recipe.setProcedimento(updatedRecipe.getProcedimento());
		}
		if (updatedRecipe.getDifficulty() != 0 && updatedRecipe.getDifficulty() != recipe.getDifficulty()) {
			recipe.setDifficulty(updatedRecipe.getDifficulty());
		}
		if (updatedRecipe.getTitle() != null && !updatedRecipe.getTitle().isBlank()) {
			recipe.setTitle(updatedRecipe.getTitle());
		}

		recipe.setEntryDate(LocalDate.now());
		recipeService.saveRecipe(recipe);
		return "redirect:/recipe/" + id;
	}

	@PostMapping("/deleteRecipe/{id}")
	public String deleteRecipe(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {

		Recipe recipe = recipeService.getRecipeById(id);
		if (recipe == null) {
			return "redirect:/recipe";
		}

		Credentials credentials = credentialsService.getCredentialsByUsername(userDetails.getUsername());

		boolean isAuthor = recipe.getAuthor().getCredentials().getUsername().equals(userDetails.getUsername());

		boolean isAdmin = credentials.getRole().equals(Credentials.ADMIN_ROLE);

		if (!isAuthor && !isAdmin) {
			return "redirect:/recipe/" + id;
		}

		recipeService.deleteRecipe(recipe);

		if (isAdmin) {
			return "redirect:/admin/manageRecipes";
		} else {
			return "redirect:/recipe";
		}
	}

	@GetMapping("/admin/manageRecipes")
	public String manageRecipes(Model model) {
		model.addAttribute("recipes", recipeService.getAllRecipe());
		return "admin/manageRecipes";
	}

	@GetMapping("/recipe")
	public String getAllRecipe(Model model) {
		model.addAttribute("recipes", recipeService.getAllRecipe());
		return "recipes";
	}

	@GetMapping("/recipe/{id}")
	public String getRecipe(@PathVariable Long id, Model model) {
		model.addAttribute("recipe", recipeService.getRecipeById(id));
		model.addAttribute("newReview", new Review());
		return "recipe";
	}

	@GetMapping("/formNewRecipe")
	public String formNewRecipe(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		if (userDetails == null) {
			return "redirect:/login";
		}

		model.addAttribute("recipe", new Recipe());
		return "formNewRecipe";
	}

	@PostMapping("/recipe/{recipeId}/ingredients/{ingId}/delete")
	public String deleteIngredient(@PathVariable("recipeId") Long recipeId, @PathVariable("ingId") Long ingId) {
		Ingredient ingredient = ingredientService.getIngredientById(ingId);

		ingredientService.deleteIngredient(ingredient);

		return "redirect:/recipe/" + recipeId;
	}

	@PostMapping("/recipe/{recipeId}/ingredients/{ingId}/edit")
	public String editIngredient(@PathVariable("recipeId") Long recipeId,
			@ModelAttribute("ingredient") Ingredient upgradedIngredient, @PathVariable("ingId") Long ingId,
			@RequestParam String name, @RequestParam double quantity, @RequestParam String unitMeasurement) {

		Ingredient ingredient = ingredientService.getIngredientById(ingId);

		if (upgradedIngredient.getName() != null && !upgradedIngredient.getName().isBlank()) {
			ingredient.setName(upgradedIngredient.getName());
		}
		if (upgradedIngredient.getQuantity() != 0 && upgradedIngredient.getQuantity() != ingredient.getQuantity()) {
			ingredient.setQuantity(upgradedIngredient.getQuantity());
		}
		if (upgradedIngredient.getUnitMeasurement() != null && !upgradedIngredient.getUnitMeasurement().isBlank()) {
			ingredient.setUnitMeasurement(upgradedIngredient.getUnitMeasurement());
		}
		ingredientService.saveIngredient(ingredient);

		return "redirect:/recipe/" + recipeId;
	}

	@PostMapping("/recipe/{id}/ingredients/add")
	public String addIngredient(@PathVariable("id") Long recipeId, @RequestParam(required = false) String name,
			@RequestParam(required = false, defaultValue = "0") double quantity,
			@RequestParam(required = false) String unitMeasurement) {

		Recipe recipe = recipeService.getRecipeById(recipeId);

		Ingredient ingredient = new Ingredient();
		ingredient.setName(name);
		ingredient.setQuantity(quantity);
		ingredient.setUnitMeasurement(unitMeasurement);
		ingredient.setRecipe(recipe);
		
		recipe.getIngredients().add(ingredient);

		ingredientService.saveIngredient(ingredient);
		recipeService.saveRecipe(recipe);
		
		return "redirect:/recipe/" + recipeId;
	}

	@PostMapping("/addRecipe")
	public String newRecipe(@Valid @ModelAttribute("recipe") Recipe recipe, BindingResult bindingResult, Model model) {
		User currentUser = userService.getCurrentUser();
		recipeValidator.validate(recipe, bindingResult);

		if (!bindingResult.hasErrors()) {
			recipe.setAuthor(currentUser);
			recipe.setEntryDate(LocalDate.now());
			recipeService.saveRecipe(recipe);
			return "redirect:/recipe";
		}

		model.addAttribute("messaggioErrore", "Questa ricetta è già presente!");
		return "formNewRecipe";
	}
}
