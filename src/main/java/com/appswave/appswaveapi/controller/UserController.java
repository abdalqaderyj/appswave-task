package com.appswave.appswaveapi.controller;

import com.appswave.appswaveapi.Util.TokenUtil;
import com.appswave.appswaveapi.model.AppsWaveCustomException;

import com.appswave.appswaveapi.model.User;

import com.appswave.appswaveapi.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

@RestController
@RequestMapping("/v1/appswave-api/user")
public class UserController {
	private final TokenUtil jwtUtil;
	private final UserService userService;

	@Autowired
	public UserController(
			UserService userService,
			TokenUtil jwtUtil
	) {
		this.userService = userService;
		this.jwtUtil = jwtUtil;
	}

	@PutMapping
	public ResponseEntity<?> updateUser(
			@RequestBody User user,
			HttpServletRequest request
	) throws Exception {
		String header = request.getHeader("Authorization");
		String token = header.substring(7);
		String username = jwtUtil.extractUsername(token);

		if (!username.equals(user.getEmail())) {
			throw new AppsWaveCustomException("unauthorized to update user", HttpServletResponse.SC_FORBIDDEN);
		}

		User updatedUser = userService.update(user);
		return ResponseEntity.status(HttpServletResponse.SC_ACCEPTED).body(updatedUser);
	}

	@GetMapping("/all")
	public ResponseEntity<List<User>> getUserAll() throws Exception {
		return ResponseEntity.ok(userService.getAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(
			@PathVariable Long id
	) throws Exception {
		return ResponseEntity.ok(userService.getById(id));
	}

	@DeleteMapping
	public ResponseEntity<?> deleteUser(
			@PathVariable Long id,
			HttpServletRequest request
	) throws Exception {
		boolean success = userService.deleteById(id);
		if (!success) {
			throw new AppsWaveCustomException("failed to delete user", HttpServletResponse.SC_BAD_REQUEST);
		}
		return ResponseEntity.status(HttpServletResponse.SC_ACCEPTED).body("user deleted successfully");
	}
}
