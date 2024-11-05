package com.sitepark.ies.contentrepository.core.domain.entity;

public enum BackgroundOperationKey {
  PURGE_LOCK("contentrepository.purge.lock"),
  PURGE_DEPUBLISH("contentrepository.purge.depublish"),
  PURGE_PURGE("contentrepository.purge.purge"),
  PURGE_CLEANUP("contentrepository.purge.cleanup");

  private final String name;

  private BackgroundOperationKey(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
