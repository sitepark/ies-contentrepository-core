package com.sitepark.ies.contentrepository.core.port;

import java.time.LocalDateTime;
import java.util.List;

import com.sitepark.ies.contentrepository.core.domain.entity.Entity;

public interface VersioningManager {

	void removeAllVersions(String id);
	Entity createNewVersion(Entity entity);
	Entity revertToVersion(LocalDateTime version);
	Entity getVersion(LocalDateTime version);
	Entity getNextVersionSince(LocalDateTime date);
	List<Long> getAllMediaReferences(String id);

}
