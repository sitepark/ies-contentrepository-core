package com.sitepark.ies.contentrepository.core.port;

public interface AccessControl {

	boolean isEntityCreateable(long parent);
	boolean isEntityReadable(long id);
	boolean isEntityWritable(long id);
	boolean isEntityRemovable(long id);

	boolean isGroupCreateable(long parent);
	boolean isGroupReadable(long id);
	boolean isGroupWritable(long id);
	boolean isGroupRemoveable(long id);
}
