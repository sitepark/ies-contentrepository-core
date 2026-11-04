package com.sitepark.ies.contentrepository.core.domain.exception;

import java.io.Serial;
import java.time.Instant;

/** An entity was changed since the version the change is based on. */
public class VersionConflictException extends ContentRepositoryException {

  @Serial private static final long serialVersionUID = 1L;

  private final String id;

  private final Instant expectedVersion;

  private final Instant actualVersion;

  public VersionConflictException(String id, Instant expectedVersion, Instant actualVersion) {
    super();
    this.id = id;
    this.expectedVersion = expectedVersion;
    this.actualVersion = actualVersion;
  }

  public String getId() {
    return this.id;
  }

  public Instant getExpectedVersion() {
    return this.expectedVersion;
  }

  public Instant getActualVersion() {
    return this.actualVersion;
  }

  @Override
  public String getMessage() {
    return "Entity "
        + this.id
        + " has version "
        + this.actualVersion
        + ", expected "
        + this.expectedVersion;
  }
}
