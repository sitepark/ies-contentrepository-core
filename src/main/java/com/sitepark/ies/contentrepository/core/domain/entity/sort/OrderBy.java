package com.sitepark.ies.contentrepository.core.domain.entity.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@JsonDeserialize(builder = OrderBy.Builder.class)
public class OrderBy {

	private final List<OrderCriteria> sort;

	protected OrderBy(Builder builder) {
		this.sort = Collections.unmodifiableList(builder.sort);
	}

	@SuppressFBWarnings("EI_EXPOSE_REP")
	public List<OrderCriteria> getSort() {
		return this.sort;
	}

	public static Builder builder() {
		return new Builder();
	}

	public Builder toBuilder() {
		return new Builder(this);
	}

	@JsonPOJOBuilder(withPrefix = "", buildMethodName = "build")
	public static class Builder {

		private final List<OrderCriteria> sort = new ArrayList<>();

		protected Builder() { }

		protected Builder(OrderBy and) {
			this.sort.addAll(and.sort);
		}

		@SuppressWarnings("PMD.UseArraysAsList")
		public Builder sort(OrderCriteria... sortCriteriaList) {
			assert sortCriteriaList != null;
			for (OrderCriteria sortCriteria : sortCriteriaList) {
				assert sortCriteria != null : "sortCriteria is null";
				this.sort.add(sortCriteria);
			}
			return this;
		}

		public OrderBy build() {
			return new OrderBy(this);
		}
	}
}
