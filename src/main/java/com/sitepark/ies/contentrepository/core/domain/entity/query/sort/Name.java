package com.sitepark.ies.contentrepository.core.domain.entity.query.sort;

public class Name extends SortCriteria {

  public Name(Direction direction) {
    super(direction);
  }

  @Override
  public String toString() {
    return "Name [direction=" + getDirection() + "]";
  }
}
