package net.ranger.constraint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import net.ranger.constraint.IMTestCaseTestChecker;
import net.ranger.core.JavaFile;
import net.ranger.core.Source;
import net.ranger.test.UnitTestHelper;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.jmock.Expectations;


/**
 * The class <code>IMTestCaseTestCheckerTest</code> contains tests for the class
 * {@link <code>IMTestCaseTestChecker</code>}
 * 
 * @pattern JUnit Test Case
 * 
 * @generatedBy CodePro
 * 
 * @author Emerson Loureiro
 */
public class IMTestCaseTestCheckerTest extends UnitTestHelper {

	private IMTestCaseTestChecker testChecker;

	@Override
	public void setUpImpl() {
		this.testChecker = new IMTestCaseTestChecker();
	}

	/** Tests the basics of the isDisabled method. */
	public void testIsDisabled() throws Exception {
		// Checking with a source that has the test method disabled
		Source source = this.createIsDisabledSourceMock("CompilationUnitSourceMockDisabled.txt");
		assertTrue("Test should be as disabled!", this.testChecker.isDisabled(source));

		// Checking with a source that has the test method enabled
		source = this.createIsDisabledSourceMock("CompilationUnitSourceMockEnabled.txt");
		assertFalse("Test should be as enabled!", this.testChecker.isDisabled(source));
	}

	/**
	 * Tests the isDisabled method, for when a method has been disabled by means
	 * of calling something like <code>disableTestMethod(Class.class, "test1",
	 * "test2", ..., "testN");</code> and any of the methods are being checked
	 * for enable/disable.
	 */
	public void testIsDisabled_MultipleMethodsDisabledInSameCall() throws Exception {
		Source source = this.createIsDisabledSourceMock("CompilationUnitSourceMockDisabled_MultipleTestsSameCall.txt");
		assertTrue("Test should be as disabled!", this.testChecker.isDisabled(source));
	}

	/**
	 * Tests the isDisabled method, for when a method has been disabled by means
	 * of something like
	 * <code>disableTestMethod(getClass(), "test1", ..., "testM");</code>
	 */
	public void testIsDisabled_GetClassInPlaceOfClassName() throws Exception {
		Source source = this.createIsDisabledSourceMock("CompilationUnitSourceMockDisabled_GetClassInPlaceOfClassName.txt");
		assertTrue("Test should be as disabled!", this.testChecker.isDisabled(source));
	}

	/**
	 * Tests the isDisabled method when a method is in a disable call, but the
	 * call is commented out.
	 */
	public void testIsDisabled_DisableCallCommentedOut() throws Exception {
		Source source = this.createIsDisabledSourceMock("CompilationUnitSourceMockDisabled_DisableCallCommentedOut.txt");
		assertFalse("Test should be as enabled!", this.testChecker.isDisabled(source));
	}

	/**
	 * Tests the isDisabled method when it's being called with a super keyword.
	 */
	public void testIsDisabled_SuperBeforeIsDisabled() throws Exception {
		Source source = this.createIsDisabledSourceMock("CompilationUnitSourceMockDisabled_SuperBeforeIsDisabled.txt");
		assertTrue("Test should be as disabled!", this.testChecker.isDisabled(source));
	}

	/**
	 * Tests the isDisabled method when it's being called with a this keyword.
	 */
	public void testIsDisabled_ThisBeforeIsDisabled() throws Exception {
		Source source = this.createIsDisabledSourceMock("CompilationUnitSourceMockDisabled_ThisBeforeIsDisabled.txt");
		assertTrue("Test should be as disabled!", this.testChecker.isDisabled(source));
	}

	private String readFromMockClass(String fileName) throws IOException {
		String contents = "";
		BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
		String line = null;

		while ((line = fileReader.readLine()) != null) {
			contents += line + "\n";
		}

		return contents;
	}

	private Source createIsDisabledSourceMock(final String fileName) throws JavaModelException, IOException {
		final Source source = this.mock(Source.class);
		final IAnnotation ignoreAnnotation = this.mock(IAnnotation.class);
		final JavaFile javaFile = this.mock(JavaFile.class);
		final IMethod javaElement = this.mock(IMethod.class);
		final ICompilationUnit compilationUnit = this.mock(ICompilationUnit.class);

		this.getMockery().checking(new Expectations() {
			{
				atLeast(0).of(source).getJavaElement();
				will(returnValue(javaElement));

				atLeast(0).of(source).getName();
				will(returnValue("testA"));

				atLeast(0).of((IMethod) javaElement).getAnnotation(with(aNonNull(String.class)));
				will(returnValue(ignoreAnnotation));

				atLeast(0).of(ignoreAnnotation).exists();
				will(returnValue(false));

				atLeast(0).of(source).getJavaFile();
				will(returnValue(javaFile));

				atLeast(0).of(javaFile).getCompilationUnit();
				will(returnValue(compilationUnit));

				atLeast(0).of(javaFile).getName();
				will(returnValue("CompilationUnitTest.java"));

				atLeast(0).of(compilationUnit).getSource();
				will(returnValue(IMTestCaseTestCheckerTest.this.readFromMockClass(this.getClass().getResource(fileName).getPath())));
			}
		});

		return source;
	}
}