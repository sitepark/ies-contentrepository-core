package com.sitepark.ies.contentrepository.core.domain.exception;

public class AccessDenied extends ContentRepositoryException {
	private static final long serialVersionUID = 1L;

	public AccessDenied(String message) {
		super(message);
	}
}