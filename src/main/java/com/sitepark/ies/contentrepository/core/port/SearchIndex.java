package com.sitepark.ies.contentrepository.core.port;

public interface SearchIndex {
  void index(String id);

  void remove(String id);
}
