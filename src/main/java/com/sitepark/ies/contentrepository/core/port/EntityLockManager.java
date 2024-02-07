package com.sitepark.ies.contentrepository.core.port;

import com.sitepark.ies.contentrepository.core.domain.entity.EntityLock;
import java.util.List;
import java.util.Optional;

public interface EntityLockManager {
  Optional<EntityLock> getLock(String id);

  EntityLock forceLock(String id);

  void lock(String id);

  void unlock(String id);

  List<EntityLock> getLockList();
}
