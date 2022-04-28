package com.sitepark.ies.contentrepository.core.domain.exception;

import com.sitepark.ies.contentrepository.core.domain.entity.Identifier;

public class EntityNotFound extends ContentRepositoryException {

	private static final long serialVersionUID = 1L;

	private final Identifier identifier;

	public EntityNotFound(Identifier identifier) {
		super();
		this.identifier = identifier;
	}

	public Identifier getIdentifier() {
		return this.identifier;
	}

	@Override
	public String getMessage() {
		return "Entity with identifier " + this.identifier + " not found";
	}
}
