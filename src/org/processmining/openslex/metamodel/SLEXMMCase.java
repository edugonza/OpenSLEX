package org.processmining.openslex.metamodel;

public class SLEXMMCase extends SLEXMMAbstractDatabaseObject {

	private int id = -1;
	private String name = null;
	
	protected SLEXMMCase(SLEXMMStorageMetaModel storage) {
		super(storage);
	}

	public SLEXMMStorageMetaModel getStorage() {
		return (SLEXMMStorageMetaModel) super.storage;
	}
	
	public int getId() {
		return this.id;
	}
	
	protected void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
		setDirty(true);
	}
	
	public boolean add(SLEXMMActivityInstance ai) {
		return getStorage().addActivityInstanceToCase(this,ai);
	}
	
	public boolean add(int activityInstanceId) {
		return getStorage().addActivityInstanceToCase(this.getId(),activityInstanceId);
	}
	
	@Override
	boolean insert(SLEXMMAbstractDatabaseObject t) {
		return getStorage().insert((SLEXMMCase) t);
	}

	@Override
	boolean update(SLEXMMAbstractDatabaseObject t) {
		return getStorage().update((SLEXMMCase) t);
	}

//	public SLEXMMActivityInstanceResultSet getActivityInstanceResultSet() {
//		return getStorage().getActivityInstancesOfCase(this);
//	}
	
	public SLEXMMEventResultSet getEventResultSet() {
		return getStorage().getEventsOfCase(this);
	}
	
	@Override
	public int hashCode() {
		return ("case#"+getId()).hashCode();
	}
}
