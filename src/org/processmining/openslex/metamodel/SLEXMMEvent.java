package org.processmining.openslex.metamodel;

import java.util.HashMap;

public class SLEXMMEvent extends SLEXMMAbstractDatabaseObject {

	private int id = -1;
	private int activityInstanceId = -1;
	private int order = -1;
	private HashMap<SLEXMMEventAttribute, SLEXMMEventAttributeValue> attributeValues = null;
	private String lifecycle = null;
	private long timestamp = -1;
	private String resource = null;
	
	protected SLEXMMEvent(SLEXMMStorageMetaModel storage) {
		super(storage);
	}
	
	public SLEXMMStorageMetaModel getStorage() {
		return (SLEXMMStorageMetaModel) super.storage;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getActivityInstanceId() {
		return this.activityInstanceId;
	}
	
	protected void setId(int id) {
		this.id = id;
	}
	
	public void setOrder(int order) {
		this.order = order;
		setDirty(true);
	}
	
	public void setActivityInstanceId(int id) {
		this.activityInstanceId = id;
		setDirty(true);
	}
	
	public int getOrder() {
		return this.order;
	}
	
	public String getLifecycle() {
		return this.lifecycle;
	}
	
	public String getResource() {
		return this.resource;
	}
	
	public long getTimestamp() {
		return this.timestamp;
	}
	
	protected void setLifecycle(String lifecycle) {
		this.lifecycle = lifecycle;
		setDirty(true);
	}
	
	protected void setResource(String resource) {
		this.resource = resource;
		setDirty(true);
	}
	
	protected void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
		setDirty(true);
	}
	
	@Override
	boolean insert(SLEXMMAbstractDatabaseObject e) {
		return getStorage().insert((SLEXMMEvent)e);
	}

	@Override
	boolean update(SLEXMMAbstractDatabaseObject e) {
		return getStorage().update((SLEXMMEvent)e);
	}

	protected void retrieveAttributeValues() {
		attributeValues = getStorage().getAttributeValuesForEvent(this);
	}
	
	public HashMap<SLEXMMEventAttribute, SLEXMMEventAttributeValue> getAttributeValues() {
		if (attributeValues == null) {
			retrieveAttributeValues();
		}
		return attributeValues;
	}

}
