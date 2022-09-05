package com.sitepark.ies.contentrepository.core.port;

import java.util.List;
import java.util.Optional;

import com.sitepark.ies.contentrepository.core.domain.entity.EntityLock;

public interface EntityLockManager {
	Optional<EntityLock> getLock(long id);
	EntityLock forceLock(long id);
	void lock(long id);
	void unlock(long id);
	List<EntityLock> getLockList();
}
