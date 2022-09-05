package com.sitepark.ies.contentrepository.core.domain.entity.sort;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = OrderCriteria.Builder.class)
public abstract class OrderCriteria {
	private final String name;

	private final Direction direction;

	protected OrderCriteria(Builder<?> builder) {
		this.name = builder.name;
		this.direction = builder.direction;
	}

	public String getName() {
		return this.name;
	}

	public Direction getDirection() {
		return this.direction;
	}

	public abstract Builder<?> toBuilder();

	@JsonPOJOBuilder(withPrefix = "", buildMethodName = "build")
	public static abstract class Builder<B extends Builder<B>> {

		private String name;

		private Direction direction = Direction.DESC;

		protected Builder() { }

		protected Builder(OrderCriteria sortCriteria) {
			this.name = sortCriteria.name;
			this.direction = sortCriteria.direction;
		}

		public B name(String name) {
			assert name != null;
			this.name = name;
			return this.self();
		}

		public B direction(Direction direction) {
			assert direction != null;
			this.direction = direction;
			return this.self();
		}

		public abstract B self();

		public abstract OrderCriteria build();
	}
}
