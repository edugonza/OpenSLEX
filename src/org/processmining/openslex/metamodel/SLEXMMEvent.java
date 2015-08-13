package org.processmining.openslex.metamodel;

import java.util.HashMap;

public class SLEXMMEvent extends SLEXMMAbstractDatabaseObject {

	private int id = -1;
	private int collectionId = -1;
	private int order = -1;
	private HashMap<SLEXMMEventAttribute, SLEXMMEventAttributeValue> attributeValues = null;
	
	protected SLEXMMEvent(SLEXMMStorageMetaModel storage) {
		super(storage);
	}
	
	public SLEXMMStorageMetaModel getStorage() {
		return (SLEXMMStorageMetaModel) super.storage;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getCollectionId() {
		return this.collectionId;
	}
	
	protected void setId(int id) {
		this.id = id;
	}
	
	public void setOrder(int order) {
		this.order = order;
		setDirty(true);
	}
	
	protected void setCollectionId(int id) {
		this.collectionId = id;
		setDirty(true);
	}
	
	public int getOrder() {
		return this.order;
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
