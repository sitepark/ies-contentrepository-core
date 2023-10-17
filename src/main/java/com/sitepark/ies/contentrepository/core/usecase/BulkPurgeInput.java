package com.sitepark.ies.contentrepository.core.usecase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.sitepark.ies.contentrepository.core.domain.entity.filter.Filter;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public final class BulkPurgeInput {

	private final List<Long> rootList;

	private final Filter filterBy;

	private final boolean forceLock;

	private BulkPurgeInput(Builder builder) {
		this.rootList = Collections.unmodifiableList(builder.rootList);
		this.filterBy = builder.filterBy;
		this.forceLock = builder.forceLock;
	}

	@SuppressFBWarnings("EI_EXPOSE_REP")
	public List<Long> getRootList() {
		return this.rootList;
	}

	public Optional<Filter> getFilter() {
		return Optional.ofNullable(this.filterBy);
	}

	public boolean isForceLock() {
		return this.forceLock;
	}

	public static Builder builder() {
		return new Builder();
	}

	public Builder toBuilder() {
		return new Builder(this);
	}

	public static final class Builder {

		private final List<Long> rootList = new ArrayList<>();

		private Filter filterBy;

		private boolean forceLock;

		private Builder() { }

		private Builder(BulkPurgeInput bulkPurgeRequest) {
			this.rootList.addAll(bulkPurgeRequest.rootList);
			this.filterBy = bulkPurgeRequest.filterBy;
			this.forceLock = bulkPurgeRequest.forceLock;
		}

		public Builder rootList(List<Long> rootList) {
			Objects.requireNonNull(rootList, "rootList is null");
			for (Long root : rootList) {
				this.root(root);
			}
			return this;
		}

		public Builder root(Long root) {
			Objects.requireNonNull(root, "root is null");
			this.rootList.add(root);
			return this;
		}

		public Builder filterBy(Filter filterBy) {
			Objects.requireNonNull(filterBy, "filterBy is null");
			this.filterBy = filterBy;
			return this;
		}

		public Builder forceLock(boolean forceLock) {
			this.forceLock = forceLock;
			return this;
		}

		public BulkPurgeInput build() {
			assert (!this.rootList.isEmpty() || this.filterBy != null)
					: "Either rootList or filterBy must be specified";
			return new BulkPurgeInput(this);
		}
	}
}
