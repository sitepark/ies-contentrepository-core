package com.sitepark.ies.contentrepository.core.port;

import java.util.List;
import java.util.Optional;

import com.sitepark.ies.contentrepository.core.domain.entity.RecycleBinItem;
import com.sitepark.ies.contentrepository.core.domain.entity.RecycleBinItemFilter;

public interface RecycleBin {
	Optional<RecycleBinItem> get(long id);
	void add(RecycleBinItem item);
	void removeByObject(long id);
	List<RecycleBinItem> find(RecycleBinItemFilter filter);
}
