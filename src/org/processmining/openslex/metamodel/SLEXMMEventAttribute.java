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
	public int hashCode() {
		return ("event_attribute#"+getId()).hashCode();
	}
}
