package com.sitepark.ies.contentrepository.core.domain.exception;

import com.sitepark.ies.contentrepository.core.domain.entity.EntityLock;

public class EntityLockedException extends ContentRepositoryException {

  private static final long serialVersionUID = 1L;

  private final EntityLock lock;

  public EntityLockedException(EntityLock lock) {
    super();
    this.lock = lock;
  }

  public EntityLock getLock() {
    return this.lock;
  }

  @Override
  public String getMessage() {
    return "Entity is locked: " + this.lock;
  }
}
