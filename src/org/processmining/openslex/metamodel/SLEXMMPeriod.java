package org.processmining.openslex.metamodel;

public class SLEXMMPeriod {

	private long start = -1L;
	private long end = -1L;
	
	public SLEXMMPeriod(long start, long end) {
		this.setStart(start);
		this.setEnd(end);
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}
	
	@Override
	public int hashCode() {
		return new String(getStart()+"#"+getEnd()).hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			return true;
		} else {
			return this.hashCode() == obj.hashCode();
		}
	}
	
}
