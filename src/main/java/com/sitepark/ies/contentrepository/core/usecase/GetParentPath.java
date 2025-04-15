package com.sitepark.ies.contentrepository.core.usecase;

import com.sitepark.ies.contentrepository.core.port.ContentRepository;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;

public final class GetParentPath {

  private final ContentRepository repository;

  @Inject
  GetParentPath(ContentRepository repository) {
    this.repository = repository;
  }

  public Map<String, List<String>> getParentPath(List<String> idList) {
    return this.repository.getParentPath(idList);
  }
}
