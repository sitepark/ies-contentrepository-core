package com.sitepark.ies.contentrepository.core.domain.entity;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

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

  @Override
  public final int hashCode() {
    return Objects.hash(this.key, this.entityList, this.consumer);
  }

  @Override
  public final boolean equals(Object o) {

    if (!(o instanceof EntityBulkOperation)) {
      return false;
    }

    EntityBulkOperation op = (EntityBulkOperation) o;

    return Objects.equals(this.key, op.key)
        && Objects.equals(this.entityList, op.entityList)
        && Objects.equals(this.consumer, op.consumer);
  }

  @Override
  public String toString() {
    StringBuilder b =
        new StringBuilder(100)
            .append("EntityBulkOperation[key:")
            .append(this.key)
            .append(", entityList:")
            .append(this.entityList)
            .append(", consumer:")
            .append(this.consumer)
            .append(']');
    return b.toString();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private BulkOperationKey key;

    private final List<Entity> entityList = new ArrayList<>();

    private Consumer<Entity> consumer;

    protected Builder() {}

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
      if (this.key == null) {
        throw new IllegalStateException("key must be set");
      }
      if (this.entityList.isEmpty()) {
        throw new IllegalStateException("entity must be set");
      }
      if (this.consumer == null) {
        throw new IllegalStateException("consumer must be set");
      }
      return new EntityBulkOperation(this);
    }
  }
}
