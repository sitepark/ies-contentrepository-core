package com.sitepark.ies.contentrepository.core.port;

import java.util.Collection;

public interface MediaRepository {
	void remove(long id);
	void remove(Collection<Long> idList);
}
