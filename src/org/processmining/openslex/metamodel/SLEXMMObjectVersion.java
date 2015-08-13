package org.processmining.openslex.metamodel;

import java.util.HashMap;

public class SLEXMMObjectVersion extends SLEXMMAbstractDatabaseObject {

	private int id = -1;
	private int objectId = -1;
	private int eventId = -1;
	private HashMap<SLEXMMAttribute,SLEXMMAttributeValue> attributeValues = null;
	
	protected SLEXMMObjectVersion(SLEXMMStorageMetaModel storage) {
		super(storage);
	}
	
	public SLEXMMStorageMetaModel getStorage() {
		return (SLEXMMStorageMetaModel) super.storage;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getObjectId() {
		return this.objectId;
	}
	
	public int getEventId() {
		return this.eventId;
	}
	
	protected void setId(int id) {
		this.id = id;
	}
	
	protected void setObjectId(int id) {
		this.objectId = id;
		setDirty(true);
	}
	
	protected void setEventId(int id) {
		this.eventId = id;
		setDirty(true);
	}
	
	@Override
	boolean insert(SLEXMMAbstractDatabaseObject e) {
		return getStorage().insert((SLEXMMObjectVersion)e);
	}

	@Override
	boolean update(SLEXMMAbstractDatabaseObject e) {
		return getStorage().update((SLEXMMObjectVersion)e);
	}

	protected void retrieveAttributeValues() {
		attributeValues = getStorage().getAttributeValuesForObjectVersion(this);
	}
	
	public HashMap<SLEXMMAttribute, SLEXMMAttributeValue> getAttributeValues() {
		if (attributeValues == null) {
			retrieveAttributeValues();
		}
		return attributeValues;
	}

}
