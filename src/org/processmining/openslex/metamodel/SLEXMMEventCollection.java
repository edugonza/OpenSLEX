package org.processmining.openslex.metamodel;

import java.util.List;

public class SLEXMMEventCollection extends SLEXMMAbstractDatabaseObject {
	
	private int id = -1;
	private String name = null;
	
	protected SLEXMMEventCollection(SLEXMMStorageMetaModel storage, String name) {
		super(storage);
		this.name = name;
	}
	
	public SLEXMMStorageMetaModel getStorage() {
		return (SLEXMMStorageMetaModel) super.storage;
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

	public boolean add(SLEXMMEvent event) {
		event.setCollectionId(this.id);
		return event.commit();
	}
	
	@Override
	boolean insert(SLEXMMAbstractDatabaseObject ec) {
		return getStorage().insert((SLEXMMEventCollection) ec);
	}

	@Override
	boolean update(SLEXMMAbstractDatabaseObject ec) {
		return getStorage().update((SLEXMMEventCollection) ec);
	}
	
	public SLEXMMEventResultSet getEventsResultSet() {
		return getStorage().getEventsOfCollection(this);
	}
	
	public SLEXMMEventResultSet getEventsResultSetOrderedBy(List<SLEXMMEventAttribute> orderAttributes) {
		return getStorage().getEventsOfCollectionOrderedBy(this,orderAttributes);
	}
	
	public SLEXMMEventResultSet getEventsResultSetBetweenDatesOrderedBy(
			List<SLEXMMEventAttribute> orderAtts, SLEXMMEventAttribute timestampAttr, String startDate, String endDate) {
		return getStorage().getEventsOfCollectionBetweenDatesOrderedBy(this,orderAtts,timestampAttr,startDate,endDate);
	}
	
	@Override
	public String toString() {
		return getId()+"";
	}

}
