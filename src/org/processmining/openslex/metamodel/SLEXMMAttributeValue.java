/*
 * 
 */
package org.processmining.openslex.metamodel;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMAttributeValue.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMAttributeValue extends AbstractDBElementWithValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6952890727095161997L;

	/** The attribute id. */
	private int attributeId = -1;
	
	/** The object version id. */
	private int objectVersionId = -1;
	
	/**
	 * Instantiates a new SLEXMM attribute value.
	 *
	 * @param storage the storage
	 * @param attributeId the attribute id
	 * @param objectVersionId the object version id
	 */
	protected SLEXMMAttributeValue(SLEXMMStorageMetaModel storage,int attributeId, int objectVersionId) {
		super(storage);
		this.attributeId = attributeId;
		this.objectVersionId = objectVersionId;
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
	 * Gets the object version id.
	 *
	 * @return the object version id
	 */
	public int getObjectVersionId() {
		return this.objectVersionId;
	}
	
	/**
	 * Gets the attribute id.
	 *
	 * @return the attribute id
	 */
	public int getAttributeId() {
		return this.attributeId;
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#insert(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean insert(AbstractDBElement at) {
		return getStorage().insert((SLEXMMAttributeValue) at);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#update(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean update(AbstractDBElement at) {
		return getStorage().update((SLEXMMAttributeValue) at);
	}
	
}
