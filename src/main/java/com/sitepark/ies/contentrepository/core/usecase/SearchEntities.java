package com.sitepark.ies.contentrepository.core.usecase;

import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import com.sitepark.ies.contentrepository.core.port.ContentRepository;
import com.sitepark.ies.contentrepository.core.usecase.query.Query;
import com.sitepark.ies.contentrepository.core.usecase.query.Result;
import jakarta.inject.Inject;

public final class SearchEntities {

  private final ContentRepository repository;

  @Inject
  SearchEntities(ContentRepository repository) {
    this.repository = repository;
  }

  public Result<Entity> search(Query query) {
    return this.repository.search(query);
  }
}
