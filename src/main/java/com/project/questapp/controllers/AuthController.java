package com.project.questapp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.project.questapp.entities.RefreshToken;
import com.project.questapp.entities.User;
import com.project.questapp.requests.RefreshRequest;
import com.project.questapp.requests.UserRequest;
import com.project.questapp.responses.AuthResponse;
import com.project.questapp.security.JwtTokenProvider;
import com.project.questapp.services.RefreshTokenService;
import com.project.questapp.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private AuthenticationManager authenticationManager;
	private JwtTokenProvider jwtTokenProvider;
	private UserService userService;
	private PasswordEncoder passwordEncoder;
	private RefreshTokenService refreshTokenService;
	
	

	public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
			UserService userService, PasswordEncoder passwordEncoder, RefreshTokenService refreshTokenService) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.refreshTokenService = refreshTokenService;
	}

	@PostMapping("/login")
	public AuthResponse login(@RequestBody UserRequest loginRequest) {
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUserName(),loginRequest.getPassword());
		Authentication auth = authenticationManager.authenticate(authToken);
		SecurityContextHolder.getContext().setAuthentication(auth);
		String jwtToken = jwtTokenProvider.genereteJwtToken(auth);
		User user = userService.getOneUserByUserName(loginRequest.getUserName());
		AuthResponse authResponse=new AuthResponse();
		authResponse.setAccessToken("Bearer "+jwtToken);
		authResponse.setRefreshToken(refreshTokenService.createRefreshToken(user));
		authResponse.setUserId(user.getId());
		return authResponse;
	}
	
	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@RequestBody UserRequest registerRequest){
		AuthResponse authResponse = new AuthResponse();
		if(userService.getOneUserByUserName(registerRequest.getUserName()) != null) {
			authResponse.setMessage("Username already in use.");
			return new ResponseEntity<>(authResponse,HttpStatus.BAD_REQUEST);
		}
		
		User user = new User();
		user.setUserName(registerRequest.getUserName());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		userService.saveOneUser(user);		
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(registerRequest.getUserName(), registerRequest.getPassword());
		Authentication auth = authenticationManager.authenticate(authToken);
		SecurityContextHolder.getContext().setAuthentication(auth);
		String jwtToken = jwtTokenProvider.genereteJwtToken(auth);
		
		authResponse.setMessage("User succesfuly registered!");
		authResponse.setAccessToken("Bearer "+jwtToken);
		authResponse.setRefreshToken(refreshTokenService.createRefreshToken(user));
		authResponse.setUserId(user.getId());
		return new ResponseEntity<>(authResponse,HttpStatus.CREATED);
		
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest refreshRequest){
		AuthResponse authResponse = new AuthResponse();
		RefreshToken token = refreshTokenService.getByUser(refreshRequest.getUserId());
		if(token.getToken().equals(refreshRequest.getRefreshToken()) && 
			!refreshTokenService.isRefreshExpired(token)) {
			User user = token.getUser();
			String jwtToken = jwtTokenProvider.genereteJwtTokenByUserName(user.getId());
			authResponse.setMessage("Token successfully refreshed!");
			authResponse.setAccessToken("Bearer "+jwtToken);
			authResponse.setRefreshToken(refreshTokenService.createRefreshToken(user));
			authResponse.setUserId(user.getId());
			return new ResponseEntity<>(authResponse,HttpStatus.CREATED);
		}else {
			authResponse.setMessage("Refresh token is not valid.");
			return new ResponseEntity<>(authResponse,HttpStatus.UNAUTHORIZED);
		}
		
	}
}
