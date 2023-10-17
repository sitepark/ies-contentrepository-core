package com.sitepark.ies.contentrepository.core.domain.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class EntityBulkOperation {

	private final BulkOperationKey key;

	private final List<Entity> entityList;

	private final Consumer<Entity> consumer;

	protected EntityBulkOperation(Builder builder) {
		this.key = builder.key;
		this.entityList = Collections.unmodifiableList(builder.entityList);
		this.consumer = builder.consumer;
	}

	public BulkOperationKey getKey() {
		return this.key;
	}

	@SuppressFBWarnings("EI_EXPOSE_REP")
	public List<Entity> getEntityList() {
		return this.entityList;
	}

	public Consumer<Entity> getConsumer() {
		return this.consumer;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private BulkOperationKey key;

		private final List<Entity> entityList = new ArrayList<>();

		private Consumer<Entity> consumer;

		protected Builder() {
		}

		public Builder key(BulkOperationKey key) {
			Objects.requireNonNull(key, "key is null");
			this.key = key;
			return this;
		}

		public Builder entityList(List<Entity> entityList) {
			Objects.requireNonNull(entityList, "entityList is null");
			this.entityList.addAll(entityList);
			return this;
		}

		public Builder consumer(Consumer<Entity> consumer) {
			Objects.requireNonNull(consumer, "consumer is null");
			this.consumer = consumer;
			return this;
		}

		public EntityBulkOperation build() {
			assert this.key != null;
			assert !this.entityList.isEmpty();
			assert this.consumer != null;
			return new EntityBulkOperation(this);
		}
	}
}
