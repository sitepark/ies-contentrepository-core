package com.sitepark.ies.contentrepository.core.usecase.query.filter;

import com.sitepark.ies.contentrepository.core.domain.databind.UniquePropertyPolymorphicDeserializer;
import java.io.Serial;

public class FilterDeserializer extends UniquePropertyPolymorphicDeserializer<Filter> {

  @Serial private static final long serialVersionUID = 1L;

  public FilterDeserializer() {
    super(Filter.class);
    super.register("id", Id.class);
    super.register("idList", IdList.class);
    super.register("parent", Parent.class);
    super.register("parentList", ParentList.class);
    super.register("root", Root.class);
    super.register("rootList", RootList.class);
    super.register("anchor", Anchor.class);
    super.register("anchorList", AnchorList.class);
    super.register("name", Name.class);
    super.register("and", And.class);
    super.register("or", Or.class);
    super.register("not", Not.class);
  }
}
