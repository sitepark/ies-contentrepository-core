package com.sitepark.ies.contentrepository.core.usecase;

import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import com.sitepark.ies.contentrepository.core.port.ContentRepository;
import com.sitepark.ies.contentrepository.core.usecase.query.Query;
import jakarta.inject.Inject;
import java.util.List;

public final class GetAllEntities {

  private final ContentRepository repository;

  @Inject
  GetAllEntities(ContentRepository repository) {
    this.repository = repository;
  }

  public List<Entity> getAllEntities(Query query) {
    return this.repository.getAll(query);
  }
}
