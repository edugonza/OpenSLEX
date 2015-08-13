package org.processmining.openslex.metamodel;

public class SLEXMMEventAttribute extends SLEXMMAbstractDatabaseObject {

	private int id = -1;
	private String name = null;
	private int collectionId = -1;
	
	protected SLEXMMEventAttribute(SLEXMMStorageMetaModel storage) {
		super(storage);
	}
	
	public SLEXMMStorageMetaModel getStorage() {
		return (SLEXMMStorageMetaModel) super.storage;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getCollectionId() {
		return this.collectionId;
	}
	
	public String getName() {
		return this.name;
	}
	
	protected void setId(int id) {
		this.id = id;
	}
	
	protected void setCollectionId(int id) {
		this.collectionId = id;
		setDirty(true);
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
		return getCollectionId()+":"+getId()+":"+getName();
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
						this.getCollectionId() == objat.getCollectionId() &&
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
