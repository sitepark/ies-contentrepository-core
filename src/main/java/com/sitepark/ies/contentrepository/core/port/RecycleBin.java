package com.sitepark.ies.contentrepository.core.port;

import com.sitepark.ies.contentrepository.core.domain.entity.RecycleBinItem;
import com.sitepark.ies.contentrepository.core.domain.entity.RecycleBinItemFilter;
import java.util.List;
import java.util.Optional;

public interface RecycleBin {
  Optional<RecycleBinItem> get(String id);

  void add(RecycleBinItem item);

  void removeByObject(String id);

  List<RecycleBinItem> find(RecycleBinItemFilter filter);
}
