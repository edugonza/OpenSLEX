/*
 * 
 */
package org.processmining.openslex.metamodel;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMEventAttributeValue.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMEventAttributeValue extends SLEXMMAbstractDatabaseObject {

	/** The attribute id. */
	private int attributeId = -1;
	
	/** The event id. */
	private int eventId = -1;
	
	/** The value. */
	private String value = null;
	
	/** The type. */
	private String type = null;
	
	/**
	 * Instantiates a new SLEXMM event attribute value.
	 *
	 * @param storage the storage
	 * @param attributeId the attribute id
	 * @param eventId the event id
	 */
	protected SLEXMMEventAttributeValue(SLEXMMStorageMetaModel storage,int attributeId, int eventId) {
		super(storage);
		this.attributeId = attributeId;
		this.eventId = eventId;
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
	 * Gets the event id.
	 *
	 * @return the event id
	 */
	public int getEventId() {
		return this.eventId;
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
		return getStorage().insert((SLEXMMEventAttributeValue) at);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#update(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean update(SLEXMMAbstractDatabaseObject at) {
		return getStorage().update((SLEXMMEventAttributeValue) at);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return ("event_attribute_value#"+getId()).hashCode();
	}
}
