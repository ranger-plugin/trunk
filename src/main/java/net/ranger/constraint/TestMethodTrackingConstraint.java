package net.ranger.constraint;

import net.ranger.core.CallHierarchyNode;
import net.ranger.core.TrackingConstraint;

public class TestMethodTrackingConstraint implements TrackingConstraint {

	private int maximumSearchDepth;

	private TestChecker testChecker;

	public TestMethodTrackingConstraint(int maximumSearchDepth) {
		this.maximumSearchDepth = maximumSearchDepth;
		this.testChecker = new IMTestCaseTestChecker();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isTarget(CallHierarchyNode node) {
		return this.testChecker.isTest(node.getCaller()) && !this.testChecker.isDisabled(node.getCaller());
	}

	/** {@inheritDoc} */
	@Override
	public boolean keepGoing(CallHierarchyNode node) {
		return node.getDepth() < this.maximumSearchDepth && !node.getCaller().isWithinAnonymous() && !this.testChecker.isTest(node.getCaller());
	}

	/** Only used for unit testing. */
	void setTestChecker(TestChecker checker) {
		this.testChecker = checker;
	}
}
