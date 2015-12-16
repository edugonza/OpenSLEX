package org.processmining.openslex.metamodel;

public class SLEXMMEventAttributeValue extends SLEXMMAbstractDatabaseObject {

	private int attributeId = -1;
	private int eventId = -1;
	private String value = null;
	private String type = null;
	
	protected SLEXMMEventAttributeValue(SLEXMMStorageMetaModel storage,int attributeId, int eventId) {
		super(storage);
		this.attributeId = attributeId;
		this.eventId = eventId;
	}
	
	public SLEXMMStorageMetaModel getStorage() {
		return (SLEXMMStorageMetaModel) super.storage;
	}
	
	public int getEventId() {
		return this.eventId;
	}
	
	public int getAttributeId() {
		return this.attributeId;
	}
	
	public String getValue() {
		return this.value;
	}
	
	protected void setValue(String value) {
		this.value = value;
		setDirty(true);
	}
	
	public String getType() {
		return this.type;
	}
	
	public void setType(String type) {
		this.type = type;
		setDirty(true);
	}
	
	
	@Override
	boolean insert(SLEXMMAbstractDatabaseObject at) {
		return getStorage().insert((SLEXMMEventAttributeValue) at);
	}

	@Override
	boolean update(SLEXMMAbstractDatabaseObject at) {
		return getStorage().update((SLEXMMEventAttributeValue) at);
	}

	@Override
	public int hashCode() {
		return ("event_attribute_value#"+getId()).hashCode();
	}
}
