package org.processmining.openslex.metamodel;

public class SLEXMMRelationship extends SLEXMMAbstractDatabaseObject {

	private int id = -1;
	private String name = null;
	private int sourceClassId = -1;
	private int targetClassId = -1;
	
	protected SLEXMMRelationship(SLEXMMStorageMetaModel storage) {
		super(storage);
	}
	
	public SLEXMMStorageMetaModel getStorage() {
		return (SLEXMMStorageMetaModel) super.storage;
	}
	
	public int getId() {
		return this.id;
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
	
	protected void setId(int id) {
		this.id = id;
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
	public String toString() {
		return getSourceClassId()+":"+getTargetClassId()+":"+getId()+":"+getName();
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			if (obj instanceof SLEXMMRelationship) {
				SLEXMMRelationship objat = (SLEXMMRelationship) obj;
				if (this.getId() == objat.getId() &&
						this.getSourceClassId() == objat.getSourceClassId() &&
						this.getTargetClassId() == objat.getTargetClassId() &&
						this.getName().equals(objat.getName())) {
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
