package net.ranger.core;

import net.ranger.core.CallHierarchyNode;
import net.ranger.core.TrackingMonitor;

public class MockTrackMonitor implements TrackingMonitor {

	public int callFoundInvoked = 0;

	@Override
	public void callFound(CallHierarchyNode call) {
		this.callFoundInvoked++;
	}
}
