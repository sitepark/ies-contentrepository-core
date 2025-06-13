package com.sitepark.ies.contentrepository.core.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sitepark.ies.contentrepository.core.domain.value.permission.SectionGrant;
import com.sitepark.ies.contentrepository.core.port.Permissions;
import java.util.List;
import org.junit.jupiter.api.Test;

class GetAllSectionGrantsTest {

  @Test
  void testGetAllSectionGrants() {
    Permissions permissions = mock();
    when(permissions.getAllSectionGrants())
        .thenReturn(
            List.of(
                new SectionGrant("1", "Section 1", "Description for Section 1"),
                new SectionGrant("2", "Section 2", "Description for Section 2")));
    GetAllSectionGrants usecase = new GetAllSectionGrants(permissions);
    List<SectionGrant> sectionGrants = usecase.getAllSectionGrants();

    assertEquals(
        List.of(
            new SectionGrant("1", "Section 1", "Description for Section 1"),
            new SectionGrant("2", "Section 2", "Description for Section 2")),
        sectionGrants,
        "The section grants should match the expected values.");
  }
}
