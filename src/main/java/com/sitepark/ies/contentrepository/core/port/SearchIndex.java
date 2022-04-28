package com.sitepark.ies.contentrepository.core.port;

import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import com.sitepark.ies.contentrepository.core.domain.entity.Identifier;

public interface SearchIndex {
	void index(Entity entity);
	void remove(Identifier identifier);
}
