package com.sitepark.ies.contentrepository.core.domain.exception;

public class EntityNotFoundException extends ContentRepositoryException {

	private static final long serialVersionUID = 1L;

	private final long id;

	public EntityNotFoundException(long id) {
		super();
		this.id = id;
	}

	public long getId() {
		return this.id;
	}

	@Override
	public String getMessage() {
		return "Entity with id " + this.id + " not found";
	}
}
