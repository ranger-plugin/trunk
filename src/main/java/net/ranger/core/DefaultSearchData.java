package net.ranger.core;

/**
 * A default implementation of {@link SearchData}
 * 
 * @author Emerson Loureiro
 * 
 */
class DefaultSearchData implements SearchData {

	/** @see SearchData#getOffset() */
	private int offset;

	/** @see SearchData#getLength() */
	private int length;

	public DefaultSearchData(int offset, int length) {
		this.offset = offset;
		this.length = length;
	}

	/** {@inheritDoc} */
	@Override
	public int getOffset() {
		return this.offset;
	}

	/** {@inheritDoc} */
	@Override
	public int getLength() {
		return this.length;
	}
}
