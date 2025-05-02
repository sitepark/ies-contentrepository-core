package com.sitepark.ies.contentrepository.core.domain.entity;

import java.util.*;

public class EntityBackgroundExecution {

  private final String[] topic;

  private final List<EntityBackgroundOperation> operations;

  private final EntityBackgroundOperation finalizer;

  protected EntityBackgroundExecution(Builder builder) {
    this.topic = builder.topic;
    this.operations = Collections.unmodifiableList(builder.operations);
    this.finalizer = builder.finalizer;
  }

  public String[] getTopic() {
    return this.topic.clone();
  }

  public List<EntityBackgroundOperation> getOperations() {
    return this.operations;
  }

  public Optional<EntityBackgroundOperation> getFinalizer() {
    return Optional.ofNullable(this.finalizer);
  }

  @Override
  public final int hashCode() {
    return Objects.hash(Arrays.hashCode(this.topic), this.operations, this.finalizer);
  }

  @Override
  public final boolean equals(Object o) {

    if (!(o instanceof EntityBackgroundExecution execution)) {
      return false;
    }

    return Arrays.equals(this.topic, execution.topic)
        && Objects.equals(this.operations, execution.operations)
        && Objects.equals(this.finalizer, execution.finalizer);
  }

  @Override
  public String toString() {
    return "EntityBackgroundExecution{"
        + "topic="
        + Arrays.toString(topic)
        + ", operations="
        + operations
        + ", finalizer="
        + finalizer
        + '}';
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private String[] topic;

    private final List<EntityBackgroundOperation> operations = new ArrayList<>();

    private EntityBackgroundOperation finalizer;

    protected Builder() {}

    /**
     * Topics are used to display all background operations for a specific topic. Topics are
     * hierarchical and the path of the topic is specified via a string array. Topics are freely
     * definable. If e.g. all Topics of <code>level1</code> are queried, all BackgroundExecutions
     * recursively below <code>level1</code> are returned.
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
    public Builder operation(EntityBackgroundOperation... operations) {
      Objects.requireNonNull(operations, "operations is null");
      for (EntityBackgroundOperation operation : operations) {
        Objects.requireNonNull(operation, "operations contains null values");
        this.operations.add(operation);
      }
      return this;
    }

    public Builder finalizer(EntityBackgroundOperation finalizer) {
      Objects.requireNonNull(finalizer, "finalizer is null");
      this.finalizer = finalizer;
      return this;
    }

    public EntityBackgroundExecution build() {
      if (this.topic == null) {
        throw new IllegalStateException("topic must be set");
      }
      if (this.operations.isEmpty()) {
        throw new IllegalStateException("operation must be set");
      }
      return new EntityBackgroundExecution(this);
    }
  }
}
