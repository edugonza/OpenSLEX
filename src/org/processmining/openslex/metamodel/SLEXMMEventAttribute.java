package org.processmining.openslex.metamodel;

public class SLEXMMEventAttribute extends SLEXMMAbstractDatabaseObject {

	private int id = -1;
	private String name = null;
	
	protected SLEXMMEventAttribute(SLEXMMStorageMetaModel storage) {
		super(storage);
	}
	
	public SLEXMMStorageMetaModel getStorage() {
		return (SLEXMMStorageMetaModel) super.storage;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	protected void setId(int id) {
		this.id = id;
	}
	
	protected void setName(String name) {
		this.name = name;
		setDirty(true);
	}
	
	@Override
	boolean insert(SLEXMMAbstractDatabaseObject at) {
		return getStorage().insert((SLEXMMEventAttribute) at);
	}

	@Override
	boolean update(SLEXMMAbstractDatabaseObject at) {
		return getStorage().update((SLEXMMEventAttribute) at);
	}

	@Override
	public String toString() {
		return getId()+":"+getName();
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			if (obj instanceof SLEXMMEventAttribute) {
				SLEXMMEventAttribute objat = (SLEXMMEventAttribute) obj;
				if (this.getId() == objat.getId() &&
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
