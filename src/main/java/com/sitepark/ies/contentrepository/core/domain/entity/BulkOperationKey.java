package com.sitepark.ies.contentrepository.core.domain.entity;

public enum BulkOperationKey {
  PURGE_LOCK("contentrepository.purge.lock"),
  PURGE_DEPUBLISH("contentrepository.purge.depublish"),
  PURGE_PURGE("contentrepository.purge.purge"),
  PURGE_CLEANUP("contentrepository.purge.cleanup");

  private final String name;

  private BulkOperationKey(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
