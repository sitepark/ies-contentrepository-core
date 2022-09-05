package com.sitepark.ies.contentrepository.core.domain.entity.filter;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Id implements Filter {

	@SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
	private final Long id;

	protected Id(@JsonProperty("id") Long id) {
		assert id != null;
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}
}
