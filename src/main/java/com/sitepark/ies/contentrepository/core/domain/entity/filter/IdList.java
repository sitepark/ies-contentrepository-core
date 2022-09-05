package com.sitepark.ies.contentrepository.core.domain.entity.filter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class IdList implements Filter {

	@SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format

	private final List<Long> idList;

	protected IdList(@JsonProperty("idList") Long... idList) {
		assert idList != null;
		this.idList = List.of(idList);
	}

	@SuppressFBWarnings("EI_EXPOSE_REP")
	public List<Long> getIdList() {
		return this.idList;
	}
}
