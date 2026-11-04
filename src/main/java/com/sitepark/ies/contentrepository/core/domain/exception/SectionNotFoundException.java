package com.sitepark.ies.contentrepository.core.domain.exception;

import java.io.Serial;

/** An entry or a group has no section with the given UUID. */
public class SectionNotFoundException extends ContentRepositoryException {

  @Serial private static final long serialVersionUID = 1L;

  private final String uuid;

  public SectionNotFoundException(String uuid) {
    super();
    this.uuid = uuid;
  }

  public String getUuid() {
    return this.uuid;
  }

  @Override
  public String getMessage() {
    return "Section with uuid " + this.uuid + " not found";
  }
}
