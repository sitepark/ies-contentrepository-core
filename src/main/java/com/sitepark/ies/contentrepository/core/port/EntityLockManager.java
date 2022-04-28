package com.sitepark.ies.contentrepository.core.port;

import java.util.List;
import java.util.Optional;

import com.sitepark.ies.contentrepository.core.domain.entity.EntityLock;
import com.sitepark.ies.contentrepository.core.domain.entity.Identifier;

public interface EntityLockManager {
	Optional<EntityLock> getLock(Identifier identifier);
	void lock(Identifier identifier);
	void unlock(Identifier identifier);
	List<EntityLock> getLockList();
}
