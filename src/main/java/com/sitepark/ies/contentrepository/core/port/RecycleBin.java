package com.sitepark.ies.contentrepository.core.port;

import java.util.List;
import java.util.Optional;

import com.sitepark.ies.contentrepository.core.domain.entity.Identifier;
import com.sitepark.ies.contentrepository.core.domain.entity.RecycleBinItem;
import com.sitepark.ies.contentrepository.core.domain.entity.RecycleBinItemFilter;

public interface RecycleBin {
	Optional<RecycleBinItem> get(Identifier identifier);
	void add(RecycleBinItem item);
	void remove(Identifier identifier);
	List<RecycleBinItem> find(RecycleBinItemFilter filter);
}
