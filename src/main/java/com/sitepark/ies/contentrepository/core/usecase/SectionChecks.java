package com.sitepark.ies.contentrepository.core.usecase;

import com.sitepark.ies.contentrepository.core.domain.entity.Section;
import com.sitepark.ies.contentrepository.core.domain.entity.SectionData;
import com.sitepark.ies.contentrepository.core.domain.exception.SectionDataInvalidException;
import com.sitepark.ies.contentrepository.core.domain.exception.SectionTypeNotFoundException;
import com.sitepark.ies.contentrepository.core.domain.value.SectionType;
import com.sitepark.ies.contentrepository.core.port.AccessControl;
import com.sitepark.ies.contentrepository.core.port.SectionSchemaValidator;
import com.sitepark.ies.contentrepository.core.port.SectionTypeProvider;
import com.sitepark.ies.sharedkernel.security.AccessDeniedException;
import java.util.List;

/** The checks for a section before it is changed: its type, the grant and the schema. */
final class SectionChecks {

  private final AccessControl accessControl;
  private final SectionTypeProvider sectionTypes;
  private final SectionSchemaValidator schemaValidator;

  SectionChecks(
      AccessControl accessControl,
      SectionTypeProvider sectionTypes,
      SectionSchemaValidator schemaValidator) {
    this.accessControl = accessControl;
    this.sectionTypes = sectionTypes;
    this.schemaValidator = schemaValidator;
  }

  /**
   * Checks that the type of the section exists and that the current user holds its grant, if the
   * type names one.
   *
   * @throws SectionTypeNotFoundException if the type does not exist
   * @throws AccessDeniedException if the grant is missing
   */
  SectionType checkGrant(String entityId, Section section) {
    SectionType type =
        this.sectionTypes
            .get(section.getType())
            .orElseThrow(() -> new SectionTypeNotFoundException(section.getType()));
    type.getGrant()
        .filter(grant -> !this.accessControl.isSectionGrantGranted(entityId, grant))
        .ifPresent(
            grant -> {
              throw new AccessDeniedException(
                  "Not allowed to change section " + section.getUuid() + " of entity " + entityId);
            });
    return type;
  }

  /**
   * Checks the data against the schema of the type, if the type has one.
   *
   * @throws SectionDataInvalidException if the data does not match the schema
   */
  void checkSchema(SectionType type, String uuid, SectionData data) {
    type.getSchema()
        .ifPresent(
            schema -> {
              List<String> violations = this.schemaValidator.validate(schema, data);
              if (!violations.isEmpty()) {
                throw new SectionDataInvalidException(uuid, violations);
              }
            });
  }

  /** Checks the type, the grant and the schema of a section. */
  void check(String entityId, Section section) {
    SectionType type = this.checkGrant(entityId, section);
    this.checkSchema(type, section.getUuid(), section.getData());
  }
}
