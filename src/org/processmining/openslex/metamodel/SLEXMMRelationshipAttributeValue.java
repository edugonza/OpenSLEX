package org.processmining.openslex.metamodel;

public class SLEXMMRelationshipAttributeValue extends SLEXMMAbstractDatabaseObject {

	private int id = -1;
	private int relationId = -1;
	private int relationshipAttributeId = -1;
	private String value = null;
	private String type = null;
	
	protected SLEXMMRelationshipAttributeValue(SLEXMMStorageMetaModel storage,int relationId, int relationshipAttributeId) {
		super(storage);
		this.relationId = relationId;
		this.relationshipAttributeId = relationshipAttributeId;
	}
	
	public SLEXMMStorageMetaModel getStorage() {
		return (SLEXMMStorageMetaModel) super.storage;
	}
	
	public int getId() {
		return this.id;
	}
	
	protected void setId(int id) {
		this.id = id;
	}
	
	public int getRelationId() {
		return this.relationId;
	}
	
	public int getRelationshipAttributeId() {
		return this.relationshipAttributeId;
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
		return getStorage().insert((SLEXMMRelationshipAttributeValue) at);
	}

	@Override
	boolean update(SLEXMMAbstractDatabaseObject at) {
		return getStorage().update((SLEXMMRelationshipAttributeValue) at);
	}

}
