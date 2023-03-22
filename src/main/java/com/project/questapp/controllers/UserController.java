package com.project.questapp.controllers;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.project.questapp.repos.UserRepository;
import com.project.questapp.responses.UserResponse;
import com.project.questapp.services.UserService;
import com.project.questapp.entities.User;
import com.project.questapp.exceptions.UserNotFoundException;

@RestController
@RequestMapping("/users")
public class UserController {
	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService=userService;
	}
	
	@GetMapping
	public List<User> getAllUsers(){
		return userService.getAllUsers();
	}
	
	@PostMapping
	public User createUser(@RequestBody User newUser) {
		return userService.saveOneUser(newUser);
	}
	
	@GetMapping("/{userId}")
	public UserResponse getOneUser(@PathVariable Long userId) {
		User user = userService.getOneUserById(userId);
		if(user==null) {
			throw new UserNotFoundException();
		}
		return new UserResponse(user);
	}
	
	@PutMapping("/{userId}")
	public User updateOneUser(@PathVariable Long userId,@RequestBody User newUser) {
		return userService.updateOneUserById(userId,newUser);
	}
	
	@DeleteMapping("/{userId}")
	public void deleteOneUser(@PathVariable Long userId) {
		userService.deleteOneUserById(userId);
	}
	
	@GetMapping("/activity/{userId}")
	public List<Object> getUserActivity(@PathVariable Long userId){
		return userService.getUserActivity(userId);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	private void handleUserNotFound() {
		
	}
}
