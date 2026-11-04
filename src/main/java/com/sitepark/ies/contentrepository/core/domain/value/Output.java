package com.sitepark.ies.contentrepository.core.domain.value;

/** What publishing an entry produces. */
public enum Output {
  /** A file, e.g. a page. */
  FILE,
  /** Nothing; the entry is published but creates no file (formerly a resource article). */
  NONE
}
