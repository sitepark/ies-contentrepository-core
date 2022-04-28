package com.sitepark.ies.contentrepository.core.port;

import com.sitepark.ies.contentrepository.core.domain.entity.Identifier;

public interface AccessControl {

	boolean isEntityCreateable(Identifier parent);
	boolean isEntityReadable(Identifier identifier);
	boolean isEntityWritable(Identifier identifier);
	boolean isEntityRemovable(Identifier identifier);

	boolean isGroupCreateable(Identifier parent);
	boolean isGroupReadable(Identifier identifier);
	boolean isGroupWritable(Identifier identifier);
	boolean isGroupRemoveable(Identifier identifier);
}
