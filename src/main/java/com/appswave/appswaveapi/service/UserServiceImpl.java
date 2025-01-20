package com.appswave.appswaveapi.service;

import com.appswave.appswaveapi.dao.UserRepository;
import com.appswave.appswaveapi.model.News;
import com.appswave.appswaveapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserServiceImpl (
			UserRepository userRepository,
			PasswordEncoder passwordEncoder
	) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public User create(User user) {
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			throw new IllegalArgumentException("User already exists");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public User update(User updatedUser) {
		if (userRepository.findById(updatedUser.getId()).isPresent()) {
			return userRepository.save(updatedUser);
		} else {
			throw new NoSuchElementException("News not found");
		}
	}

	@Override
	public List<User> getAll() {
		return userRepository.findAll();
	}

	@Override
	public User getById(Long id) {
		return userRepository.findById(id).orElse(new User());
	}

	@Override
	public Optional<User> getByRefreshToken(String refreshToken) {
		return userRepository.findByRefreshToken(refreshToken);
	}

	@Override
	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public Boolean deleteById(Long id) {
		userRepository.deleteById(id);
		return userRepository.findById(id).isEmpty();
	}

	@Override
	public boolean verifyPassword(String password, User user) {
		return passwordEncoder.matches(password, user.getPassword());
	}
}
