package com.sitepark.ies.contentrepository.core.port;

import com.sitepark.ies.contentrepository.core.domain.value.InheritedMetadata;

public interface InheritedMetadataResolver {
  InheritedMetadata resolve(String entityId);
}
