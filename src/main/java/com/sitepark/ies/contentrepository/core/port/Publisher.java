package com.sitepark.ies.contentrepository.core.port;

public interface Publisher {
	void republish(long id, long version);
	void depublish(long id);
}
