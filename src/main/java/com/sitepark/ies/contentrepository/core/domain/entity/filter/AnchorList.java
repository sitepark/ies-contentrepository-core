package com.sitepark.ies.contentrepository.core.domain.entity.filter;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class AnchorList implements Filter {

	@SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format

	private final List<com.sitepark.ies.contentrepository.core.domain.entity.Anchor> anchorList;

	protected AnchorList(@JsonProperty("anchorList")
			com.sitepark.ies.contentrepository.core.domain.entity.Anchor... anchorList) {
		Objects.requireNonNull(anchorList, "anchorList is null");
		this.anchorList = List.of(anchorList);
	}

	@SuppressFBWarnings("EI_EXPOSE_REP")
	public List<com.sitepark.ies.contentrepository.core.domain.entity.Anchor> getAnchorList() {
		return this.anchorList;
	}
}
