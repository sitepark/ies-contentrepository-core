package com.sitepark.ies.contentrepository.core.domain.service;

import com.sitepark.ies.contentrepository.core.domain.entity.UrlMapping;

public interface UrlMappingResolver {
  UrlMapping resolve(String entityId);
}
