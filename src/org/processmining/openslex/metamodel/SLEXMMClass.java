package org.processmining.openslex.metamodel;

import java.util.List;

public class SLEXMMClass extends SLEXMMAbstractDatabaseObject {

	private int id = -1;
	private String name = null;
	private int datamodelId = -1;
	private List<SLEXMMAttribute> attributes = null;
	private List<SLEXMMRelationship> keys = null;
	
	protected SLEXMMClass(SLEXMMStorageMetaModel storage, String name, int data_modelId) {
		super(storage);
		this.name = name;
		this.datamodelId = data_modelId;
	}
	
	public SLEXMMStorageMetaModel getStorage() {
		return (SLEXMMStorageMetaModel) super.storage;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getDataModelId() {
		return this.datamodelId;
	}
	
	protected void setName(String name) {
		this.name = name;
		setDirty(true);
	}
	
	protected void setId(int id) {
		this.id = id;
	}
	
	protected void setDataModelId(int datamodelId) {
		this.datamodelId = datamodelId;
		setDirty(true);
	}
	
	protected void retrieveAttributesAndKeys() {
		getAttributes();
		getKeys();
	}
	
	protected void retrieveAttributes() {		
		this.attributes = getStorage().getAttributesForClass(this);
	}
	
	protected void retrieveRelationships() {
		this.keys = getStorage().getRelationshipsForClass(this);
	}
	
	public List<SLEXMMRelationship> getKeys() {
		if (this.keys == null) {
			retrieveRelationships();
		}
		
		return this.keys;
	} 
	
	public List<SLEXMMAttribute> getAttributes() {
		if (this.attributes == null) {
			retrieveAttributes();
		}
		
		return this.attributes;
	}
	
	@Override
	boolean insert(SLEXMMAbstractDatabaseObject cl) {
		return getStorage().insert((SLEXMMClass) cl);
	}

	@Override
	boolean update(SLEXMMAbstractDatabaseObject cl) {
		return getStorage().update((SLEXMMClass) cl);
	}

}
