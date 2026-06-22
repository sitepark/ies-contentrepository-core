package com.sitepark.ies.contentrepository.core.domain.service;

import com.sitepark.ies.contentrepository.core.domain.entity.UrlMapping;

@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface UrlMappingResolver {
  UrlMapping resolve(String entityId);
}
