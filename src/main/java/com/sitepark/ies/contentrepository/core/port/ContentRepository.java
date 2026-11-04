package com.sitepark.ies.contentrepository.core.port;

import com.sitepark.ies.contentrepository.core.domain.entity.Anchor;
import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import com.sitepark.ies.contentrepository.core.domain.entity.Entry;
import com.sitepark.ies.contentrepository.core.domain.entity.Group;
import com.sitepark.ies.contentrepository.core.domain.entity.query.Query;
import java.util.List;
import java.util.Optional;

public interface ContentRepository {
  boolean isGroup(String id);

  boolean isEmptyGroup(String id);

  Entity store(Entity entity);

  Optional<Entity> get(String id);

  /** Returns the entry with its sections; empty if there is none or the id names a group. */
  Optional<Entry> getEntry(String id);

  /** Returns the group with its sections; empty if there is none or the id names an entry. */
  Optional<Group> getGroup(String id);

  void removeEntity(String id);

  Optional<Long> resolveAnchor(Anchor anchor);

  List<Entity> getAll(Query query);
}
