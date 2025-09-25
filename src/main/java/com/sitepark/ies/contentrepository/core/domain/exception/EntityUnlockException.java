package com.sitepark.ies.contentrepository.core.domain.exception;

import com.sitepark.ies.contentrepository.core.domain.entity.EntityLock;
import com.sitepark.ies.sharedkernel.domain.DomainException;
import java.io.Serial;

public class EntityUnlockException extends DomainException {

  @Serial private static final long serialVersionUID = 1L;

  private final EntityLock lock;

  public EntityUnlockException(EntityLock lock, Throwable cause) {
    super("Unable to unlock: " + lock, cause);
    this.lock = lock;
  }

  public EntityLock getLock() {
    return this.lock;
  }
}
