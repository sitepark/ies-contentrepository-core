package com.sitepark.ies.contentrepository.core.port;

public interface SearchIndex {
	void index(long id);
	void remove(long id);
}
