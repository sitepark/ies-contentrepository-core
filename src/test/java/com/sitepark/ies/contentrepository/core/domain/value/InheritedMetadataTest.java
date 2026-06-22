package com.sitepark.ies.contentrepository.core.domain.value;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Locale;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

@SuppressFBWarnings("PI_DO_NOT_REUSE_PUBLIC_IDENTIFIERS_CLASS_NAMES")
class InheritedMetadataTest {

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void testEquals() {
    EqualsVerifier.forClass(InheritedMetadata.class).verify();
  }

  @Test
  void testSiteId() {
    InheritedMetadata metadata = new InheritedMetadata("123", Locale.GERMAN);

    assertEquals("123", metadata.siteId(), "unexpected siteId");
  }

  @Test
  void testLocale() {
    InheritedMetadata metadata = new InheritedMetadata("123", Locale.GERMAN);

    assertEquals(Locale.GERMAN, metadata.locale(), "unexpected locale");
  }
}
