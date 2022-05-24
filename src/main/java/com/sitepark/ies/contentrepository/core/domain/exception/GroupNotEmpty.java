package com.sitepark.ies.contentrepository.core.domain.exception;

public class GroupNotEmpty extends ContentRepositoryException {
	private static final long serialVersionUID = 1L;

	private final long id;

	public GroupNotEmpty(long id) {
		super();
		this.id = id;
	}

	public long getId() {
		return this.id;
	}

	@Override
	public String getMessage() {
		return "Group with id " + this.id + " not empty";
	}

}