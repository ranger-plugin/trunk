package net.ranger.view;

/**
 * @author Emerson Loureiro
 * 
 */
public class Groupers {

	public static final CallHierarchyNodeGrouper PROJECT_GROUPER = new ProjectSearchViewNodeGrouper();
	public static final CallHierarchyNodeGrouper DEFAULT_GROUPER = new DefaultSearchResultGrouper();
	public static final CallHierarchyNodeGrouper CLASS_GROUPER = new ClassCallHierarchyNodeGrouper();

	private Groupers() {
	}
}
