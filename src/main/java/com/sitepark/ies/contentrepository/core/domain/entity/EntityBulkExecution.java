package com.sitepark.ies.contentrepository.core.domain.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class EntityBulkExecution {

	private final String[] topic;

	private final List<EntityBulkOperation> operations;

	private final EntityBulkOperation finalizer;

	protected EntityBulkExecution(Builder builder) {
		this.topic = builder.topic;
		this.operations = Collections.unmodifiableList(builder.operations);
		this.finalizer = builder.finalizer;
	}

	@SuppressFBWarnings("EI_EXPOSE_REP")
	public String[] getTopic() {
		return this.topic.clone();
	}

	@SuppressFBWarnings("EI_EXPOSE_REP")
	public List<EntityBulkOperation> getOperations() {
		return this.operations;
	}

	public Optional<EntityBulkOperation> getFinalizer() {
		return Optional.ofNullable(this.finalizer);
	}

	@Override
	public final int hashCode() {
		return Objects.hash(
				Arrays.hashCode(this.topic),
				this.operations,
				this.finalizer);
	}


	@Override
	public final boolean equals(Object o) {

		if (!(o instanceof EntityBulkExecution)) {
			return false;
		}

		EntityBulkExecution execution = (EntityBulkExecution)o;

		return Arrays.equals(this.topic, execution.topic) &&
				Objects.equals(this.operations, execution.operations) &&
				Objects.equals(this.finalizer, execution.finalizer);
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder(100)
				.append("EntityBulkExecution[topic:")
				.append(Arrays.toString(this.topic))
				.append(", operations:")
				.append(this.operations)
				.append(", finalizer:")
				.append(this.finalizer)
				.append(']');
		return b.toString();
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private String[] topic;

		private final List<EntityBulkOperation> operations = new ArrayList<>();

		private EntityBulkOperation finalizer;

		protected Builder() {
		}

		/**
		 * Topics are used to display all bulk operations for a specific topic.
		 * Topics are hierarchical and the path of the topic is specified via
		 * a string array. Topics are freely definable.
		 * If e.g. all Topics of <code>level1</code> are queried, all
		 * BulkExecutions recursively below <code>level1</code> are returned.
		 */
		public Builder topic(String... topic) {
			Objects.requireNonNull(topic, "topic is null");
			for (String part : topic) {
				Objects.requireNonNull(part, "operations contains null values");
			}

			this.topic = topic.clone();
			return this;
		}

		@SuppressWarnings("PMD.UseArraysAsList")
		public Builder operation(EntityBulkOperation... operations) {
			Objects.requireNonNull(operations, "operations is null");
			for (EntityBulkOperation operation : operations) {
				Objects.requireNonNull(operation, "operations contains null values");
				this.operations.add(operation);
			}
			return this;
		}

		public Builder finalizer(EntityBulkOperation finalizer) {
			Objects.requireNonNull(finalizer, "finalizer is null");
			this.finalizer = finalizer;
			return this;
		}

		public EntityBulkExecution build() {
			if (this.topic == null) {
				throw new IllegalStateException("topic must be set");
			}
			if (this.operations.isEmpty()) {
				throw new IllegalStateException("operation must be set");
			}
			return new EntityBulkExecution(this);
		}
	}
}
