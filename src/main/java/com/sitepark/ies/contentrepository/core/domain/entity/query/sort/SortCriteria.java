package com.sitepark.ies.contentrepository.core.domain.entity.query.sort;

import java.util.Objects;

public class SortCriteria {

  private final Direction direction;

  protected SortCriteria(Direction direction) {
    this.direction = direction;
  }

  public Direction getDirection() {
    return this.direction;
  }

  @Override
  public final int hashCode() {
    return Objects.hash(this.direction);
  }

  @Override
  public final boolean equals(Object o) {
    return (o instanceof SortCriteria that) && Objects.equals(this.direction, that.direction);
  }
}
