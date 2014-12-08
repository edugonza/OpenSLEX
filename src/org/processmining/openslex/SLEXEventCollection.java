package org.processmining.openslex;

import java.util.List;

public class SLEXEventCollection extends SLEXAbstractDatabaseObject {
	
	private int id = -1;
	private String name = null;
	
	protected SLEXEventCollection(SLEXStorage storage, String name) {
		super(storage);
		this.name = name;
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
		return storage.insert((SLEXEventCollection) ec);
	}

	@Override
	boolean update(SLEXAbstractDatabaseObject ec) {
		return storage.update((SLEXEventCollection) ec);
	}
	
	public SLEXEventResultSet getEventsResultSet() {
		return storage.getEventsOfCollection(this);
	}
	
	public SLEXEventResultSet getEventsResultSetOrderedBy(List<SLEXAttribute> orderAttributes) {
		return storage.getEventsOfCollectionOrderedBy(this,orderAttributes);
	}
	
	public SLEXEventResultSet getEventsResultSetBetweenDatesOrderedBy(
			List<SLEXAttribute> orderAtts, String startDate, String endDate) {
		return storage.getEventsOfCollectionBetweenDatesOrderedBy(this,orderAtts,startDate,endDate);
	}
	
	@Override
	public String toString() {
		return getId()+"";
	}

}
