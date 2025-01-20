package com.appswave.appswaveapi.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class SignUpDTO {
	@NotBlank(message = "Email is mandatory")
	@Email(message = "Invalid email format")
	private String email;

	@NotBlank(message = "Password is mandatory")
	@Size(min = 8, message = "Password must be at least 8 characters long")
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).+$",
			message = "Password must contain at least one uppercase letter and one digit")
	private String password;

	@NotBlank(message = "FullName is mandatory")
	private String fullName;

	@NotBlank(message = "DateOfBirth is mandatory")
	@Past
	private Date dateOfBirth;
	private String role;

	public User toUser() {
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setFullName(fullName);
		user.setDateOfBirth(dateOfBirth);
		user.setRole(role);
		return user;
	}
}
