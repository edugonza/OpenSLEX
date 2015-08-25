package org.processmining.openslex.metamodel;

public class SLEXMMActivityInstance extends SLEXMMAbstractDatabaseObject {

	private int id = -1;
	private int activityId = -1;
	
	protected SLEXMMActivityInstance(SLEXMMStorageMetaModel storage) {
		super(storage);
	}
	
	public SLEXMMStorageMetaModel getStorage() {
		return (SLEXMMStorageMetaModel) super.storage;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getActivityId() {
		return this.activityId;
	}
	
	protected void setId(int id) {
		this.id = id;
	}
	
	protected void setActivityId(int id) {
		this.activityId = id;
		setDirty(true);
	}
	
	@Override
	boolean insert(SLEXMMAbstractDatabaseObject e) {
		return getStorage().insert((SLEXMMActivityInstance)e);
	}

	@Override
	boolean update(SLEXMMAbstractDatabaseObject e) {
		return getStorage().update((SLEXMMActivityInstance)e);
	}
	
//	public SLEXMMObjectVersionResultSet getEvents() {
//		return getStorage().getEventsForActivityInstanceOrdered(this);
//	}

}