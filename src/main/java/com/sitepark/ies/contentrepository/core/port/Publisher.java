package com.sitepark.ies.contentrepository.core.port;

import com.sitepark.ies.contentrepository.core.domain.entity.Identifier;

public interface Publisher {
	void republish(Identifier identifier, long version);
	void depublish(Identifier identifier);
}
