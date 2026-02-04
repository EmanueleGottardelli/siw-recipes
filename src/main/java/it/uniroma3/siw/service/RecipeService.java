package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.repository.RecipeRepository;

@Service
public class RecipeService {
	
	@Autowired
	private RecipeRepository recipeRepository;
	
	public Recipe getRecipeById(Long id) {
		return this.recipeRepository.findById(id).get();
	}
	
	public List<Recipe> getAllRecipe(){
		List<Recipe> result = new ArrayList<>();
		
		for(Recipe r : this.recipeRepository.findAll()) {
			result.add(r);
		}
		
		return result;
	}
	
	public List<Recipe> getRecipeByTitle(String title){
		return this.recipeRepository.findByTitle(title);
	}
	
	public List<Recipe> getRecipeByCategory(String category){
		return this.recipeRepository.findByCategory(category);
	}
	
	public List<Recipe> getRecipeByPrepTime(int prepTime){
		return this.recipeRepository.findByPrepTime(prepTime);
	}
	
	public List<Recipe> getRecipeByDifficulty(int difficulty){
		return this.recipeRepository.findByDifficulty(difficulty);
	}
	
	public Recipe saveRecipe(Recipe recipe) {
		return this.recipeRepository.save(recipe);
	}
	
	public void deleteRecipe(Recipe recipe) {
		this.recipeRepository.delete(recipe);
	}
	
	public boolean alreadyExistsByTitleCategoryPrepTimeAndAuthor(Recipe recipe) {
		return this.recipeRepository.existsByTitleAndCategoryAndPrepTimeAndAuthor(recipe.getTitle(), recipe.getCategory(), recipe.getPrepTime(), recipe.getAuthor());
	}
}
