package com.sitepark.ies.contentrepository.core.domain.entity.filter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class RootList implements Filter {

	@SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
	private final List<Long> rootList;

	protected RootList(@JsonProperty("rootList") Long... rootList) {
		assert rootList != null;
		this.rootList = List.of(rootList);
	}

	@SuppressFBWarnings("EI_EXPOSE_REP")
	public List<Long> getRootList() {
		return this.rootList;
	}
}
