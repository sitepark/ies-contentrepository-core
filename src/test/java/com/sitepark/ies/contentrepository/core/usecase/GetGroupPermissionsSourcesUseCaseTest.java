package com.sitepark.ies.contentrepository.core.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sitepark.ies.contentrepository.core.domain.value.permission.GroupPermission;
import com.sitepark.ies.contentrepository.core.domain.value.permission.GroupPermissionSource;
import com.sitepark.ies.contentrepository.core.port.Permissions;
import java.util.List;
import org.junit.jupiter.api.Test;

class GetGroupPermissionsSourcesUseCaseTest {
  @Test
  void testGetGroupPermissionsSourcesUseCase() {
    Permissions permissions = mock();
    when(permissions.getGroupPermissionSources(any()))
        .thenReturn(
            List.of(
                GroupPermissionSource.builder()
                    .privilegeId("1")
                    .groupPermission(GroupPermission.builder().read(true).build())
                    .roleId("2")
                    .build()));

    GetGroupPermissionsSourcesUseCase useCase = new GetGroupPermissionsSourcesUseCase(permissions);

    List<GroupPermissionSource> expected =
        List.of(
            GroupPermissionSource.builder()
                .privilegeId("1")
                .groupPermission(GroupPermission.builder().read(true).build())
                .roleId("2")
                .build());

    assertEquals(
        expected,
        useCase.getGroupPermissionsSources("groupId"),
        "unexpected group permissions sources");
  }
}
