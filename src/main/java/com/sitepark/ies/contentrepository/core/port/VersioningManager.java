package com.sitepark.ies.contentrepository.core.port;

import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import java.time.LocalDateTime;
import java.util.List;

public interface VersioningManager {

  void removeAllVersions(String id);

  Entity createNewVersion(Entity entity);

  Entity revertToVersion(LocalDateTime version);

  Entity getVersion(LocalDateTime version);

  Entity getNextVersionSince(LocalDateTime date);

  List<Long> getAllMediaReferences(String id);
}
