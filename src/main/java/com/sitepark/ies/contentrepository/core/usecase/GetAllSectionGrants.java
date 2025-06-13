package com.sitepark.ies.contentrepository.core.usecase;

import com.sitepark.ies.contentrepository.core.domain.value.permission.SectionGrant;
import com.sitepark.ies.contentrepository.core.port.Permissions;
import jakarta.inject.Inject;
import java.util.List;

public final class GetAllSectionGrants {

  private final Permissions permissions;

  @Inject
  GetAllSectionGrants(Permissions permissions) {
    this.permissions = permissions;
  }

  public List<SectionGrant> getAllSectionGrants() {
    return this.permissions.getAllSectionGrants();
  }
}
