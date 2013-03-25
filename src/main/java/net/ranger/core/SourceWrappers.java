package net.ranger.core;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IInitializer;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

/**
 * This class provides factory methods for instantiating various wrappers for
 * the JDT representation of java types, classes, and the like. I'll serve as a
 * central point of creation of these objects, encapsulating not only the logic
 * by which they are created, but also types that implement the wrapper
 * interfaces, without the need to expose them to client code.
 * 
 * @author Emerson Loureiro
 * 
 */
public class SourceWrappers {

	private static final SourceWrappers instance = new SourceWrappers();

	/**
	 * This class only provides static factory methods, no point in having a
	 * visible constructor.
	 */
	private SourceWrappers() {
	}

	/**
	 * Utility method to return a {@link JDTInvokable} out of the given
	 * {@link IJavaElement}.
	 * 
	 * @param javaElement
	 *            The java element.
	 */
	public static Source create(IJavaElement javaElement) {
		Source invokable = null;

		if (javaElement instanceof IMethod || javaElement instanceof IInitializer) {
			invokable = instance.new DefaultSource((IMember) javaElement);
		}

		return invokable;
	}

	// --------------------------------------------
	// PRIVATE IMPLEMENTATIONS OF WRAPPER TYPES
	// --------------------------------------------

	/**
	 * Default implementation of {@link JavaFile}
	 * 
	 * @see JavaFile
	 */
	private class DefaultJavaFile implements JavaFile {

		private ICompilationUnit compilationUnit;

		public DefaultJavaFile(ICompilationUnit compilationUnit) {
			this.compilationUnit = compilationUnit;
		}

		/** {@inheritDoc} */
		@Override
		public String getName() {
			return this.compilationUnit.getElementName();
		}

		/** {@inheritDoc} */
		@Override
		public String getProjectName() {
			return this.compilationUnit.getJavaProject().getElementName();
		}

		/** {@inheritDoc} */
		@Override
		public boolean isTestClass() {
			return false;
		}

		/** {@inheritDoc} */
		@Override
		public ICompilationUnit getCompilationUnit() {
			return this.compilationUnit;
		}
	}

	/**
	 * Default implementation of {@link JDTInvokable}
	 * 
	 * @see JDTInvokable
	 */
	private class DefaultSource implements Source {

		IMember wrappedInvokable;
		private JavaFile javaFile;

		public DefaultSource(IMember wrappedInvokable) {
			this.wrappedInvokable = wrappedInvokable;
			this.javaFile = new DefaultJavaFile(wrappedInvokable.getCompilationUnit());
		}

		/** {@inheritDoc} */
		@Override
		public final IJavaElement getJavaElement() {
			return this.wrappedInvokable;
		}

		/** {@inheritDoc} */
		@Override
		public final String getName() {
			return this.wrappedInvokable.getElementName();
		}

		/** {@inheritDoc} */
		@Override
		public boolean isWithinAnonymous() {
			IJavaElement enclosedType = this.wrappedInvokable.getParent();

			while (enclosedType != null && !(enclosedType instanceof IType)) {
				enclosedType = enclosedType.getParent();
			}

			return (enclosedType == null || !(enclosedType instanceof IType) || enclosedType.getElementName() == null || enclosedType.getElementName().equals(""));
		}

		/** {@inheritDoc} */
		@Override
		public JavaFile getJavaFile() {
			return this.javaFile;
		}

		/** {@inheritDoc} */
		@Override
		public String toString() {
			return this.getName() + " at " + this.getJavaFile().getName();
		}
	}
}
