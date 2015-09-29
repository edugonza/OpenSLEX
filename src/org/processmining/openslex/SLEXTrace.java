package org.processmining.openslex;

import java.io.Serializable;

public class SLEXTrace extends SLEXAbstractDatabaseObject implements Serializable {

	private int id = -1;
	private String caseId = null;
	private int perspectiveId = -1;
	
	public SLEXTrace() {
		super(null);
	}
	
	protected SLEXTrace(SLEXStoragePerspective storage) {
		super(storage);
	}

	@Override
	public int hashCode() {
		String strHash = id+"#"+caseId+"#"+perspectiveId;
		return strHash.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SLEXTrace) {
			SLEXTrace oT = (SLEXTrace) obj;
			if (this.id == oT.id) {
				if (this.caseId.equals(oT.caseId)) {
					if (this.perspectiveId == oT.perspectiveId) {
						return true;
					}
				}
			}
		}
		
		return false;
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
