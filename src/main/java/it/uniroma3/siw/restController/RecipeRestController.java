package it.uniroma3.siw.restController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.service.RecipeService;

@RestController
public class RecipeRestController {

	@Autowired
	private RecipeService recipeService;

	@GetMapping(value = "/rest/recipe/{id}")
	public Recipe getRecipe(@PathVariable("id") Long id) {
		return this.recipeService.getRecipeById(id);
	}

	@GetMapping(value = "/rest/recipe")
	public List<Recipe> getAllRecipes() {
		return this.recipeService.getAllRecipe();
	}

	@GetMapping(value = "/rest/searchRecipes")
	public Set<Recipe> searchRecipes(@RequestParam(required = false) String title,
			@RequestParam(required = false, defaultValue = "0") int difficulty,
			@RequestParam(required = false) String category,
			@RequestParam(required = false, defaultValue = "0") int prepTime) {

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

		return result;
	}
}
