package com.sitepark.ies.contentrepository.core.domain.entity.filter;

import com.sitepark.ies.contentrepository.core.domain.databind.UniquePropertyPolymorphicDeserializer;

public class FilterDeserializer extends UniquePropertyPolymorphicDeserializer<Filter> {

	private static final long serialVersionUID = 1L;

	public FilterDeserializer() {
		super(Filter.class);
		this.register("id", Id.class);
		this.register("idList", IdList.class);
		this.register("parent", Parent.class);
		this.register("parentList", ParentList.class);
		this.register("root", Root.class);
		this.register("rootList", RootList.class);
		this.register("anchor", Anchor.class);
		this.register("anchorList", AnchorList.class);
		this.register("and", And.class);
		this.register("or", Or.class);
		this.register("not", Not.class);
	}
}
