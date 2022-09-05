package com.sitepark.ies.contentrepository.core.domain.entity.filter;

@SuppressWarnings("PMD.TooManyMethods")
public interface Filter {

	public static Or or(Filter... filterList) {
		return new Or(filterList);
	}

	public static And and(Filter... filterList) {
		return new And(filterList);
	}

	public static Not not(Filter filter) {
		return new Not(filter);
	}

	public static Id id(Long id) {
		return new Id(id);
	}

	public static IdList idList(Long... idlist) {
		return new IdList(idlist);
	}

	public static Anchor anchor(com.sitepark.ies.contentrepository.core.domain.entity.Anchor anchor) {
		return new Anchor(anchor);
	}

	public static AnchorList anchorList(com.sitepark.ies.contentrepository.core.domain.entity.Anchor... anchorList) {
		return new AnchorList(anchorList);
	}

	public static Parent parent(Long parent) {
		return new Parent(parent);
	}

	public static ParentList parentList(Long... parentList) {
		return new ParentList(parentList);
	}

	public static Root root(Long root) {
		return new Root(root);
	}

	public static RootList rootList(Long... rootList) {
		return new RootList(rootList);
	}
}
