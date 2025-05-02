package com.sitepark.ies.contentrepository.core.domain.databind;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.sitepark.ies.contentrepository.core.usecase.query.filter.Filter;
import com.sitepark.ies.contentrepository.core.usecase.query.filter.FilterDeserializer;
import java.io.Serial;

public class DatabindModule extends SimpleModule {

  @Serial private static final long serialVersionUID = 1L;

  public DatabindModule() {
    super.addDeserializer(Filter.class, new FilterDeserializer());
  }
}
