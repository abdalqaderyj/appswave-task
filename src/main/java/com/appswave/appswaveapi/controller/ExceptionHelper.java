package com.appswave.appswaveapi.controller;

import com.appswave.appswaveapi.model.AppsWaveCustomException;

import org.springframework.boot.web.servlet.error.ErrorController;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHelper implements ErrorController {
	public static ResponseEntity<Map<String, Object>> returnErrorView(
			HttpServletRequest req,
			Map<String, Object> result,
			String publicErrorMessage,
			HttpStatus status
	) {
		if (result != null) {
			result.put("Error in " + req.getRequestURI(), publicErrorMessage);
		}
		return new ResponseEntity<>(result, status);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> ExceptionHandleError(
			HttpServletRequest req,
			HttpServletResponse response,
			Exception ex
	) {
		Map<String, Object> result = new HashMap<>();
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		return returnErrorView(req, result, ex.getMessage(),HttpStatus.valueOf(response.getStatus()));
	}

	@ExceptionHandler(AppsWaveCustomException.class)
	public ResponseEntity<Map<String, Object>> AppsWaveCustomExceptionHandleError(
			HttpServletRequest req,
			HttpServletResponse response,
			AppsWaveCustomException ex
	) {
		Map<String, Object> result = new HashMap<>();
		response.setStatus(ex.getStatusCode());
		return returnErrorView(req, result, ex.getMessage(),HttpStatus.valueOf(ex.getStatusCode()));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, Object>> IllegalArgumentExceptionHandleError(
			HttpServletRequest req,
			HttpServletResponse response,
			IllegalArgumentException ex
	) {
		Map<String, Object> result = new HashMap<>();
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return returnErrorView(req, result, ex.getMessage(),HttpStatus.valueOf(response.getStatus()));
	}
}
