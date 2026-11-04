package com.sitepark.ies.contentrepository.core.port;

import com.fasterxml.jackson.databind.JsonNode;
import com.sitepark.ies.contentrepository.core.domain.entity.SectionData;
import java.util.List;

/**
 * Validates section data against the JSON schema of its section type. The JSON schema library is
 * part of the implementation, not of the core.
 */
@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface SectionSchemaValidator {

  /**
   * Validates the data.
   *
   * @return the violations; empty if the data matches the schema
   */
  List<String> validate(JsonNode schema, SectionData data);
}
