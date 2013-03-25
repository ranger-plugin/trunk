package net.ranger.constraint;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.ranger.core.Source;

import org.eclipse.jdt.core.JavaModelException;


/**
 * A test checker specifically for the IM Testing framework and its core test
 * class (IMTestCase). It's an extension of a JUnit checker, only extending the
 * way test methods are checked to be disabled.
 * 
 * @author Emerson Loureiro
 * 
 */
public class IMTestCaseTestChecker extends JUnitTestChecker {

	public IMTestCaseTestChecker() {
	}

	@Override
	public boolean isDisabled(Source source) {
		return super.isDisabled(source) || this.isPCAMDisabled(source);
	}

	/**
	 * Checks whether the test method of the given source is being disabled
	 * through the IMTestCase API.
	 */
	private boolean isPCAMDisabled(Source source) {
		try {
			String javaSource = source.getJavaFile().getCompilationUnit().getSource();
			String className = source.getJavaFile().getName();
			className = className.replace(".java", "");
			// Tries to find the method call in the unit test that's disabling
			// the test method associated with the provided source
			Pattern pattern = Pattern.compile("^\\s*(super.|this.|\\s)disableTestMethod\\s*\\(.*\\,\\s*\\\"\\s*" + source.getName() + "\\s*\\\".*\\)\\s*\\;", Pattern.MULTILINE);
			Matcher matcher = pattern.matcher(javaSource);

			return matcher.find();
		} catch (JavaModelException e) {
			throw new RuntimeException(e);
		}
	}
}
