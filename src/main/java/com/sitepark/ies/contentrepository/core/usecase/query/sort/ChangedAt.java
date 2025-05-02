package com.sitepark.ies.contentrepository.core.usecase.query.sort;

public class ChangedAt extends SortCriteria {

  public ChangedAt(Direction direction) {
    super(direction);
  }

  @Override
  public String toString() {
    return "ChangedAt [direction=" + getDirection() + "]";
  }
}
