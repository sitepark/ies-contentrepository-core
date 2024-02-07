package com.sitepark.ies.contentrepository.core.domain.databind;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.sitepark.ies.contentrepository.core.domain.entity.filter.Filter;
import com.sitepark.ies.contentrepository.core.domain.entity.filter.FilterDeserializer;

public class DatabindModule extends SimpleModule {

  private static final long serialVersionUID = 1L;

  public DatabindModule() {
    super.addDeserializer(Filter.class, new FilterDeserializer());
  }
}
