package com.sitepark.ies.contentrepository.core.domain.entity.sort;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.Objects;

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

  @Override
  public final int hashCode() {
    return Objects.hash(this.name, this.direction);
  }

  @Override
  public final boolean equals(Object o) {

    if (!(o instanceof OrderCriteria)) {
      return false;
    }

    OrderCriteria criteria = (OrderCriteria) o;

    return Objects.equals(this.name, criteria.name)
        && Objects.equals(this.direction, criteria.direction);
  }

  public abstract Builder<?> toBuilder();

  @JsonPOJOBuilder(withPrefix = "", buildMethodName = "build")
  public abstract static class Builder<B extends Builder<B>> {

    private String name;

    private Direction direction = Direction.DESC;

    protected Builder() {}

    protected Builder(OrderCriteria sortCriteria) {
      this.name = sortCriteria.name;
      this.direction = sortCriteria.direction;
    }

    public B name(String name) {
      Objects.requireNonNull(name, "name is null");
      this.name = name;
      return this.self();
    }

    public B direction(Direction direction) {
      Objects.requireNonNull(direction, "direction is null");
      this.direction = direction;
      return this.self();
    }

    public abstract B self();

    public abstract OrderCriteria build();
  }
}
