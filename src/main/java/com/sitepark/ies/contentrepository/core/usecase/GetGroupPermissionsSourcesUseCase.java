package com.sitepark.ies.contentrepository.core.usecase;

import com.sitepark.ies.contentrepository.core.domain.entity.permission.*;
import com.sitepark.ies.contentrepository.core.port.Permissions;
import jakarta.inject.Inject;
import java.util.List;

public class GetGroupPermissionsSourcesUseCase {

  private final Permissions permissions;

  @Inject
  protected GetGroupPermissionsSourcesUseCase(Permissions permissions) {
    this.permissions = permissions;
  }

  public List<GroupPermissionSource> getGroupPermissionsSources(String groupId) {
    return this.permissions.getGroupPermissionSources(groupId);
  }
}
