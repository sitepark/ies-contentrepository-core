package com.sitepark.ies.contentrepository.core.port;

import java.util.List;

import com.sitepark.ies.contentrepository.core.domain.entity.Entity;

public interface VersioningManager {

	void removeAllVersions(long id);
	Entity createNewVersion(Entity entity);
	Entity revertToVersion(long version);
	Entity getVersion(long version);
	Entity getNextVersionSince(long timestamp);
	List<Long> getAllMediaReferences(long id);

}
