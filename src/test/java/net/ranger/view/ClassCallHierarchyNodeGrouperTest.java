package net.ranger.view;

import java.util.Arrays;
import java.util.List;

import net.ranger.core.CallHierarchyNode;
import net.ranger.core.DefaultCallHierarchyNode;
import net.ranger.core.JavaFile;
import net.ranger.core.Source;
import net.ranger.test.UnitTestHelper;
import net.ranger.view.ClassCallHierarchyNodeGrouper;
import net.ranger.view.SearchViewNode;
import net.ranger.view.SearchViewNodeComposite;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.ILabelProvider;
import org.jmock.Expectations;


/**
 * Unit test for <code>ClassSearchResultGrouper</code>.
 * 
 * @author Emerson Loureiro
 * 
 */
public class ClassCallHierarchyNodeGrouperTest extends UnitTestHelper {

	public ClassCallHierarchyNodeGrouper grouper;

	@Override
	public void setUpImpl() {
		this.grouper = new ClassCallHierarchyNodeGrouper() {
			@Override
			SearchViewNode createCallHierarchyViewNode(CallHierarchyNode node) {
				return ClassCallHierarchyNodeGrouperTest.this.mock(SearchViewNode.class);
			}

			@Override
			SearchViewNodeComposite createSearchViewNodeComposite(IJavaElement element) {
				return ClassCallHierarchyNodeGrouperTest.this.createSearchViewNodeCompositeMock(element);
			}
		};
	}

	/**
	 * Tests the grouping performed by ClassSearchResultGrouper.
	 */
	public void testGroup() {
		// The search result to be grouped
		CallHierarchyNode target_1 = new DefaultCallHierarchyNode(null, 1, this.createSourceMock("class_1"));
		CallHierarchyNode target_2 = new DefaultCallHierarchyNode(null, 1, this.createSourceMock("class_2"));

		List<SearchViewNode> viewNodes = this.grouper.group(Arrays.asList(target_1, target_2));

		assertEquals("Incorrect number of view nodes returned", 2, viewNodes.size());
	}

	// -------------------
	// UTILITY METHODS
	// -------------------

	private SearchViewNodeComposite createSearchViewNodeCompositeMock(IJavaElement element) {
		return new SearchViewNodeComposite(element) {
			@Override
			ILabelProvider getDefaultLabelProvider() {
				return ClassCallHierarchyNodeGrouperTest.this.createLabelProviderMock();
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

	private Source createSourceMock(final String className) {
		final Source source = this.mock(Source.class);
		final IJavaElement sourceJavaElement = this.mock(IJavaElement.class);
		final JavaFile javaFile = this.mock(JavaFile.class);
		final ICompilationUnit compilationUnit = this.mock(ICompilationUnit.class);

		this.getMockery().checking(new Expectations() {
			{
				atLeast(0).of(source).getJavaElement();
				will(returnValue(sourceJavaElement));

				atLeast(0).of(source).getJavaFile();
				will(returnValue(javaFile));

				atLeast(0).of(javaFile).getName();
				will(returnValue(className));

				atLeast(0).of(javaFile).getCompilationUnit();
				will(returnValue(compilationUnit));
			}
		});

		return source;
	}
}
