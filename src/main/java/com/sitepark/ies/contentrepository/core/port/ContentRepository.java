package com.sitepark.ies.contentrepository.core.port;

import java.util.List;
import java.util.Optional;

import com.sitepark.ies.contentrepository.core.domain.entity.Anchor;
import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import com.sitepark.ies.contentrepository.core.domain.entity.Identifier;

public interface ContentRepository {
	Optional<Entity> store(Entity entity);
	Optional<Entity> get(Identifier identifier);
	Optional<Entity> remove(Identifier identifier);
	Optional<Identifier> resolveAnchor(Anchor anchor);
	List<Long> getAllMediaReferences(Identifier identifier);
}
