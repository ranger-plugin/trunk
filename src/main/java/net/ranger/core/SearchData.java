package net.ranger.core;

/**
 * Encapsulates various search-related data for {@link CallHierarchyNode}
 * objects.
 * 
 * @author Emerson Loureiro
 * 
 */
public interface SearchData {

	/**
	 * Returns the offset, from the start of the file, where the call
	 * encapsulated by a {@link CallHierarchyNode} has been found.
	 * 
	 * @return int
	 */
	int getOffset();

	/**
	 * Returns the length of the selection, starting from the offset, if any. If
	 * there's no selection, 0 returned.
	 * 
	 * @return int
	 */
	int getLength();
}
