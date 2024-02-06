package com.sitepark.ies.contentrepository.core.domain.entity.sort;

public class DummyOrderCriteria extends OrderCriteria {

	protected DummyOrderCriteria(Builder builder) {
		super(builder);
	}

	public static Builder builder() {
		return new Builder();
	}

	@Override
	public Builder toBuilder() {
		return new Builder(this);
	}

	public static class Builder extends OrderCriteria.Builder<Builder> {

		protected Builder() {
		}

		protected Builder(DummyOrderCriteria sortCriteria) {
			super(sortCriteria);
		}

		@Override
		public Builder self() {
			return this;
		}

		@Override
		public DummyOrderCriteria build() {
			return new DummyOrderCriteria(this);
		}
	}
}
