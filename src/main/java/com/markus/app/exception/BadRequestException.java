package com.markus.app.exception;

public class BadRequestException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2506354157288425388L;

	public BadRequestException(final String text) {
		super(text);
	}

	public BadRequestException(final String text, final Throwable cause) {
		super(text, cause);
	}
}
