package net.ranger.constraint;

import java.util.List;

import net.ranger.core.CallHierarchyNode;
import net.ranger.core.Source;
import net.ranger.core.TrackingConstraint;
import net.ranger.extensions.TestChecker;

public class TestMethodTrackingConstraint implements TrackingConstraint {

	private final int maximumSearchDepth;

	private List<TestChecker> testCheckers;

	public TestMethodTrackingConstraint(int maximumSearchDepth, List<TestChecker> testCheckers) {
		this.maximumSearchDepth = maximumSearchDepth;
		this.testCheckers = testCheckers;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isTarget(CallHierarchyNode node) {
		for (TestChecker testChecker : this.testCheckers) {
			if (testChecker.isTest(node.getCaller()) && !testChecker.isDisabled(node.getCaller())) {
				return true;
			}
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean keepGoing(CallHierarchyNode node) {
		return node.getDepth() < this.maximumSearchDepth && !node.getCaller().isWithinAnonymous() && !isTest(node.getCaller());
	}

	/** Only used for unit testing. */
	void setTestChecker(List<TestChecker> checkers) {
		this.testCheckers = checkers;
	}

	private boolean isTest(Source source) {
		for (TestChecker testChecker : this.testCheckers) {
			if (testChecker.isTest(source)) {
				return true;
			}
		}
		return false;
	}
}
