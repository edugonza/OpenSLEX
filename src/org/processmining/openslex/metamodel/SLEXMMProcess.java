package org.processmining.openslex.metamodel;


public class SLEXMMProcess extends SLEXMMAbstractDatabaseObject {

	private int id = -1;
	private String name = null;
	
	public SLEXMMProcess(SLEXMMStorage storage) {
		super(storage);
	}
	
	public SLEXMMStorageMetaModel getStorage() {
		return (SLEXMMStorageMetaModel) super.storage;
	}
	
	@Override
	boolean insert(SLEXMMAbstractDatabaseObject dm) {
		return getStorage().insert((SLEXMMProcess) dm);
	}

	@Override
	boolean update(SLEXMMAbstractDatabaseObject dm) {
		return getStorage().update((SLEXMMProcess) dm);
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
