package com.sitepark.ies.contentrepository.core.port;

import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;

public interface Publisher {
  void republish(String id, Instant version);

  void depublish(String id);

  void depublish(List<String> idList, Consumer<String> consumer);
}
