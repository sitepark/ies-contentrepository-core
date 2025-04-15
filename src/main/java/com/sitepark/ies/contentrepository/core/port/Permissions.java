package com.sitepark.ies.contentrepository.core.port;

import com.sitepark.ies.contentrepository.core.domain.entity.permission.GroupPermissionSource;
import java.util.List;

public interface Permissions {
  List<GroupPermissionSource> getGroupPermissionSources(String groupId, String userId);

  List<GroupPermissionSource> getGroupPermissionSources(String groupId);
}
