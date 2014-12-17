package org.processmining.openslex;

public class SLEXTrace extends SLEXAbstractDatabaseObject {

	private int id = -1;
	private String caseId = null;
	private int perspectiveId = -1;
	
	protected SLEXTrace(SLEXStoragePerspective storage) {
		super(storage);
	}

	public SLEXStoragePerspective getStorage() {
		return (SLEXStoragePerspective) super.storage;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getCaseId() {
		return this.caseId;
	}
	
	public int getPerspectiveId() {
		return this.perspectiveId;
	}
	
	protected void setId(int id) {
		this.id = id;
	}
	
	public void setCaseId(String caseId) {
		this.caseId = caseId;
		setDirty(true);
	}
	
	protected void setPerspectiveId(int perspectiveId) {
		this.perspectiveId = perspectiveId;
		setDirty(true);
	}
	
	public boolean add(SLEXEvent e) {
		return getStorage().addEventToTrace(this,e);
	}
	
	@Override
	boolean insert(SLEXAbstractDatabaseObject t) {
		return getStorage().insert((SLEXTrace) t);
	}

	@Override
	boolean update(SLEXAbstractDatabaseObject t) {
		return getStorage().update((SLEXTrace) t);
	}

	public SLEXEventResultSet getEventsResultSet() {
		return getStorage().getEventsOfTrace(this);
	}

	public int getNumberEvents() {
		return getStorage().getNumberEventsOfTrace(this);
	}
		
}
