/*
 * 
 */
package org.processmining.openslex.metamodel;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMCaseAttributeValue.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMCaseAttributeValue extends SLEXMMAbstractDatabaseObject {

	/** The attribute id. */
	private int attributeId = -1;
	
	/** The case id. */
	private int caseId = -1;
	
	/** The value. */
	private String value = null;
	
	/** The type. */
	private String type = null;
	
	/**
	 * Instantiates a new SLEXMM case attribute value.
	 *
	 * @param storage the storage
	 * @param attributeId the attribute id
	 * @param caseId the case id
	 */
	protected SLEXMMCaseAttributeValue(SLEXMMStorageMetaModel storage,int attributeId, int caseId) {
		super(storage);
		this.attributeId = attributeId;
		this.caseId = caseId;
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
	 * Gets the case id.
	 *
	 * @return the case id
	 */
	public int getCaseId() {
		return this.caseId;
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
		return getStorage().insert((SLEXMMCaseAttributeValue) at);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#update(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean update(SLEXMMAbstractDatabaseObject at) {
		return getStorage().update((SLEXMMCaseAttributeValue) at);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return ("case_attribute_value#"+getId()).hashCode();
	}
}
