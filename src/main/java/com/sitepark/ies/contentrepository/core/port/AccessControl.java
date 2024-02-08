package com.sitepark.ies.contentrepository.core.port;

public interface AccessControl {

  boolean isEntityCreateable(String parent);

  boolean isEntityReadable(String id);

  boolean isEntityWritable(String id);

  boolean isEntityRemovable(String id);

  boolean isGroupCreateable(String parent);

  boolean isGroupReadable(String id);

  boolean isGroupWritable(String id);

  boolean isGroupRemoveable(String id);
}
