package com.sitepark.ies.contentrepository.core.domain.exception;

import java.io.Serial;

public class AccessDeniedException extends ContentRepositoryException {
  @Serial private static final long serialVersionUID = 1L;

  public AccessDeniedException(String message) {
    super(message);
  }

  public AccessDeniedException(String message, Throwable t) {
    super(message, t);
  }
}
