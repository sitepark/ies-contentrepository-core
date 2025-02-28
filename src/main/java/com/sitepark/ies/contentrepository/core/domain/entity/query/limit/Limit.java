package com.sitepark.ies.contentrepository.core.domain.entity.query.limit;

public interface Limit {
  static OffsetLimit offset(Integer offset, Integer limit) {
    return new OffsetLimit(offset, limit);
  }
}
