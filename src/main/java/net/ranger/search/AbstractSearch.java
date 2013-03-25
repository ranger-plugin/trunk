package net.ranger.search;

import net.ranger.core.Source;
import net.ranger.core.TrackingConstraint;

/**
 * An abstract implementation of a {@link Search}, simply for reuse. Specific
 * types of searches that can be performed by the plugin should extend this
 * class and provide search-specific behaviour.
 * 
 * @author Emerson Loureiro
 * 
 */
public abstract class AbstractSearch implements Search {

	/** See {@link Search#getConstraint()} */
	private TrackingConstraint constraint;

	/** See {@link Search#getSource()} */
	private Source source;

	/**
	 * Default constructor.
	 * 
	 * @param source
	 *            See {@link Search#getSource()}
	 * @param constraint
	 *            See {@link Search#getConstraint()}
	 */
	public AbstractSearch(Source source, TrackingConstraint constraint) {
		this.source = source;
		this.constraint = constraint;
	}

	/** {@inheritDoc} */
	@Override
	public Source getSource() {
		return this.source;
	}

	/** {@inheritDoc} */
	@Override
	public TrackingConstraint getConstraint() {
		return this.constraint;
	}
}
