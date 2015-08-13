package org.processmining.openslex.metamodel;

public class SLEXMMRelationshipAttribute extends SLEXMMAbstractDatabaseObject {

	private int id = -1;
	private int relationshipId = -1;
	private int sourceAttributeId = -1;
	private int targetAttributeId = -1;
	
	protected SLEXMMRelationshipAttribute(SLEXMMStorageMetaModel storage, int relationshipId,
			int sourceAttributeId, int targetAttributeId) {
		super(storage);
		this.relationshipId = relationshipId;
		this.sourceAttributeId = sourceAttributeId;
		this.targetAttributeId = targetAttributeId;
	}
	
	public int getId() {
		return this.id;
	}
	
	public SLEXMMStorageMetaModel getStorage() {
		return (SLEXMMStorageMetaModel) super.storage;
	}
	
	protected void setId(int id) {
		this.id = id;
	}
	
	public int getRelationshipId() {
		return this.relationshipId;
	}
	
	public int getSourceAttributeId() {
		return this.sourceAttributeId;
	}
	
	public int getTargetAttributeId() {
		return this.targetAttributeId;
	}
		
	protected void setTargetAttributeId(int targetAttributeId) {
		this.targetAttributeId = targetAttributeId;
		setDirty(true);
	}
	
	@Override
	boolean insert(SLEXMMAbstractDatabaseObject at) {
		return getStorage().insert((SLEXMMRelationshipAttribute) at);
	}

	@Override
	boolean update(SLEXMMAbstractDatabaseObject at) {
		return getStorage().update((SLEXMMRelationshipAttribute) at);
	}

	@Override
	public String toString() {
		return getId()+":"+getRelationshipId()+":"+getSourceAttributeId()+":"+getTargetAttributeId();
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			if (obj instanceof SLEXMMRelationshipAttribute) {
				SLEXMMRelationshipAttribute objat = (SLEXMMRelationshipAttribute) obj;
				if (this.getId() == objat.getId() &&
						this.getRelationshipId() == objat.getRelationshipId() &&
						this.getSourceAttributeId() == objat.getSourceAttributeId() &&
						this.getTargetAttributeId() ==objat.getTargetAttributeId()) {
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
