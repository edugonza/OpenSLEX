package org.processmining.openslex;

import java.util.Hashtable;

public class SLEXEvent extends SLEXAbstractDatabaseObject {

	private int id = -1;
	private int collectionId = -1;
	private Hashtable<SLEXAttribute, SLEXAttributeValue> attributeValues = null;
	
	protected SLEXEvent(SLEXStorageCollection storage) {
		super(storage);
	}
	
	public SLEXStorageCollection getStorage() {
		return (SLEXStorageCollection) super.storage;
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
	
	protected void setCollectionId(int id) {
		this.collectionId = id;
		setDirty(true);
	}
	
	@Override
	boolean insert(SLEXAbstractDatabaseObject e) {
		return getStorage().insert((SLEXEvent)e);
	}

	@Override
	boolean update(SLEXAbstractDatabaseObject e) {
		return getStorage().update((SLEXEvent)e);
	}

	protected void retrieveAttributeValues() {
		attributeValues = getStorage().getAttributeValuesForEvent(this);
	}
	
	public Hashtable<SLEXAttribute, SLEXAttributeValue> getAttributeValues() {
		if (attributeValues == null) {
			retrieveAttributeValues();
		}
		return attributeValues;
	}
	
	@Override
	public String toString() {
		return getCollectionId()+":"+getId();
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			if (obj instanceof SLEXEvent) {
				SLEXEvent objat = (SLEXEvent) obj;
				if (this.getId() == objat.getId() &&
						this.getCollectionId() == objat.getCollectionId()) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

}
