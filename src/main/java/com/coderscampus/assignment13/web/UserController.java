package com.coderscampus.assignment13.web;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/register")
	public String getCreateUser (ModelMap model) {
		model.put("user", new User());
		return "register";
	}
	
	@PostMapping("/register")
	public String postCreateUser (User user) {
		System.out.println(user);
		userService.saveUser(user);
		return "redirect:/register";
	}
	
	@GetMapping("/users")
	public String getAllUsers (ModelMap model) {
		Set<User> users = userService.findAll();
		model.put("users", users);
		if (users.size() == 1) {
			model.put("user", users.iterator().next());
		}
		return "users";
	}
	
	@GetMapping("/users/{userId}")
	public String getOneUser (ModelMap model, @PathVariable Long userId) {
		User user = userService.findById(userId);
		if (user.getAddress() == null){
			user.setAddress(new Address());
		}
		model.put("users", Collections.singletonList(user));
		model.put("user", user);
		model.put("address", user.getAddress());
		return "users";
	}
	
	@PostMapping("/users/{userId}")
	public String updateUser (@PathVariable Long userId, User user) {
		User existingUser = userService.findById(userId);
		if(existingUser != null) {
			existingUser.setUsername(user.getUsername());
			if (user.getPassword() != null && user.getPassword().isEmpty()) {
				existingUser.setPassword(user.getPassword());
			}
			existingUser.setName(user.getName());

			Address address = user.getAddress();
			if (address != null) {
				if (existingUser.getAddress() == null) {
					address.setUser(existingUser);
					existingUser.setAddress(address);
				} else {
					Address existingAddress = existingUser.getAddress();
					existingAddress.setAddressLine1(address.getAddressLine1());
					existingAddress.setAddressLine2(address.getAddressLine2());
					existingAddress.setCity(address.getCity());
					existingAddress.setRegion(address.getRegion());
					existingAddress.setCountry(address.getCountry());
					existingAddress.setZipCode(address.getZipCode());
				}
			}
			userService.saveUser(existingUser);
		}
		return "redirect:/users/" +userId;
	}
	
	@PostMapping("/users/{userId}/delete")
	public String deleteOneUser (@PathVariable Long userId) {
		userService.delete(userId);
		return "redirect:/users";
	}

	@PostMapping("/users/{userId}/accounts")
	public String createNewAccount(@PathVariable Long userId, Account account) {
		User user = userService.findById(userId);
		if (user != null){
			account.getUsers().add(user);
			user.getAccounts().add(account);
			userService.saveUser(user);
		}
		return "redirect:/users/" + userId;
	}
}
