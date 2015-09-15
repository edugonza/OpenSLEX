package org.processmining.openslex;

import java.io.Serializable;

public class SLEXAttributeValue extends SLEXAbstractDatabaseObject implements Serializable {

	private int attributeId = -1;
	private int eventId = -1;
	private String value = null;
	
	protected SLEXAttributeValue(SLEXStorageCollection storage,int attributeId, int eventId) {
		super(storage);
		this.attributeId = attributeId;
		this.eventId = eventId;
	}
	
	public SLEXStorageCollection getStorage() {
		return (SLEXStorageCollection) super.storage;
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
	
	@Override
	boolean insert(SLEXAbstractDatabaseObject at) {
		return getStorage().insert((SLEXAttributeValue) at);
	}

	@Override
	boolean update(SLEXAbstractDatabaseObject at) {
		return getStorage().update((SLEXAttributeValue) at);
	}

}
