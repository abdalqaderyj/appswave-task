package com.appswave.appswaveapi.service;

import com.appswave.appswaveapi.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
	User create(User user);
	User update(User updatedUser);
	List<User> getAll();
	User getById(Long id);
	Optional<User> getUserByEmail(String email);
	Optional<User> getByRefreshToken(String refreshToken);
	Boolean deleteById(Long id);
	boolean verifyPassword(String password, User user);
}
