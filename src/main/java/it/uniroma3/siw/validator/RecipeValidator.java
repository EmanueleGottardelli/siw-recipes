package it.uniroma3.siw.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.service.RecipeService;


@Component
public class RecipeValidator implements Validator {
	
	@Autowired
	private RecipeService recipeService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Recipe.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Recipe recipe = (Recipe) target;
		
		if(recipeService.alreadyExistsByTitleCategoryPrepTimeAndAuthor(recipe)) {
			errors.reject("error.alreadyExistsByTitlrCategoryPrepTimeAndAuthor");
		}
		
	}

}
