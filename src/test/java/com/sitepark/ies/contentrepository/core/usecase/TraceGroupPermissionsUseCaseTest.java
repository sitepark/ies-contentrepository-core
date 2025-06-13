package com.sitepark.ies.contentrepository.core.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sitepark.ies.contentrepository.core.domain.value.permission.GroupPermission;
import com.sitepark.ies.contentrepository.core.domain.value.permission.GroupPermissionSource;
import com.sitepark.ies.contentrepository.core.domain.value.permission.GroupPermissionTrace;
import com.sitepark.ies.contentrepository.core.port.Permissions;
import java.util.List;
import org.junit.jupiter.api.Test;

class TraceGroupPermissionsUseCaseTest {
  @Test
  void testTraceGroupPermissions() {

    Permissions permissions = mock();
    when(permissions.getGroupPermissionSources(any(), any()))
        .thenReturn(
            List.of(
                GroupPermissionSource.builder()
                    .groupPermission(GroupPermission.builder().groupId("56").read(true).build())
                    .privilegeId("6")
                    .build(),
                GroupPermissionSource.builder()
                    .groupPermission(GroupPermission.builder().groupId("56").write(true).build())
                    .privilegeId("7")
                    .build()));

    TraceGroupPermissionsUseCase usecase = new TraceGroupPermissionsUseCase(permissions);

    GroupPermissionTrace expected =
        GroupPermissionTrace.builder()
            .resolvedPermission(GroupPermission.builder().read(true).write(true).build())
            .sourcePermissions(
                List.of(
                    GroupPermissionSource.builder()
                        .groupPermission(GroupPermission.builder().groupId("56").read(true).build())
                        .privilegeId("6")
                        .build(),
                    GroupPermissionSource.builder()
                        .groupPermission(
                            GroupPermission.builder().groupId("56").write(true).build())
                        .privilegeId("7")
                        .build()))
            .build();

    assertEquals(
        expected, usecase.traceGroupPermissions("56", "3"), "Unexpected parent path result");
  }
}
