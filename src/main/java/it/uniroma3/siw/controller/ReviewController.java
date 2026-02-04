package it.uniroma3.siw.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.RecipeService;
import it.uniroma3.siw.service.ReviewService;
import it.uniroma3.siw.service.UserService;

@Controller
public class ReviewController {

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private RecipeService recipeService;

	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private UserService userService;

	@GetMapping("/review/{id}/edit")
	public String formEditReview(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails,
			Model model) {
		Review review = reviewService.getReviewById(id);

		// controllo solo l'autore puo modificare
		if (!review.getAuthor().getCredentials().getUsername().equals(userDetails.getUsername())) {
			return "redirect:/recipe/" + review.getRecipe().getId();
		}

		model.addAttribute("review", review);
		return "formEditReview";
	}

	@PostMapping("/review/{id}/update")
	public String updateReview(@PathVariable("id") Long id, @ModelAttribute("review") Review updatedReview) {

		Review review = reviewService.getReviewById(id);

		if (updatedReview.getEvaluation() != 0 && updatedReview.getEvaluation() != review.getEvaluation()) {
			review.setEvaluation(updatedReview.getEvaluation());
		}
		if (updatedReview.getText() != null && !updatedReview.getText().isBlank()) {
			review.setText(updatedReview.getText());
		}

		review.setEntryDate(LocalDate.now());
		reviewService.saveReview(review);

		// redirect alla pagina della recipe
		return "redirect:/recipe/" + review.getRecipe().getId();
	}

	@PostMapping("/review/{id}/delete")
	public String deleteReview(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
		// recupero il commento da cancellare
		Review review = reviewService.getReviewById(id);
		Long recipeId = review.getRecipe().getId();

		reviewService.deleteReview(review);

		Credentials credentials = credentialsService.getCredentialsByUsername(userDetails.getUsername());

		if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			// redirect alla pagina di update della recipe
			return "redirect:/admin/manageRecipes";
		}

		return "redirect:/recipe/" + recipeId;
	}

	@PostMapping("/recipe/{recipeId}/review")
	public String addReview(@PathVariable("recipeId") Long recipeId, @ModelAttribute("newReview") Review newReview,
			@AuthenticationPrincipal UserDetails userDetails) {
		User currentUser = userService.getCurrentUser();
		
		if(currentUser == null)
			return "redirect:/login";
		
		//recupera la recipe
		Recipe recipe = recipeService.getRecipeById(recipeId);
		
		//collego la review alla ricetta
		newReview.setRecipe(recipe);
		
		Credentials credentials = credentialsService.getCredentialsByUsername(userDetails.getUsername());
		User user = credentials.getUser();
		newReview.setAuthor(user);
		
		//salvo 
		newReview.setEntryDate(LocalDate.now());
		reviewService.saveReview(newReview);
		
		//redirect alla pagina della recipe
		return "redirect:/recipe/" + recipeId;
	}

}
