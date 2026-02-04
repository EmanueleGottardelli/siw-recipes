package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.model.User;

public interface RecipeRepository extends CrudRepository<Recipe,Long> {
	
	public List<Recipe> findByPrepTime(int prepTime);
	public List<Recipe> findByDifficulty(int difficulty);
	public List<Recipe> findByTitle(String title);
	public List<Recipe> findByAuthor(User author);
	public List<Recipe> findByCategory(String category);
	
	public boolean existsByTitleAndCategoryAndPrepTimeAndAuthor(String title, String category, int prepTime, User user);

}
