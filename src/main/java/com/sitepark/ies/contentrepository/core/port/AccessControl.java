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

  /**
   * Whether the current user holds a grant on an entry or a group. Section types may name a grant
   * that is needed to change their sections.
   */
  boolean isSectionGrantGranted(String entityId, String grant);
}
