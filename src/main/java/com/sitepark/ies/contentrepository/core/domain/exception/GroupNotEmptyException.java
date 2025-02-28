package com.sitepark.ies.contentrepository.core.domain.exception;

import java.io.Serial;

public class GroupNotEmptyException extends ContentRepositoryException {
  @Serial private static final long serialVersionUID = 1L;

  private final String id;

  public GroupNotEmptyException(String id) {
    super();
    this.id = id;
  }

  public String getId() {
    return this.id;
  }

  @Override
  public String getMessage() {
    return "Group with id " + this.id + " not empty";
  }
}
