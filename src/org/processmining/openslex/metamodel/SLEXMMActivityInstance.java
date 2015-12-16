package org.processmining.openslex.metamodel;

public class SLEXMMActivityInstance extends SLEXMMAbstractDatabaseObject {

	private int activityId = -1;
	
	protected SLEXMMActivityInstance(SLEXMMStorageMetaModel storage) {
		super(storage);
	}
	
	public SLEXMMStorageMetaModel getStorage() {
		return (SLEXMMStorageMetaModel) super.storage;
	}
	
	public int getActivityId() {
		return this.activityId;
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

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			return true;
		}
		
		if (obj instanceof SLEXMMCase) {
			return ((SLEXMMCase) obj).getId() == getId();
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return ("activity#"+getId()).hashCode();
	}
	
}
