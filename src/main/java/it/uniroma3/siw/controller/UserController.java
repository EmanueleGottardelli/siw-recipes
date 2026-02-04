package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;


	@GetMapping("/user")
	public String getAllUser(Model model) {
		model.addAttribute("users", userService.getAllUsers());
		return "users";
	}

	@GetMapping("/user/{id}")
	public String getUserById(@PathVariable("id") Long id, Model model) {
		model.addAttribute("user", userService.getUser(id));
		model.addAttribute("recipes", userService.getUser(id).getRecipes());
		model.addAttribute("reviews", userService.getUser(id).getReviews());
		return "user";
	}

	@GetMapping("/admin/manageUsers")
	public String operazioniUser(Model model) {
		model.addAttribute("users", userService.getAllUsers());
		return "admin/manageUsers";
	}

	@GetMapping("/user/{id}/edit")
	public String formEditUser(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails, Model model,
			RedirectAttributes redirectAttributes) {

		User user = userService.getUser(id);
		if (user == null) {
			return "redirect:/user";
		}

		if (!user.getCredentials().getUsername().equals(userDetails.getUsername())
				|| user.getState().equals(User.BANNED_STATE)) {
			return "redirect:/user/" + id;
		}

		model.addAttribute("user", user);
		return "formEditUser";
	}

	@PostMapping("/user/{id}/edit")
	public String updateUser(@PathVariable("id") Long id, @ModelAttribute("user") User updatedUser) {
		User user = userService.getUser(id);

		if (updatedUser.getEmail() != null && !updatedUser.getEmail().isBlank()) {
			user.setEmail(updatedUser.getEmail());
		}
		if (updatedUser.getName() != null && !updatedUser.getName().isBlank()) {
			user.setName(updatedUser.getName());
		}
		if (updatedUser.getSurname() != null && !updatedUser.getSurname().isBlank()) {
			user.setSurname(updatedUser.getSurname());
		}
		userService.updateUser(user);

		return "redirect:/user/" + id;
	}

	@PostMapping("/admin/user/{id}/updateState")
	public String updateStateUser(@PathVariable Long id) {

		User user = userService.getUser(id);
		if (user == null)
			return "redirect:/admin/manageUsers";

		String adminUsername = SecurityContextHolder.getContext().getAuthentication().getName();

		if (user.getCredentials().getUsername().equals(adminUsername)) {
			return "redirect:/admin/manageUsers";
		}

		if (User.ACTIVE_STATE.equalsIgnoreCase(user.getState())) {
			user.setState(User.BANNED_STATE);
		} else {
			user.setState(User.ACTIVE_STATE);
		}

		userService.updateUser(user);

		return "redirect:/admin/manageUsers";
	}

}
