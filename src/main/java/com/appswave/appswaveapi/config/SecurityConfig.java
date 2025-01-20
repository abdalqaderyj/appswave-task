package com.appswave.appswaveapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public GrantedAuthorityDefaults grantedAuthorityDefaults() {
		return new GrantedAuthorityDefaults(""); // Removes default "ROLE_" prefix
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
		http.csrf().disable()
				.authorizeHttpRequests(auth -> auth
						// Allow unauthenticated access to login, signup, refreshToken and logout
						.antMatchers("/v1/appswave-api/auth/**").permitAll()

						// Admin-only endpoints
						.antMatchers("/v1/appswave-api/news/**/approve").hasAuthority("Admin")
						.antMatchers(HttpMethod.DELETE,"/v1/appswave-api/user/**").hasAuthority("Admin")

						// Endpoints accessible to both Admin and Content Writers
						.antMatchers(HttpMethod.DELETE,"/v1/appswave-api/news/**").hasAnyAuthority("Admin", "Content-Writer")
						.antMatchers(HttpMethod.POST,"/v1/appswave-api/news/**").hasAnyAuthority("Admin", "Content-Writer")
						.antMatchers(HttpMethod.PUT,"/v1/appswave-api/news/**").hasAnyAuthority("Admin", "Content-Writer")

						// Endpoints accessible by any role
//						.antMatchers(HttpMethod.GET, "/v1/appswave-api/news/**").hasAnyAuthority("Normal", "Admin", "Content-Writer")
//						.antMatchers(HttpMethod.GET, "/v1/appswave-api/user/**").hasAnyAuthority("Normal", "Admin", "Content-Writer")
//						.antMatchers(HttpMethod.PUT,"/v1/appswave-api/user/**").hasAnyAuthority("Normal", "Admin", "Content-Writer")

						// All other endpoints require authentication
						.anyRequest().authenticated()
				)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}

