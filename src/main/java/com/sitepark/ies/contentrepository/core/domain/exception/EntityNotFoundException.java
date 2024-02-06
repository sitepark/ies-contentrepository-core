package com.sitepark.ies.contentrepository.core.domain.exception;

public class EntityNotFoundException extends ContentRepositoryException {

	private static final long serialVersionUID = 1L;

	private final String id;

	public EntityNotFoundException(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	@Override
	public String getMessage() {
		return "Entity with id " + this.id + " not found";
	}
}
