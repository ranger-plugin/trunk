package net.ranger.core;

import org.eclipse.jdt.core.IJavaElement;

/**
 * Represents the source of a search performed by the plugin. When searching for
 * all tests whose execution can potentially go through a method <code>A</code>,
 * then <code>A</code> is called the source.
 * 
 * @author Emerson Loureiro
 * 
 */
public interface Source {

	/**
	 * Returns the name of the source (e.g., method name).
	 * 
	 * @return String
	 */
	String getName();

	/**
	 * Returns the java file where this source is. Keep in mind that, for a
	 * method, for example, this does not necessarily mean that the method is
	 * defined in the class of the file returned. If method <code>A</code>, for
	 * example, is defined in a class <code>X</code>, which is an inner class of
	 * <code>Y.java</code>, the method then returns <code>Y.java</code>, even
	 * though the method is essentially defined at the <code>X</code> class.
	 * 
	 * @return {@link JavaFile}
	 */
	JavaFile getJavaFile();

	/**
	 * Checks whether this source is defined within an anonymous block or not.
	 * 
	 * @return True if it is within an anonymous block and false otherwise.
	 */
	boolean isWithinAnonymous();

	/**
	 * Returns the {@link IJavaElement} that's being wrapped by this source.
	 * 
	 * @return IJavaElement
	 */
	IJavaElement getJavaElement();
}
