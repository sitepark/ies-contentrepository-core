package com.sitepark.ies.contentrepository.core.usecase;

import com.sitepark.ies.contentrepository.core.api.InheritedMetadataResolver;
import com.sitepark.ies.contentrepository.core.domain.value.InheritedMetadata;
import com.sitepark.ies.contentrepository.core.port.AccessControl;
import com.sitepark.ies.sharedkernel.security.AccessDeniedException;
import jakarta.inject.Inject;
import java.util.Objects;

/**
 * Resolves the metadata an entity inherits from its surrounding hierarchy (e.g. the owning site and
 * its locale).
 */
public final class GetInheritedMetadataUseCase {

  private final InheritedMetadataResolver resolver;

  private final AccessControl accessControl;

  @Inject
  public GetInheritedMetadataUseCase(
      InheritedMetadataResolver resolver, AccessControl accessControl) {
    this.resolver = resolver;
    this.accessControl = accessControl;
  }

  /**
   * Resolves the inherited metadata for the given entity.
   *
   * @param entityId the id of the entity whose inherited metadata is resolved, must not be null
   * @return the resolved inherited metadata
   * @throws AccessDeniedException if the current user is not allowed to read the entity
   */
  public InheritedMetadata getInheritedMetadata(String entityId) {

    Objects.requireNonNull(entityId, "entityId must not be null");

    if (!this.accessControl.isEntityReadable(entityId)) {
      throw new AccessDeniedException("Not allowed to read entity " + entityId);
    }

    return this.resolver.resolve(entityId);
  }
}
