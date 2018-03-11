/*
 * 
 */
package org.processmining.openslex.metamodel;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMObject.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMObject extends AbstractDBElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9063368192007827629L;
	/** The class id. */
	private int classId = -1;
	
	/**
	 * Instantiates a new SLEXMM object.
	 *
	 * @param storage the storage
	 */
	protected SLEXMMObject(SLEXMMStorageMetaModel storage) {
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
	 * Gets the class id.
	 *
	 * @return the class id
	 */
	public int getClassId() {
		return this.classId;
	}
	
	/**
	 * Sets the class id.
	 *
	 * @param id the new class id
	 */
	protected void setClassId(int id) {
		this.classId = id;
		setDirty(true);
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#insert(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean insert(AbstractDBElement e) {
		return getStorage().insert((SLEXMMObject)e);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#update(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean update(AbstractDBElement e) {
		return getStorage().update((SLEXMMObject)e);
	}
	
	/**
	 * Gets the object versions.
	 *
	 * @return the object versions
	 */
	public SLEXMMObjectVersionResultSet getObjectVersions() {
		return getStorage().getObjectVersionsForObject(this.getId());
	}

//	/* (non-Javadoc)
//	 * @see java.lang.Object#hashCode()
//	 */
//	@Override
//	public int hashCode() {
//		return ("object#"+getId()).hashCode();
//	}
}
