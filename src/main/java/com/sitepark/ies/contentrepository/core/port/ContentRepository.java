package com.sitepark.ies.contentrepository.core.port;

import java.util.Optional;

import com.sitepark.ies.contentrepository.core.domain.entity.Anchor;
import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import com.sitepark.ies.contentrepository.core.domain.entity.Identifier;

public interface ContentRepository {
	boolean isGroup(long id);
	boolean isEmptyGroup(long id);
	Optional<Entity> store(Entity entity);
	Optional<Entity> get(long id);
	void removeEntity(long id);
	void removeGroup(long id);
	Optional<Identifier> resolveAnchor(Anchor anchor);
	long resolve(Identifier identifier);
}
