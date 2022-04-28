package com.sitepark.ies.contentrepository.core.port;

import java.util.List;

import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import com.sitepark.ies.contentrepository.core.domain.entity.EntityVersion;
import com.sitepark.ies.contentrepository.core.domain.entity.Identifier;

public interface VersioningManager {

	void removeAllVersions(Identifier identifier);
	Entity createNewVersion(Entity entity);
	Entity revertToVersion(EntityVersion version);
	Entity getVersion(EntityVersion version);
	Entity getNextVersionSince(long timestamp);
	List<Long> getAllMediaReferences(Identifier identifier);

}
