package com.sitepark.ies.contentrepository.core.domain.entity.filter;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class ParentList implements Filter {

	@SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
	private final List<Long> parentList;

	protected ParentList(@JsonProperty("parentList") Long... parentList) {
		Objects.requireNonNull(parentList, "parentList is null");
		this.parentList = List.of(parentList);
	}

	@SuppressFBWarnings("EI_EXPOSE_REP")
	public List<Long> getParentList() {
		return this.parentList;
	}
}
