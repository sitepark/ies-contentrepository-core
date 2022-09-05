package com.sitepark.ies.contentrepository.core.domain.entity.filter;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Anchor implements Filter {

	@SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
	private final com.sitepark.ies.contentrepository.core.domain.entity.Anchor anchor;

	protected Anchor(@JsonProperty("anchor") com.sitepark.ies.contentrepository.core.domain.entity.Anchor anchor) {
		assert anchor != null;
		this.anchor = anchor;
	}

	public com.sitepark.ies.contentrepository.core.domain.entity.Anchor getAnchor() {
		return this.anchor;
	}
}
