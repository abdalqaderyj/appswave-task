package com.appswave.appswaveapi.controller;

import com.appswave.appswaveapi.Util.TokenUtil;

import com.appswave.appswaveapi.model.AppsWaveCustomException;
import com.appswave.appswaveapi.model.SignUpDTO;
import com.appswave.appswaveapi.model.User;

import com.appswave.appswaveapi.service.UserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/v1/appswave-api/auth")
public class AuthController {
	private final UserService userService;
	private final TokenUtil jwtUtil;

	@Autowired
	public AuthController(
			UserService userService,
			TokenUtil jwtUtil
	) {
		this.userService = userService;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("/signup")
	public ResponseEntity<?> signup(
			@RequestBody @Valid SignUpDTO userDTO
	) throws Exception {
		// default category
		if (StringUtils.isBlank(userDTO.getRole())) {
			userDTO.setRole("Normal");
		}

		User savedUser = userService.create(userDTO.toUser());
		return ResponseEntity.status(HttpServletResponse.SC_CREATED).body(savedUser);
	}

	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(
			@RequestBody Map<String, String> request
	) throws Exception {
		Optional<User> userOptional = userService.getUserByEmail(request.get("email"));
		if (userOptional.isEmpty() || !userService.verifyPassword(request.get("password"), userOptional.get())) {
			throw new AppsWaveCustomException("Invalid credentials", HttpServletResponse.SC_UNAUTHORIZED);
		}

		User user = userOptional.get();

		String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
		String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

		// Save the refresh token in the database
		user.setRefreshToken(refreshToken);
		userService.update(user);
		return ResponseEntity.ok(Map.of(
				"token", token,
				"refreshToken", refreshToken
		));
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<?> refreshToken(
			@RequestBody Map<String, String> request
	) throws Exception {
		String refreshToken = request.get("refreshToken");
		Optional<User> userOptional = userService.getByRefreshToken(refreshToken);

		if (userOptional.isEmpty() || !jwtUtil.validateToken(refreshToken)) {
			throw new AppsWaveCustomException("Invalid refresh token", HttpServletResponse.SC_UNAUTHORIZED);
		}

		User user = userOptional.get();
		String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

		return ResponseEntity.ok(Map.of(
				"token", token
		));
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(
			@RequestBody Map<String, String> request
	) throws Exception {
		String refreshToken = request.get("refreshToken");

		// Find the user by the refresh token
		Optional<User> userOptional = userService.getByRefreshToken(refreshToken);

		if (userOptional.isEmpty()) {
			throw new AppsWaveCustomException("Invalid refresh token", HttpServletResponse.SC_UNAUTHORIZED);
		}

		User user = userOptional.get();

		// Invalidate the refresh token
		user.setRefreshToken(null);
		userService.update(user);

		return ResponseEntity.ok("Logged out successfully");
	}
}
