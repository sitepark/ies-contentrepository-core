package com.sitepark.ies.contentrepository.core.port;

import java.util.List;

public interface ExtensionsNotifier {

	void notifyPurge(long id);

	void notifyBulkPurge(List<Long> idList);
}
