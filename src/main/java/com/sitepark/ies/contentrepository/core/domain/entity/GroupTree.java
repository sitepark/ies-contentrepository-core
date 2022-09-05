package com.sitepark.ies.contentrepository.core.domain.entity;

import java.util.List;
import java.util.Set;

/**
 * The hierarchy of groups in the content repository
 */
public interface GroupTree {

	/**
	 * Returns the root elements of the tree
	 */
	Set<Long> getRoots();

	/**
	 * Creates a sub-tree starting from the branch specified with <code>root</code>.
	 * @param root Node from where the sub-tree should be created.
	 */
	GroupTree getSubTree(Long node);

	/**
	 * Liefert <tt>true</tt>, wenn Object schon enthalten ist.
	 */
	boolean contains(Long entity);

	/**
	 * Returns <tt>true</tt> if Object is already contained.
	 */
	boolean isEmpty();

	/**
	 * Provides the parent elements of the child
	 */
	Long getParent(Long entity);

	/**
	 *  Returns the subelements of the parent
	 */
	Set<Long> getChildren(Long parent);

	/**
	 * Returns the subelements of the parent rekusive.
	 */
	Set<Long> getChildrenRecursive(Long root);

	/**
	 * Returns <code>true<code> if the parent has subelement.
	 */
	boolean hasChildren(Long parent);

	/**
	 * Returns a list of all elements that have the same
	 * parent including the passed object.
	 */
	Set<Long> getSiblings(Long o);

	/**
	 * Returns all objects contained in this tree
	 */
	Set<Long> getAll();

	/**
	 * Returns the path of an object from the root element to the specified element.
	 * @param o
	 */
	List<Long> getPath(Entity o);

	/**
	 * Number of elements in the tree
	 */
	int size();

	@Override String toString();

	String toString(int indent);

	String toString(int indent, Long parent);

}
