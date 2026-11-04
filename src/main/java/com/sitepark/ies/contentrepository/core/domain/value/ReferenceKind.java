package com.sitepark.ies.contentrepository.core.domain.value;

/** The kind of a reference found in the data of a section. */
public enum ReferenceKind {
  /** A link to an entry or a group ({@code ies:entity/<id>}). */
  LINK,
  /** A medium of the media repository ({@code ies:media/<id>}). */
  MEDIA
}
