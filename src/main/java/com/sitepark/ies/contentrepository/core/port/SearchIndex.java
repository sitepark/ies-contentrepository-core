package com.sitepark.ies.contentrepository.core.port;

import com.sitepark.ies.contentrepository.core.domain.entity.Entity;

public interface SearchIndex {
	void index(Entity entity);
	void remove(long id);
}
