package com.sitepark.ies.contentrepository.core.domain.entity.filter;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class IdList implements Filter {

	@SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format

	private final List<String> idList;

	protected IdList(@JsonProperty("idList") String... idList) {
		Objects.requireNonNull(idList, "idList is null");
		this.idList = List.of(idList);
	}

	@SuppressFBWarnings("EI_EXPOSE_REP")
	public List<String> getIdList() {
		return this.idList;
	}
}
