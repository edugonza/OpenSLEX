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
public class SLEXMMLogAttributeValue extends SLEXMMAbstractDatabaseObject {

	/** The attribute id. */
	private int attributeId = -1;
	
	/** The log id. */
	private int logId = -1;
	
	/** The value. */
	private String value = null;
	
	/** The type. */
	private String type = null;
	
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
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return this.value;
	}
	
	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	protected void setValue(String value) {
		this.value = value;
		setDirty(true);
	}
	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}
	
	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
		setDirty(true);
	}
	
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#insert(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean insert(SLEXMMAbstractDatabaseObject at) {
		return getStorage().insert((SLEXMMLogAttributeValue) at);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#update(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean update(SLEXMMAbstractDatabaseObject at) {
		return getStorage().update((SLEXMMLogAttributeValue) at);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return ("log_attribute_value#"+getId()).hashCode();
	}
}
