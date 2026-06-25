package com.sitepark.ies.contentrepository.core.api;

import com.sitepark.ies.contentrepository.core.domain.entity.UrlMapping;

public interface UrlMappingResolver {
  UrlMapping resolve(String entityId);
}
