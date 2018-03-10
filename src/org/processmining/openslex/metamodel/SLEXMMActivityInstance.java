/*
 * 
 */
package org.processmining.openslex.metamodel;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMActivityInstance.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMActivityInstance extends AbstractDBElement {

	/** The activity id. */
	private int activityId = -1;
	
	/**
	 * Instantiates a new SLEXMM activity instance.
	 *
	 * @param storage the storage
	 */
	protected SLEXMMActivityInstance(SLEXMMStorageMetaModel storage) {
		super(storage);
	}
	
	/**
	 * Gets the storage.
	 *
	 * @return the storage
	 */
	public SLEXMMStorageMetaModel getStorage() {
		return (SLEXMMStorageMetaModel) super.storage;
	}
	
	/**
	 * Gets the activity id.
	 *
	 * @return the activity id
	 */
	public int getActivityId() {
		return this.activityId;
	}
	
	/**
	 * Sets the activity id.
	 *
	 * @param id the new activity id
	 */
	protected void setActivityId(int id) {
		this.activityId = id;
		setDirty(true);
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#insert(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean insert(AbstractDBElement e) {
		return getStorage().insert((SLEXMMActivityInstance)e);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#update(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean update(AbstractDBElement e) {
		return getStorage().update((SLEXMMActivityInstance)e);
	}
	
//	public SLEXMMObjectVersionResultSet getEvents() {
//		return getStorage().getEventsForActivityInstanceOrdered(this);
//	}

	/* (non-Javadoc)
 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#equals(java.lang.Object)
 */
//@Override
//	public boolean equals(Object obj) {
//		if (super.equals(obj)) {
//			return true;
//		}
//		
//		if (obj instanceof SLEXMMCase) {
//			return ((SLEXMMCase) obj).getId() == getId();
//		}
//		
//		return false;
//	}
	
//	/* (non-Javadoc)
//	 * @see java.lang.Object#hashCode()
//	 */
//	@Override
//	public int hashCode() {
//		return ("activity#"+getId()).hashCode();
//	}
	
}
