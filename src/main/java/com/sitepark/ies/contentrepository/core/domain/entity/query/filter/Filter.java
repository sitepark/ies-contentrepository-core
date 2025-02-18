package com.sitepark.ies.contentrepository.core.domain.entity.query.filter;

@SuppressWarnings("PMD.TooManyMethods")
public interface Filter {

  public static IsGroup isGroup(Boolean isGroup) {
    return new IsGroup(isGroup);
  }

  public static Id id(String id) {
    return new Id(id);
  }

  public static IdList idList(String... idlist) {
    return new IdList(idlist);
  }

  public static Anchor anchor(com.sitepark.ies.contentrepository.core.domain.entity.Anchor anchor) {
    return new Anchor(anchor);
  }

  public static AnchorList anchorList(
      com.sitepark.ies.contentrepository.core.domain.entity.Anchor... anchorList) {
    return new AnchorList(anchorList);
  }

  public static Parent parent(String parent) {
    return new Parent(parent);
  }

  public static ParentList parentList(String... parentList) {
    return new ParentList(parentList);
  }

  public static ParentAnchor parentAnchor(
      com.sitepark.ies.contentrepository.core.domain.entity.Anchor parentAnchor) {
    return new ParentAnchor(parentAnchor);
  }

  public static ParentAnchorList parentAnchorList(
      com.sitepark.ies.contentrepository.core.domain.entity.Anchor... parentAnchorList) {
    return new ParentAnchorList(parentAnchorList);
  }

  public static Root root(String root) {
    return new Root(root);
  }

  public static RootList rootList(String... rootList) {
    return new RootList(rootList);
  }

  public static Or or(Filter... filterList) {
    return new Or(filterList);
  }

  public static And and(Filter... filterList) {
    return new And(filterList);
  }

  public static Not not(Filter filter) {
    return new Not(filter);
  }
}
