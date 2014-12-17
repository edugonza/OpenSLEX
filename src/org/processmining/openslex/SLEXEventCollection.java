package org.processmining.openslex;

import java.util.List;

public class SLEXEventCollection extends SLEXAbstractDatabaseObject {
	
	private int id = -1;
	private String name = null;
	
	protected SLEXEventCollection(SLEXStorageCollection storage, String name) {
		super(storage);
		this.name = name;
	}
	
	public SLEXStorageCollection getStorage() {
		return (SLEXStorageCollection) super.storage;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setName(String name) {
		this.name = name;
		setDirty(true);
	}
	
	protected void setId(int id) {
		this.id = id;
	}

	public boolean add(SLEXEvent event) {
		event.setCollectionId(this.id);
		return event.commit();
	}
	
	@Override
	boolean insert(SLEXAbstractDatabaseObject ec) {
		return getStorage().insert((SLEXEventCollection) ec);
	}

	@Override
	boolean update(SLEXAbstractDatabaseObject ec) {
		return getStorage().update((SLEXEventCollection) ec);
	}
	
	public SLEXEventResultSet getEventsResultSet() {
		return getStorage().getEventsOfCollection(this);
	}
	
	public SLEXEventResultSet getEventsResultSetOrderedBy(List<SLEXAttribute> orderAttributes) {
		return getStorage().getEventsOfCollectionOrderedBy(this,orderAttributes);
	}
	
	public SLEXEventResultSet getEventsResultSetBetweenDatesOrderedBy(
			List<SLEXAttribute> orderAtts, String startDate, String endDate) {
		return getStorage().getEventsOfCollectionBetweenDatesOrderedBy(this,orderAtts,startDate,endDate);
	}
	
	@Override
	public String toString() {
		return getId()+"";
	}

}
