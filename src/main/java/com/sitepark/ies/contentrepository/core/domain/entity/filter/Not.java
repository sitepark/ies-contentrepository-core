package com.sitepark.ies.contentrepository.core.domain.entity.filter;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Not implements Filter {

	@SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
	private final Filter not;

	protected Not(@JsonProperty("not") Filter not) {
		assert not != null;
		this.not = not;
	}

	public Filter getNot() {
		return this.not;
	}
}
