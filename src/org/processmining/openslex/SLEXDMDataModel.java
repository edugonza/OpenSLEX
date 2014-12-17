package org.processmining.openslex;


public class SLEXDMDataModel extends SLEXAbstractDatabaseObject {

	private int id = -1;
	private String name = null;
	
	public SLEXDMDataModel(SLEXStorage storage) {
		super(storage);
	}
	
	public SLEXStorageDataModel getStorage() {
		return (SLEXStorageDataModel) super.storage;
	}
	
	@Override
	boolean insert(SLEXAbstractDatabaseObject dm) {
		return getStorage().insert((SLEXDMDataModel) dm);
	}

	@Override
	boolean update(SLEXAbstractDatabaseObject dm) {
		return getStorage().update((SLEXDMDataModel) dm);
	}

	public int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
		setDirty(true);
	}

}
