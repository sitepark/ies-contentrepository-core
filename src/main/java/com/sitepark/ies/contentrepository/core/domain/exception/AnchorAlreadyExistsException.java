package com.sitepark.ies.contentrepository.core.domain.exception;

import com.sitepark.ies.contentrepository.core.domain.entity.Anchor;

public class AnchorAlreadyExistsException extends ContentRepositoryException {

  private static final long serialVersionUID = 1L;

  private final Anchor anchor;

  private final long owner;

  public AnchorAlreadyExistsException(Anchor anchor, long owner) {
    super();
    this.anchor = anchor;
    this.owner = owner;
  }

  public Anchor getAnchor() {
    return this.anchor;
  }

  public long getOwner() {
    return this.owner;
  }

  @Override
  public String getMessage() {
    return "Anchor " + this.anchor + " already exists for entity " + this.owner;
  }
}
