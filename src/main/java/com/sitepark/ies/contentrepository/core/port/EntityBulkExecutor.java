package com.sitepark.ies.contentrepository.core.port;

import com.sitepark.ies.contentrepository.core.domain.entity.EntityBulkExecution;

public interface EntityBulkExecutor {
	String execute(EntityBulkExecution execution);
}
