package com.sitepark.ies.contentrepository.core.domain.exception;

import com.sitepark.ies.contentrepository.core.domain.entity.Anchor;
import java.io.Serial;

public class AnchorNotFoundException extends ContentRepositoryException {

  @Serial private static final long serialVersionUID = 1L;

  private final Anchor anchor;

  public AnchorNotFoundException(Anchor anchor) {
    super();
    this.anchor = anchor;
  }

  public Anchor getAnchor() {
    return this.anchor;
  }

  @Override
  public String getMessage() {
    return "Entity with anchor " + this.anchor + " not found";
  }
}
