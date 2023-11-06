package com.sitepark.ies.contentrepository.core.domain.exception;

public class AccessDeniedException extends ContentRepositoryException {
	private static final long serialVersionUID = 1L;

	public AccessDeniedException(String message) {
		super(message);
	}

}