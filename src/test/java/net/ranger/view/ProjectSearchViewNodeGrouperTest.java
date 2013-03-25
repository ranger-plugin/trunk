package net.ranger.view;

import java.util.Arrays;
import java.util.List;

import net.ranger.core.CallHierarchyNode;
import net.ranger.core.DefaultCallHierarchyNode;
import net.ranger.core.JavaFile;
import net.ranger.core.Source;
import net.ranger.test.UnitTestHelper;
import net.ranger.view.ClassCallHierarchyNodeGrouper;
import net.ranger.view.ProjectSearchViewNodeGrouper;
import net.ranger.view.SearchViewNode;
import net.ranger.view.SearchViewNodeComposite;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.ILabelProvider;
import org.jmock.Expectations;


/**
 * Unit test for <code>ProjectSearchResultGrouper</code>.
 * 
 * @author Emerson Loureiro
 * 
 */
public class ProjectSearchViewNodeGrouperTest extends UnitTestHelper {

	public ProjectSearchViewNodeGrouper projectGrouper;

	public ClassCallHierarchyNodeGrouper classGrouper;

	@Override
	public void setUpImpl() {
		this.classGrouper = new ClassCallHierarchyNodeGrouper() {
			@Override
			SearchViewNode createCallHierarchyViewNode(CallHierarchyNode node) {
				return ProjectSearchViewNodeGrouperTest.this.createSearchViewNodeMock(node);
			}

			@Override
			SearchViewNodeComposite createSearchViewNodeComposite(IJavaElement element) {
				return ProjectSearchViewNodeGrouperTest.this.createSearchViewNodeCompositeMock(element);
			}
		};

		this.projectGrouper = new ProjectSearchViewNodeGrouper(this.classGrouper) {
			@Override
			SearchViewNodeComposite createSearchViewNode(IJavaElement element) {
				return ProjectSearchViewNodeGrouperTest.this.createSearchViewNodeCompositeMock(element);
			}
		};
	}

	/**
	 * Tests the grouping performed by ProjectSearchViewNodeGrouper.
	 */
	public void testGroup() {
		// The search result to be grouped
		CallHierarchyNode target_1 = new DefaultCallHierarchyNode(null, 1, this.createSourceMock("test1", "project_1", "JavaFile_1.java"));
		CallHierarchyNode target_2 = new DefaultCallHierarchyNode(null, 1, this.createSourceMock("test2", "project_2", "JavaFile_2.java"));
		CallHierarchyNode target_3 = new DefaultCallHierarchyNode(null, 1, this.createSourceMock("test3", "project_2", "JavaFile_3.java"));

		List<SearchViewNode> viewNodes = this.projectGrouper.group(Arrays.asList(target_1, target_2, target_3));

		assertEquals("Incorrect number of view nodes returned", 2, viewNodes.size());
		assertEquals("Incorrect number of children under project node!", 1, viewNodes.get(0).getChildren().size());
		assertEquals("Incorrect number of children under project node!", 2, viewNodes.get(1).getChildren().size());
	}

	// ------------------------
	// UTILITY METHODS
	// ------------------------

	private SearchViewNode createSearchViewNodeMock(final CallHierarchyNode node) {
		final SearchViewNode searchViewNode = this.mock(SearchViewNode.class);

		this.getMockery().checking(new Expectations() {
			{
				atLeast(0).of(searchViewNode).getLabel();
				will(returnValue(node.getCaller().getJavaFile().getName()));

				atLeast(0).of(searchViewNode).getJavaElement();
				will(returnValue(node.getCaller().getJavaElement()));
			}
		});

		return searchViewNode;
	}

	private SearchViewNodeComposite createSearchViewNodeCompositeMock(final IJavaElement element) {
		return new SearchViewNodeComposite(element) {

			@Override
			ILabelProvider getDefaultLabelProvider() {
				return ProjectSearchViewNodeGrouperTest.this.createLabelProviderMock();
			}
		};
	}

	private ILabelProvider createLabelProviderMock() {
		final ILabelProvider labelProvider = this.mock(ILabelProvider.class);

		this.getMockery().checking(new Expectations() {
			{
				atLeast(0).of(labelProvider).getImage(with(anything()));
				will(returnValue(null));

				atLeast(0).of(labelProvider).getText(with(anything()));
				will(returnValue(null));
			}
		});

		return labelProvider;
	}

	private Source createSourceMock(final String name, final String projectName, final String javaFileName) {
		final Source source = this.mock(Source.class);
		final IJavaElement sourceJavaElement = this.mock(IJavaElement.class);
		final JavaFile javaFile = this.mock(JavaFile.class);
		final ICompilationUnit compilationUnit = this.mock(ICompilationUnit.class);
		final IJavaProject javaProject = this.mock(IJavaProject.class);

		this.getMockery().checking(new Expectations() {
			{
				atLeast(0).of(source).getName();
				will(returnValue(name));

				atLeast(0).of(source).getJavaElement();
				will(returnValue(sourceJavaElement));

				atLeast(0).of(source).getJavaFile();
				will(returnValue(javaFile));

				atLeast(0).of(javaFile).getProjectName();
				will(returnValue(projectName));

				atLeast(0).of(javaFile).getName();
				will(returnValue(javaFileName));

				atLeast(0).of(javaFile).getCompilationUnit();
				will(returnValue(compilationUnit));

				atLeast(0).of(compilationUnit).getJavaProject();
				will(returnValue(javaProject));

				atLeast(0).of(compilationUnit).getElementName();
				will(returnValue(javaFileName));

				atLeast(0).of(javaProject).getElementName();
				will(returnValue(projectName));
			}
		});

		return source;
	}
}
