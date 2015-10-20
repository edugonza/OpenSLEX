package org.processmining.openslex.metamodel;


abstract class SLEXMMAbstractDatabaseObject {
	protected SLEXMMStorage storage = null;
	private boolean dirty = true;
	private boolean inserted = false;
	
	public SLEXMMAbstractDatabaseObject(SLEXMMStorage storage) {
		this.storage = storage;
	}
	
	protected void setInserted(boolean inserted) {
		this.inserted = inserted;
	}
	
	protected boolean isInserted() {
		return this.inserted;
	}
	
	protected boolean isDirty() {
		return this.dirty;
	}
	
	protected void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	
	abstract boolean insert(SLEXMMAbstractDatabaseObject dbob);
	
	abstract boolean update(SLEXMMAbstractDatabaseObject dbob);
	
	public boolean commit() {
		
		if (!isInserted()) {
			if (insert(this)) {
				setInserted(true);
				setDirty(false);
			}
		} else if (isDirty()){
			if (update(this)) {
				setDirty(false);
			}
		}
		
		return !isDirty();
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
