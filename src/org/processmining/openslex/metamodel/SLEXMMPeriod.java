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
public class SLEXMMPeriod extends AbstractDBElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -412409433401409499L;

	/** The start. */
	private long start = -1L;
	
	/** The end. */
	private long end = -1L;
	
	private static int counter = 0;
	/**
	 * Instantiates a new SLEXMM period.
	 *
	 * @param start the start
	 * @param end the end
	 */
	public SLEXMMPeriod(SLEXMMStorageMetaModel storage, long start, long end) {
		super(storage);
		this.start = start;
		this.end = end;
		this.setId(counter++);
	}
	
	public SLEXMMPeriod(SLEXMMStorageMetaModel storage) {
		this(storage,-1L,-1L);
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
	 * Gets the end.
	 *
	 * @return the end
	 */
	public long getEnd() {
		return end;
	}
	
	public void setStart(long start) {
		this.start = start;
	}
	
	public void setEnd(long end) {
		this.end = end;
	}
	
	@Override
	boolean insert(AbstractDBElement dbob) {
		return false;
	}

	@Override
	boolean update(AbstractDBElement dbob) {
		return false;
	}
	
}
