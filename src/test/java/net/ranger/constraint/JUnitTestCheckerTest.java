package net.ranger.constraint;

import net.ranger.constraint.JUnitTestChecker;
import net.ranger.core.JavaFile;
import net.ranger.core.Source;
import net.ranger.test.UnitTestHelper;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.jmock.Expectations;


/**
 * The class <code>JUnitTestCheckerTest</code> contains tests for the class
 * {@link <code>JUnitTestChecker</code>}
 * 
 * @pattern JUnit Test Case
 * 
 * @generatedBy CodePro
 * 
 * @author Emerson Loureiro
 */
public class JUnitTestCheckerTest extends UnitTestHelper {

	private JUnitTestChecker jUnitTestChecker;

	@Override
	public void setUpImpl() {
		this.jUnitTestChecker = new JUnitTestChecker();
	}

	/**
	 * Tests the {@link JUnitTestChecker#isTest(net.ranger.core.Source)}
	 * method.
	 */
	public void testIsTest() throws Exception {
		// First, we check with a source which isn't associated with a method
		IJavaElement nonMethodJavaElement = this.mock(IJavaElement.class);
		Source sourceMock = this.createIsTestSourceMock(nonMethodJavaElement, "testA", false, false);
		assertFalse("Source object doesn't refer to a method, so can't be a test!", this.jUnitTestChecker.isTest(sourceMock));

		// Then, we check with a source associated with a method, whose name doesn't start with "test"
		// and which isn't annotated with the JUnit test annotation
		IMethod methodJavaElement = this.mock(IMethod.class);
		sourceMock = this.createIsTestSourceMock(methodJavaElement, "methodA", false, false);
		assertFalse("Method isn't a test!", this.jUnitTestChecker.isTest(sourceMock));

		// Now check a source associated with a method whose name starts with "test", but isn't defined
		// inside a unit test class, or a class that inherits indirectly from it (so essentially, not a test).
		methodJavaElement = this.mock(IMethod.class);
		sourceMock = this.createIsTestSourceMock(methodJavaElement, "testA", false, false);
		assertFalse("Method isn't a test!", this.jUnitTestChecker.isTest(sourceMock));

		// Now check a source associated with a method whose name starts with "test", and it's defined inside
		// a class that directly/indirectly inherits from the JUnit unit test class
		methodJavaElement = this.mock(IMethod.class);
		sourceMock = this.createIsTestSourceMock(methodJavaElement, "testA", true, false);
		assertTrue("Method is a test!", this.jUnitTestChecker.isTest(sourceMock));

		// Now check a source associated with a method whose name doesn't start with "test", 
		// it's got an JUnit test annotation, but isn't defined
		// inside a class that directly/indirectly inherits from the JUnit unit test class
		methodJavaElement = this.mock(IMethod.class);
		sourceMock = this.createIsTestSourceMock(methodJavaElement, "methodA", false, true);
		assertFalse("Method isn't a test!", this.jUnitTestChecker.isTest(sourceMock));

		// Now check a source associated with a method whose name doesn't start with "test", 
		// it's got an JUnit test annotation, and it's defined inside
		// a class that indirectly inherits from the JUnit unit test class
		methodJavaElement = this.mock(IMethod.class);
		sourceMock = this.createIsTestSourceMock(methodJavaElement, "methodA", true, true);
		assertTrue("Method is a test!", this.jUnitTestChecker.isTest(sourceMock));
	}

	public void testIsDisabled() {
		// Checking that is disabled returns false for a non-method
		IJavaElement javaElement = this.mock(IJavaElement.class);
		Source sourceMock = this.createIsDisabledSourceMock(javaElement, false);
		assertFalse("isDisabled should return false for a non-method!", this.jUnitTestChecker.isDisabled(sourceMock));

		// Checking that isDisabled returns false for a method
		// that's not annotated with the ignore annotation
		IMethod method = this.mock(IMethod.class);
		sourceMock = this.createIsDisabledSourceMock(method, false);
		assertFalse("isDisabled should return false for a method without the ignore annotation!", this.jUnitTestChecker.isDisabled(sourceMock));

		// Checking that isDisabled returns true for a method
		// that's annotated with the ignore annotation
		method = this.mock(IMethod.class);
		sourceMock = this.createIsDisabledSourceMock(method, true);
		assertTrue("isDisabled should return true for a method with the ignore annotation!", this.jUnitTestChecker.isDisabled(sourceMock));
	}

	private Source createIsDisabledSourceMock(final IJavaElement javaElement, final boolean annotated) {
		final Source source = this.mock(Source.class);
		final IAnnotation ignoreAnnotation = this.mock(IAnnotation.class);

		this.getMockery().checking(new Expectations() {
			{
				atLeast(0).of(source).getJavaElement();
				will(returnValue(javaElement));

				if (javaElement instanceof IMethod) {
					atLeast(0).of((IMethod) javaElement).getAnnotation(with(aNonNull(String.class)));
					will(returnValue(ignoreAnnotation));
				}

				atLeast(0).of(ignoreAnnotation).exists();
				if (annotated) {
					will(returnValue(true));
				} else {
					will(returnValue(false));
				}
			}
		});

		return source;
	}

	private Source createIsTestSourceMock(final IJavaElement javaElement, final String name, final boolean test, final boolean annotated) throws Exception {
		final Source source = this.mock(Source.class);
		final JavaFile javaFile = this.mock(JavaFile.class);
		final ICompilationUnit compilationUnit = this.mock(ICompilationUnit.class);
		final IType primaryType = this.mock(IType.class);
		final ITypeHierarchy typeHierarchy = this.mock(ITypeHierarchy.class);
		final IType[] superClasses = new IType[2];
		superClasses[0] = this.mock(IType.class);
		superClasses[1] = this.mock(IType.class);
		final IAnnotation testAnnotation = this.mock(IAnnotation.class);

		this.getMockery().checking(new Expectations() {
			{
				atLeast(0).of(source).getJavaElement();
				will(returnValue(javaElement));

				atLeast(0).of(source).getJavaFile();
				will(returnValue(javaFile));

				atLeast(0).of(source).getName();
				will(returnValue(name));

				if (javaElement instanceof IMethod) {
					atLeast(0).of((IMethod) javaElement).getAnnotation(with(aNonNull(String.class)));
					will(returnValue(testAnnotation));
				}

				atLeast(0).of(testAnnotation).exists();
				if (annotated) {
					will(returnValue(true));
				} else {
					will(returnValue(false));
				}

				atLeast(0).of(javaFile).getCompilationUnit();
				will(returnValue(compilationUnit));

				atLeast(0).of(compilationUnit).findPrimaryType();
				will(returnValue(primaryType));

				atLeast(0).of(primaryType).newSupertypeHierarchy(with(any(IProgressMonitor.class)));
				will(returnValue(typeHierarchy));

				atLeast(0).of(typeHierarchy).getAllSuperclasses(primaryType);
				will(returnValue(superClasses));

				atLeast(0).of(superClasses[0]).getFullyQualifiedName();
				will(returnValue("whatever.UnitTestHelper"));

				atLeast(0).of(superClasses[1]).getFullyQualifiedName();
				if (test) {
					will(returnValue("junit.framework.TestCase"));
				} else {
					will(returnValue("Whatever"));
				}
			}
		});

		return source;
	}
}