package org.processmining.openslex.metamodel;

public class SLEXMMRelationship extends SLEXMMAbstractDatabaseObject {

	private String name = null;
	private int sourceClassId = -1;
	private int targetClassId = -1;
	
	protected SLEXMMRelationship(SLEXMMStorageMetaModel storage) {
		super(storage);
	}
	
	public SLEXMMStorageMetaModel getStorage() {
		return (SLEXMMStorageMetaModel) super.storage;
	}
	
	public int getSourceClassId() {
		return this.sourceClassId;
	}
	
	public int getTargetClassId() {
		return this.targetClassId;
	}
	
	public String getName() {
		return this.name;
	}
	
	protected void setSourceClassId(int id) {
		this.sourceClassId = id;
		setDirty(true);
	}
	
	protected void setTargetClassId(int id) {
		this.targetClassId = id;
		setDirty(true);
	}
	
	protected void setName(String name) {
		this.name = name;
		setDirty(true);
	}
		
	@Override
	boolean insert(SLEXMMAbstractDatabaseObject at) {
		return getStorage().insert((SLEXMMRelationship) at);
	}

	@Override
	boolean update(SLEXMMAbstractDatabaseObject at) {
		return getStorage().update((SLEXMMRelationship) at);
	}

	@Override
	public int hashCode() {
		return ("relationship#"+getId()).hashCode();
	}
}
