package net.ranger.core;

import org.eclipse.jdt.core.ICompilationUnit;

/**
 * Name says it all, represents a java file.
 * 
 * @author Emerson Loureiro
 * 
 */
public interface JavaFile {

	/**
	 * Returns the name of the class wrapped by this object.
	 * 
	 * @return String.
	 */
	String getName();

	/**
	 * Returns the name of the project where this class is.
	 * 
	 * @return
	 */
	String getProjectName();

	/**
	 * Checks whether this class is a test class.
	 * 
	 * @return True if this is a test class and false otherwise.
	 */
	boolean isTestClass();

	/**
	 * Returns the {@link ICompilationUnit} that's being wrapped by this object.
	 * 
	 * @return ICompilationUnit
	 */
	ICompilationUnit getCompilationUnit();
}
