package org.processmining.openslex.metamodel;

import java.util.HashMap;

public class SLEXMMObjectVersion extends SLEXMMAbstractDatabaseObject {

	private int objectId = -1;
	private long startTimestamp = -1;
	private long endTimestamp = -1;
	private HashMap<SLEXMMAttribute,SLEXMMAttributeValue> attributeValues = null;
	
	protected SLEXMMObjectVersion(SLEXMMStorageMetaModel storage) {
		super(storage);
	}
	
	public SLEXMMStorageMetaModel getStorage() {
		return (SLEXMMStorageMetaModel) super.storage;
	}
	
	public int getObjectId() {
		return this.objectId;
	}
		
	public long getStartTimestamp() {
		return this.startTimestamp;
	}
	
	public long getEndTimestamp() {
		return this.endTimestamp;
	}
	
	protected void setObjectId(int id) {
		this.objectId = id;
		setDirty(true);
	}
	
	protected void setStartTimestamp(long timestamp) {
		this.startTimestamp = timestamp;
		setDirty(true);
	}
	
	protected void setEndTimestamp(long timestamp) {
		this.endTimestamp = timestamp;
		setDirty(true);
	}
		
	public boolean add(SLEXMMEvent ev, String label) {
		return getStorage().addEventToObjectVersion(this,ev,label);
	}
	
	public boolean add(int eventId, String label) {
		return getStorage().addEventToObjectVersion(this.getId(),eventId,label);
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

	@Override
	public int hashCode() {
		return ("object_version#"+getId()).hashCode();
	}
}
