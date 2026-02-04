package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.repository.IngredientRepository;

@Service
public class IngredientService {
	
	@Autowired
	private IngredientRepository ingredientRepository;
	
	public Ingredient getIngredientById(Long id) {
		return this.ingredientRepository.findById(id).get();
	}
	
	public List<Ingredient> getIngredientByName(String name){
		return this.ingredientRepository.findByName(name);
	}
	
	public List<Ingredient> getAllIngredient(){
		List<Ingredient> result = new ArrayList<>();
		
		for(Ingredient i : this.ingredientRepository.findAll()) {
			result.add(i);
		}
		
		return result;
	}
	
	public void saveIngredient(Ingredient ingredient) {
		 this.ingredientRepository.save(ingredient);
	}
	
	public void deleteIngredient(Ingredient ingredient) {
		this.ingredientRepository.delete(ingredient);
	}

}
