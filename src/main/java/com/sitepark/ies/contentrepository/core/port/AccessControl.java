package com.sitepark.ies.contentrepository.core.port;

public interface AccessControl {

  boolean isEntityCreatable(String parent);

  boolean isEntityReadable(String id);

  boolean isEntityWritable(String id);

  boolean isEntityRemovable(String id);

  boolean isGroupCreatable(String parent);

  boolean isGroupReadable(String id);

  boolean isGroupWritable(String id);

  boolean isGroupRemovable(String id);
}
