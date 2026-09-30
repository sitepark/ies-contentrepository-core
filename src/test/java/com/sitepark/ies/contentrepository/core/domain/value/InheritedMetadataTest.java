package com.sitepark.ies.contentrepository.core.domain.value;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class InheritedMetadataTest {

  @Test
  void testEquals() {
    EqualsVerifier.forClass(InheritedMetadata.class).verify();
  }

  @Test
  void testSiteId() {
    InheritedMetadata metadata = new InheritedMetadata("123", null, Locale.GERMAN);

    assertEquals("123", metadata.siteId(), "unexpected siteId");
  }

  @Test
  void testMicrositeId() {
    InheritedMetadata metadata = new InheritedMetadata("123", "345", Locale.GERMAN);

    assertEquals("345", metadata.micrositeId(), "unexpected siteId");
  }

  @Test
  void testLocale() {
    InheritedMetadata metadata = new InheritedMetadata("123", null, Locale.GERMAN);

    assertEquals(Locale.GERMAN, metadata.locale(), "unexpected locale");
  }
}
