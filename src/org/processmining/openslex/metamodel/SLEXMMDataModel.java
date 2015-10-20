package org.processmining.openslex.metamodel;


public class SLEXMMDataModel extends SLEXMMAbstractDatabaseObject {

	private int id = -1;
	private String name = null;
	
	public SLEXMMDataModel(SLEXMMStorage storage) {
		super(storage);
	}
	
	public SLEXMMStorageMetaModel getStorage() {
		return (SLEXMMStorageMetaModel) super.storage;
	}
	
	@Override
	boolean insert(SLEXMMAbstractDatabaseObject dm) {
		return getStorage().insert((SLEXMMDataModel) dm);
	}

	@Override
	boolean update(SLEXMMAbstractDatabaseObject dm) {
		return getStorage().update((SLEXMMDataModel) dm);
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

	@Override
	public int hashCode() {
		return ("datamodel#"+getId()).hashCode();
	}
}
