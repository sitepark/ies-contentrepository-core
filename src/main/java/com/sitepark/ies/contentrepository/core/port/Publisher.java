package com.sitepark.ies.contentrepository.core.port;

import java.util.List;
import java.util.function.Consumer;

public interface Publisher {
	void republish(long id, long version);
	void depublish(long id);
	void depublish(List<Long> idList, Consumer<Long> consumer);
}
