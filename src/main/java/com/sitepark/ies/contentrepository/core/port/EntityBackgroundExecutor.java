package com.sitepark.ies.contentrepository.core.port;

import com.sitepark.ies.contentrepository.core.domain.entity.EntityBackgroundExecution;

@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface EntityBackgroundExecutor {
  /**
   * @return BackgroundExecution ID that can be used to track the progress
   */
  String execute(EntityBackgroundExecution execution);
}
