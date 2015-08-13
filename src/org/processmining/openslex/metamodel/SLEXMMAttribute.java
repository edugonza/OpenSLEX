package org.processmining.openslex.metamodel;

public class SLEXMMAttribute extends SLEXMMAbstractDatabaseObject {

	private int id = -1;
	private String name = null;
	private int classId = -1;
	
	protected SLEXMMAttribute(SLEXMMStorageMetaModel storage) {
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
	
	public String getName() {
		return this.name;
	}
	
	protected void setId(int id) {
		this.id = id;
	}
	
	protected void setClassId(int id) {
		this.classId = id;
		setDirty(true);
	}
	
	protected void setName(String name) {
		this.name = name;
		setDirty(true);
	}
	
	@Override
	boolean insert(SLEXMMAbstractDatabaseObject at) {
		return getStorage().insert((SLEXMMAttribute) at);
	}

	@Override
	boolean update(SLEXMMAbstractDatabaseObject at) {
		return getStorage().update((SLEXMMAttribute) at);
	}

	@Override
	public String toString() {
		return getClassId()+":"+getId()+":"+getName();
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			if (obj instanceof SLEXMMAttribute) {
				SLEXMMAttribute objat = (SLEXMMAttribute) obj;
				if (this.getId() == objat.getId() &&
						this.getClassId() == objat.getClassId() &&
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
