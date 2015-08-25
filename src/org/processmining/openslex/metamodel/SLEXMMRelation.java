package org.processmining.openslex.metamodel;

public class SLEXMMRelation extends SLEXMMAbstractDatabaseObject {

	private int id = -1;
	private int sourceObjectVersionId = -1;
	private int targetObjectVersionId = -1;
	
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
	
	@Override
	boolean insert(SLEXMMAbstractDatabaseObject e) {
		return getStorage().insert((SLEXMMRelation)e);
	}

	@Override
	boolean update(SLEXMMAbstractDatabaseObject e) {
		return getStorage().update((SLEXMMRelation)e);
	}

}
