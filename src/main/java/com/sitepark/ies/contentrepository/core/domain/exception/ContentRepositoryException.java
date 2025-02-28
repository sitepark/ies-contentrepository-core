package com.sitepark.ies.contentrepository.core.domain.exception;

import java.io.Serial;

public abstract class ContentRepositoryException extends RuntimeException {

  @Serial private static final long serialVersionUID = 1L;

  public ContentRepositoryException() {
    super();
  }

  public ContentRepositoryException(String message) {
    super(message);
  }

  public ContentRepositoryException(String message, Throwable t) {
    super(message, t);
  }
}
