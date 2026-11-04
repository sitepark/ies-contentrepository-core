package com.sitepark.ies.contentrepository.core.domain.exception;

import java.io.Serial;

/** A section refers to a section type that does not exist. */
public class SectionTypeNotFoundException extends ContentRepositoryException {

  @Serial private static final long serialVersionUID = 1L;

  private final String type;

  public SectionTypeNotFoundException(String type) {
    super();
    this.type = type;
  }

  public String getType() {
    return this.type;
  }

  @Override
  public String getMessage() {
    return "Section type " + this.type + " not found";
  }
}
