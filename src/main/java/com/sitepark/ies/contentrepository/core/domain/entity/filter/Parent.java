package com.sitepark.ies.contentrepository.core.domain.entity.filter;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Parent implements Filter {

	@SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
	private final Long parent;

	protected Parent(@JsonProperty("parent") Long parent) {
		assert parent != null;
		this.parent = parent;
	}

	public Long getParent() {
		return this.parent;
	}
}
