package org.processmining.openslex.metamodel;

public class SLEXMMObject extends SLEXMMAbstractDatabaseObject {

	private int id = -1;
	private int classId = -1;
	
	protected SLEXMMObject(SLEXMMStorageMetaModel storage) {
		super(storage);
	}
	
	public SLEXMMStorageMetaModel getStorage() {
		return (SLEXMMStorageMetaModel) super.storage;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getClassId() {
		return this.classId;
	}
	
	protected void setId(int id) {
		this.id = id;
	}
	
	protected void setClassId(int id) {
		this.classId = id;
		setDirty(true);
	}
	
	@Override
	boolean insert(SLEXMMAbstractDatabaseObject e) {
		return getStorage().insert((SLEXMMObject)e);
	}

	@Override
	boolean update(SLEXMMAbstractDatabaseObject e) {
		return getStorage().update((SLEXMMObject)e);
	}
	
	public SLEXMMObjectVersionResultSet getObjectVersions() {
		return getStorage().getObjectVersionsForObjectOrdered(this);
	}

	@Override
	public int hashCode() {
		return ("object#"+getId()).hashCode();
	}
}
