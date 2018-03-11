/*
 * 
 */
package org.processmining.openslex.metamodel;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMLogAttributeValue.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMLogAttributeValue extends AbstractDBElementWithValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5064580015677868912L;

	/** The attribute id. */
	private int attributeId = -1;
	
	/** The log id. */
	private int logId = -1;
	
	/**
	 * Instantiates a new SLEXMM log attribute value.
	 *
	 * @param storage the storage
	 * @param attributeId the attribute id
	 * @param logId the case id
	 */
	protected SLEXMMLogAttributeValue(SLEXMMStorageMetaModel storage,int attributeId, int logId) {
		super(storage);
		this.attributeId = attributeId;
		this.logId = logId;
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
	 * Gets the log id.
	 *
	 * @return the log id
	 */
	public int getLogId() {
		return this.logId;
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
		return getStorage().insert((SLEXMMLogAttributeValue) at);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#update(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean update(AbstractDBElement at) {
		return getStorage().update((SLEXMMLogAttributeValue) at);
	}
}
