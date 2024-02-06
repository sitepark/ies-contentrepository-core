package com.sitepark.ies.contentrepository.core.port;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

public interface Publisher {
	void republish(String id, LocalDateTime version);
	void depublish(String id);
	void depublish(List<String> idList, Consumer<String> consumer);
}
