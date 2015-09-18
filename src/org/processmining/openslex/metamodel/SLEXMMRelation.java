package org.processmining.openslex.metamodel;

public class SLEXMMRelation extends SLEXMMAbstractDatabaseObject {

	private int id = -1;
	private int sourceObjectVersionId = -1;
	private int targetObjectVersionId = -1;
	private int relationshipId = -1;
	private long startTimestamp = -1;
	private long endTimestamp = -1;
	
	protected SLEXMMRelation(SLEXMMStorageMetaModel storage) {
		super(storage);
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
	
	public int getSourceObjectVersionId() {
		return this.sourceObjectVersionId;
	}
	
	protected void setSourceObjectVersionId(int id) {
		this.sourceObjectVersionId = id;
		setDirty(true);
	}
	
	public int getTargetObjectVersionId() {
		return this.targetObjectVersionId;
	}
	
	protected void setTargetObjectVersionId(int id) {
		this.targetObjectVersionId = id;
		setDirty(true);
	}
	
	public int getRelationshipId() {
		return this.relationshipId;
	}
	
	protected void setRelationshipId(int id) {
		this.relationshipId = id;
		setDirty(true);
	}
	
	public long getStartTimestamp() {
		return this.startTimestamp;
	}
	
	public long getEndTimestamp() {
		return this.endTimestamp;
	}
	
	protected void setStartTimestamp(long timestamp) {
		this.startTimestamp = timestamp;
		setDirty(true);
	}
	
	protected void setEndTimestamp(long timestamp) {
		this.endTimestamp = timestamp;
		setDirty(true);
	}
	
	@Override
	boolean insert(SLEXMMAbstractDatabaseObject e) {
		return getStorage().insert((SLEXMMRelation)e);
	}

	@Override
	boolean update(SLEXMMAbstractDatabaseObject e) {
		return getStorage().update((SLEXMMRelation)e);
	}

}
