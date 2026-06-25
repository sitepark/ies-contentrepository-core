package com.sitepark.ies.contentrepository.core.api;

import com.sitepark.ies.contentrepository.core.domain.value.InheritedMetadata;

public interface InheritedMetadataResolver {
  InheritedMetadata resolve(String entityId);
}
