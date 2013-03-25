package net.ranger.constraint;

import net.ranger.constraint.TestChecker;
import net.ranger.constraint.TestMethodTrackingConstraint;
import net.ranger.core.CallHierarchyNode;
import net.ranger.core.DefaultCallHierarchyNode;
import net.ranger.core.Source;
import net.ranger.test.UnitTestHelper;

import org.jmock.Expectations;


/**
 * The class <code>TestMethodTrackingConstraintTest</code> contains tests for
 * the class {@link <code>TestMethodTrackingConstraint</code>}
 * 
 * @pattern JUnit Test Case
 * 
 * @generatedBy CodePro
 * 
 * @author Emerson Loureiro
 */
public class TestMethodTrackingConstraintTest extends UnitTestHelper {

	/**
	 * Tests the
	 * {@link TestMethodTrackingConstraint#keepGoing(net.ranger.core.CallHierarchyNode)}
	 * method
	 */
	public void testKeepGoing() {
		TestMethodTrackingConstraint trackingConstraint = new TestMethodTrackingConstraint(10);
		// Check for a hierarchy node with a depth of less than the maximum,
		// whose source isn't in an anonymous block of code and is not a test
		// method.
		final Source notWithinAnonymousInvokable = this.createSourceMock(false);
		CallHierarchyNode node = new DefaultCallHierarchyNode(null, 0, notWithinAnonymousInvokable);
		trackingConstraint.setTestChecker(this.createTestCheckerMock(false));
		assertTrue("Tracking should determine to keep going!", trackingConstraint.keepGoing(node));

		// Check for a hierarchy node with a depth of less than the maximum,
		// whose source isn't in an anonymous block of code and is a test
		// method.
		node = new DefaultCallHierarchyNode(null, 0, notWithinAnonymousInvokable);
		trackingConstraint.setTestChecker(this.createTestCheckerMock(true));
		assertFalse("Tracking should determine to not keep going!", trackingConstraint.keepGoing(node));

		// Check for a hierarchy node with a depth of more than the maximum
		node = new DefaultCallHierarchyNode(null, 11, (Source) this.mock(Source.class));
		assertFalse("Tracking should determine to not keep going!", trackingConstraint.keepGoing(node));

		// Check for a hierarchy node within the limit and whose invokable is on a anonymous block of code
		final Source withinAnonymousInvokable = this.createSourceMock(true);
		node = new DefaultCallHierarchyNode(null, 0, withinAnonymousInvokable);
		assertFalse("Tracking should determine to not keep going!", trackingConstraint.keepGoing(node));
	}

	private Source createSourceMock(final boolean withinAnonymous) {
		final Source source = this.mock(Source.class);
		this.getMockery().checking(new Expectations() {
			{
				atLeast(0).of(source).isWithinAnonymous();
				will(returnValue(withinAnonymous));
			}
		});
		return source;
	}

	private TestChecker createTestCheckerMock(final boolean isTest) {
		final TestChecker checker = this.mock(TestChecker.class);
		this.getMockery().checking(new Expectations() {
			{
				atLeast(0).of(checker).isTest(with(aNonNull(Source.class)));
				will(returnValue(isTest));
			}
		});
		return checker;
	}
}