package com.sitepark.ies.contentrepository.core.domain.entity.filter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class And implements Filter {

	@SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
	private final List<Filter> and;

	protected And(@JsonProperty("and") Filter... and) {
		assert and != null;
		this.and = List.of(and);
	}

	@SuppressFBWarnings("EI_EXPOSE_REP")
	public List<Filter> getAnd() {
		return this.and;
	}
}
