package com.appswave.appswaveapi.Util;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

import java.util.*;

@Component
public class TokenUtil {
	private final String SECRET_KEY = "appswave_token";
	private final long TOKEN_EXPIRATION = 1000 * 60; // 1 minute
	private final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7; // 7 days

	public String generateToken(String username, String role) {
		return Jwts.builder()
				.claim("role", role)
				.setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
				.compact();
	}

	public String generateRefreshToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION)) // 7 days
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
				.compact();
	}

		public Claims extractClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	public String extractUsername(String token) {
		return extractClaims(token).getSubject();
	}

	public String extractUserRole(String token) {
		return (String) extractClaims(token).get("role");
	}

	public boolean isTokenExpired(String token) {
		return extractClaims(token).getExpiration().before(new Date());
	}

	public boolean validateToken(String token) {
		return ObjectUtils.isNotEmpty(extractClaims(token)) && !isTokenExpired(token);
	}
}
