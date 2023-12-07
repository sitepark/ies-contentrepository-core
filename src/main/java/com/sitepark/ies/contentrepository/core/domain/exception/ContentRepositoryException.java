package com.sitepark.ies.contentrepository.core.domain.exception;

public abstract class ContentRepositoryException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ContentRepositoryException() {
		super();
	}
	public ContentRepositoryException(String message) {
		super(message);
	}
	public ContentRepositoryException(String message, Throwable t) {
		super(message, t);
	}
}
