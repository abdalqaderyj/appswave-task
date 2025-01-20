package com.appswave.appswaveapi.model;

import lombok.Getter;

@Getter
public class AppsWaveCustomException extends Exception {
	private final int statusCode;

	public AppsWaveCustomException(String message) {
		super(message);
		statusCode = 418;
	}

	public AppsWaveCustomException(String message, int statusCode) {
		super(message);
		this.statusCode = statusCode;
	}
}
