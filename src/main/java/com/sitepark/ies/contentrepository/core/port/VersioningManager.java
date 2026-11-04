package com.sitepark.ies.contentrepository.core.port;

import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import java.time.Instant;
import java.util.List;

public interface VersioningManager {

  void removeAllVersions(String id);

  Entity createNewVersion(Entity entity);

  Entity revertToVersion(String id, Instant version);

  Entity getVersion(String id, Instant version);

  Entity getNextVersionSince(String id, Instant date);

  List<Long> getAllMediaReferences(String id);
}
