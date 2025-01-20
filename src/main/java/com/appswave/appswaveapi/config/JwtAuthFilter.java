package com.appswave.appswaveapi.config;

import com.appswave.appswaveapi.Util.TokenUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	private final TokenUtil jwtUtil;

	public JwtAuthFilter(TokenUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain chain
	) throws IOException, ServletException {
		try {

			String header = request.getHeader("Authorization");
			if (StringUtils.isBlank(header) || !header.startsWith("Bearer ")) {
				chain.doFilter(request, response);
				return;
			}

			String token = header.substring(7);
			String username = jwtUtil.extractUsername(token);
			String role = jwtUtil.extractUserRole(token);

			if (username != null) {
				if (jwtUtil.validateToken(token)) {
					UsernamePasswordAuthenticationToken authToken =
							new UsernamePasswordAuthenticationToken(username, null, List.of(new SimpleGrantedAuthority(role)));
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
			chain.doFilter(request, response);
		} catch (Exception e) {
			handleInvalidCorrelationId(response, e);
		}
	}

	private void handleInvalidCorrelationId(HttpServletResponse response, Exception e) throws IOException {
		Map<String, String> errorDetails = new HashMap<>();
		errorDetails.put("error", e.getMessage());

		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		ObjectMapper objectMapper = new ObjectMapper();
		response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
	}
}

