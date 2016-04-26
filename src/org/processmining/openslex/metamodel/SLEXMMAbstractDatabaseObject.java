/*
 * 
 */
package org.processmining.openslex.metamodel;


// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMAbstractDatabaseObject.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public abstract class SLEXMMAbstractDatabaseObject {
	
	/** The storage. */
	protected SLEXMMStorage storage = null;
	
	/** The dirty. */
	private boolean dirty = true;
	
	/** The inserted. */
	private boolean inserted = false;
	
	/** The id. */
	private int id = -1;
	
	/**
	 * Instantiates a new SLEXMM abstract database object.
	 *
	 * @param storage the storage
	 */
	public SLEXMMAbstractDatabaseObject(SLEXMMStorage storage) {
		this.storage = storage;
	}
	
	/**
	 * Sets the inserted.
	 *
	 * @param inserted the new inserted
	 */
	protected void setInserted(boolean inserted) {
		this.inserted = inserted;
	}
	
	/**
	 * Checks if is inserted.
	 *
	 * @return true, if is inserted
	 */
	protected boolean isInserted() {
		return this.inserted;
	}
	
	/**
	 * Checks if is dirty.
	 *
	 * @return true, if is dirty
	 */
	protected boolean isDirty() {
		return this.dirty;
	}
	
	/**
	 * Sets the dirty.
	 *
	 * @param dirty the new dirty
	 */
	protected void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	protected void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Insert.
	 *
	 * @param dbob the dbob
	 * @return true, if successful
	 */
	abstract boolean insert(SLEXMMAbstractDatabaseObject dbob);
	
	/**
	 * Update.
	 *
	 * @param dbob the dbob
	 * @return true, if successful
	 */
	abstract boolean update(SLEXMMAbstractDatabaseObject dbob);
	
	/**
	 * Commit.
	 *
	 * @return true, if successful
	 */
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
