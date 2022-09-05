package com.sitepark.ies.contentrepository.core.domain.entity.filter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class Or implements Filter {

	@SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
	private final List<Filter> or;

	protected Or(@JsonProperty("or") Filter... or) {
		assert or != null;
		this.or = List.of(or);
	}

	@SuppressFBWarnings("EI_EXPOSE_REP")
	public List<Filter> getOr() {
		return this.or;
	}
}