/*
 * 
 */
package org.processmining.openslex.metamodel;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMPeriod.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMPeriod {

	/** The start. */
	private long start = -1L;
	
	/** The end. */
	private long end = -1L;
	
	/**
	 * Instantiates a new SLEXMM period.
	 *
	 * @param start the start
	 * @param end the end
	 */
	public SLEXMMPeriod(long start, long end) {
		this.setStart(start);
		this.setEnd(end);
	}

	/**
	 * Gets the start.
	 *
	 * @return the start
	 */
	public long getStart() {
		return start;
	}

	/**
	 * Sets the start.
	 *
	 * @param start the new start
	 */
	public void setStart(long start) {
		this.start = start;
	}

	/**
	 * Gets the end.
	 *
	 * @return the end
	 */
	public long getEnd() {
		return end;
	}

	/**
	 * Sets the end.
	 *
	 * @param end the new end
	 */
	public void setEnd(long end) {
		this.end = end;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new String(getStart()+"#"+getEnd()).hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			return true;
		} else {
			return this.hashCode() == obj.hashCode();
		}
	}
	
}
