package com.sitepark.ies.contentrepository.core.domain.service;

import com.sitepark.ies.contentrepository.core.domain.value.InheritedMetadata;

@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface InheritedMetadataResolver {
  InheritedMetadata resolve(String entityId);
}
