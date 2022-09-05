package com.sitepark.ies.contentrepository.core.domain.entity.filter;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Root implements Filter {

	@SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
	private final Long root;

	protected Root(@JsonProperty("root") Long root) {
		assert root != null;
		this.root = root;
	}

	public Long getRoot() {
		return this.root;
	}

}