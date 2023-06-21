package com.sitepark.ies.contentrepository.core.domain.entity.filter;

import com.sitepark.ies.contentrepository.core.domain.databind.UniquePropertyPolymorphicDeserializer;

public class FilterDeserializer extends UniquePropertyPolymorphicDeserializer<Filter> {

	private static final long serialVersionUID = 1L;

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
		super.register("and", And.class);
		super.register("or", Or.class);
		super.register("not", Not.class);
	}
}
