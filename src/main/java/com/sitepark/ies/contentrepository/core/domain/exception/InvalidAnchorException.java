package com.sitepark.ies.contentrepository.core.domain.exception;

public class InvalidAnchorException extends ContentRepositoryException {

  private final String name;

  private static final long serialVersionUID = 1L;

  public InvalidAnchorException(String name, String message) {
    super(message);
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  @Override
  public String getMessage() {
    return "Invalid anchor '" + this.name + "': " + super.getMessage();
  }
}
