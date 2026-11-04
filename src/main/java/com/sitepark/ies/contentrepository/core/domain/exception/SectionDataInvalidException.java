package com.sitepark.ies.contentrepository.core.domain.exception;

import java.io.Serial;
import java.util.List;

/** The data of a section does not match the JSON schema of its section type. */
public class SectionDataInvalidException extends ContentRepositoryException {

  @Serial private static final long serialVersionUID = 1L;

  private final String uuid;

  private final List<String> violations;

  public SectionDataInvalidException(String uuid, List<String> violations) {
    super();
    this.uuid = uuid;
    this.violations = List.copyOf(violations);
  }

  public String getUuid() {
    return this.uuid;
  }

  public List<String> getViolations() {
    return this.violations;
  }

  @Override
  public String getMessage() {
    return "Data of section " + this.uuid + " is invalid: " + this.violations;
  }
}
