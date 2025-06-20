package com.sitepark.ies.contentrepository.core.usecase.query.filter;

@SuppressWarnings("PMD.TooManyMethods")
public interface Filter {

  static IsGroup isGroup(Boolean isGroup) {
    return new IsGroup(isGroup);
  }

  static Id id(String id) {
    return new Id(id);
  }

  static IdList idList(String... idList) {
    return new IdList(idList);
  }

  static Anchor anchor(com.sitepark.ies.sharedkernel.anchor.Anchor anchor) {
    return new Anchor(anchor);
  }

  static AnchorList anchorList(com.sitepark.ies.sharedkernel.anchor.Anchor... anchorList) {
    return new AnchorList(anchorList);
  }

  static Parent parent(String parent) {
    return new Parent(parent);
  }

  static ParentList parentList(String... parentList) {
    return new ParentList(parentList);
  }

  static ParentAnchor parentAnchor(com.sitepark.ies.sharedkernel.anchor.Anchor parentAnchor) {
    return new ParentAnchor(parentAnchor);
  }

  static ParentAnchorList parentAnchorList(
      com.sitepark.ies.sharedkernel.anchor.Anchor... parentAnchorList) {
    return new ParentAnchorList(parentAnchorList);
  }

  static Root root(String root) {
    return new Root(root);
  }

  static RootList rootList(String... rootList) {
    return new RootList(rootList);
  }

  static Name name(String name) {
    return new Name(name);
  }

  static Or or(Filter... filterList) {
    return new Or(filterList);
  }

  static And and(Filter... filterList) {
    return new And(filterList);
  }

  static Not not(Filter filter) {
    return new Not(filter);
  }
}
