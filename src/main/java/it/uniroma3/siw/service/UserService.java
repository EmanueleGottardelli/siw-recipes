package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Transactional
	public User getCurrentUser() {		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		
		if((authentication instanceof AnonymousAuthenticationToken)) {
			return null;
		}
		
		User user = credentialsService.getCredentialsByUsername(authentication.getName()).getUser();
		
		if(user.getState().equals(User.BANNED_STATE)) {
			return null;
		}
		
		return user;
	}
	
	@Transactional
	public List<User> getUserByState(String state){
		return this.userRepository.findByState(state);
	}
	
	@Transactional
	public User getUser(Long id) {
		Optional<User> result = this.userRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public User updateUser(User user) {
		return this.userRepository.save(user);
	}
	
	@Transactional
	public User saveUser(User user) {
		user.setState(User.ACTIVE_STATE);
		return this.userRepository.save(user);
	}
	
	@Transactional
	public List<User> getAllUsers(){
		List<User> result = new ArrayList<>();
		
		for(User u : this.userRepository.findAll()) {
			result.add(u);
		}
		
		return result;
	}
	
	public boolean alreadyExistsByNameSurnameAndEmail(User user) {
		return this.userRepository.existsByNameAndSurnameAndEmail(user.getName(), user.getSurname(), user.getEmail());
	}
}
