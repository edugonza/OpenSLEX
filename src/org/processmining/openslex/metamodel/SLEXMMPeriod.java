/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.time.Duration;

import org.processmining.openslex.utils.MMUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMPeriod.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMPeriod extends AbstractDBElement {

	private static final long SINCE_ALWAYS = MMUtils.INFINITE_PAST;
	private static final long UP_UNTIL_NOW = MMUtils.INFINITE_FUTURE;
	/**
	 * 
	 */
	private static final long serialVersionUID = -412409433401409499L;

	/** The start. */
	private long start = UP_UNTIL_NOW;
	
	/** The end. */
	private long end = UP_UNTIL_NOW;
	
	private static int counter = 0;
	
	transient private Duration dur = null;
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
		setDuration(start, end);
		this.setId(counter++);
		getStorage().putInCache(this);
	}
	
	private void setDuration(long start, long end) {
		long s = start;
		long e = end;
		
		if (e == UP_UNTIL_NOW) {
			e = System.currentTimeMillis();
		}
		if (s == SINCE_ALWAYS) {
			s = 0L;
		}
		
		this.dur = Duration.ofMillis(e - s);
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
	
	@Override
	boolean insert(AbstractDBElement dbob) {
		return false;
	}

	@Override
	boolean update(AbstractDBElement dbob) {
		return false;
	}
	
	public Duration getDuration() {
		if (dur == null) {
			setDuration(getStart(), getEnd());
		}
		return dur;
	}
	
}
