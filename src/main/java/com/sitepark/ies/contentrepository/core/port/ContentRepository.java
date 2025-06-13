package com.sitepark.ies.contentrepository.core.port;

import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import com.sitepark.ies.contentrepository.core.usecase.query.Query;
import com.sitepark.ies.contentrepository.core.usecase.query.Result;
import com.sitepark.ies.contentrepository.core.usecase.query.filter.Filter;
import com.sitepark.ies.sharedkernel.anchor.Anchor;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ContentRepository {
  boolean isGroup(String id);

  boolean isEmptyGroup(String id);

  Entity store(Entity entity);

  Optional<Entity> get(String id);

  void removeEntity(String id);

  Optional<Long> resolveAnchor(Anchor anchor);

  List<Entity> getAll(Filter filter);

  Result<Entity> search(Query query);

  Map<String, List<String>> getParentPath(List<String> idList);
}
