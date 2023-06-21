package com.sitepark.ies.contentrepository.core.domain.entity.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class SubTreeQuery extends Query {

	private final List<Long> rootList;

	protected SubTreeQuery(Builder builder) {
		super(builder);
		this.rootList = Collections.unmodifiableList(builder.rootList);
	}

	@SuppressFBWarnings("EI_EXPOSE_REP")
	public List<Long> getRootList() {
		return this.rootList;
	}

	public static Builder builder() {
		return new Builder();
	}

	@Override public Builder toBuilder() {
		return new Builder(this);
	}

	@JsonPOJOBuilder(withPrefix = "", buildMethodName = "build")
	public static class Builder extends Query.Builder<Builder> {

		private final List<Long> rootList = new ArrayList<>();

		protected Builder() { }

		protected Builder(SubTreeQuery curserBasedQuery) {
			super(curserBasedQuery);
			this.rootList.addAll(curserBasedQuery.rootList);
		}

		public Builder root(Long root) {
			assert root != null;
			this.rootList.add(root);
			return this.self();
		}

		public Builder rootList(List<Long> rootList) {
			assert rootList != null;
			for (Long root : rootList) {
				this.root(root);
			}
			return this.self();
		}

		@Override
		public SubTreeQuery build() {
			assert !this.rootList.isEmpty() : "rootList is empty";
			return new SubTreeQuery(this);
		}

		@Override
		public Builder self() {
			return this;
		}
	}
}
