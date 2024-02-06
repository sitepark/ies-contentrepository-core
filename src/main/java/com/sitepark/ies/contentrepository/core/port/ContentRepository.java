package com.sitepark.ies.contentrepository.core.port;

import java.util.List;
import java.util.Optional;

import com.sitepark.ies.contentrepository.core.domain.entity.Anchor;
import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import com.sitepark.ies.contentrepository.core.domain.entity.query.Query;

public interface ContentRepository {
	boolean isGroup(String id);
	boolean isEmptyGroup(String id);
	Entity store(Entity entity);
	Optional<Entity> get(String id);
	void removeEntity(String id);
	Optional<Long> resolveAnchor(Anchor anchor);
	List<Entity> getAll(Query query);
}
