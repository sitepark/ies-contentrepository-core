package com.sitepark.ies.contentrepository.core.port;

import com.sitepark.ies.contentrepository.core.domain.entity.MediaReference;

public interface MediaReferenceManager {
  void removeByReference(String usedBy);

  void addReference(MediaReference reference);
}
