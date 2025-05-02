package com.sitepark.ies.contentrepository.core.usecase;

import com.sitepark.ies.contentrepository.core.domain.entity.permission.*;
import com.sitepark.ies.contentrepository.core.port.Permissions;
import jakarta.inject.Inject;
import java.util.List;

public class TraceGroupPermissionsUseCase {

  private final Permissions permissions;

  @Inject
  protected TraceGroupPermissionsUseCase(Permissions permissions) {
    this.permissions = permissions;
  }

  public GroupPermissionTrace traceGroupPermissions(String groupId, String userId) {
    List<GroupPermissionSource> groupPermissionSource =
        this.permissions.getGroupPermissionSources(groupId, userId);

    GroupPermission.Builder builder =
        GroupPermission.builder()
            .groups(ContainedGroupsPermission.builder().build())
            .entries(ContainedEntriesPermission.builder().build());
    for (GroupPermissionSource source : groupPermissionSource) {
      builder.merge(source.getGroupPermission());
    }

    return GroupPermissionTrace.builder()
        .resolvedPermission(builder.build())
        .sourcePermissions(groupPermissionSource)
        .build();
  }
}
