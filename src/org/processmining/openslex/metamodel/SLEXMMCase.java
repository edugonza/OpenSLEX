package org.processmining.openslex.metamodel;

public class SLEXMMCase extends SLEXMMAbstractDatabaseObject {

	private int id = -1;
	private int logId = -1;
	
	protected SLEXMMCase(SLEXMMStorageMetaModel storage) {
		super(storage);
	}

	public SLEXMMStorageMetaModel getStorage() {
		return (SLEXMMStorageMetaModel) super.storage;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getLogId() {
		return this.logId;
	}
	
	protected void setId(int id) {
		this.id = id;
	}
	
	protected void setLogId(int logId) {
		this.logId = logId;
		setDirty(true);
	}
	
	public boolean add(SLEXMMEvent e) {
		return getStorage().addEventToCase(this,e);
	}
	
	@Override
	boolean insert(SLEXMMAbstractDatabaseObject t) {
		return getStorage().insert((SLEXMMCase) t);
	}

	@Override
	boolean update(SLEXMMAbstractDatabaseObject t) {
		return getStorage().update((SLEXMMCase) t);
	}

	public SLEXMMEventResultSet getEventsResultSet() {
		return getStorage().getEventsOfCase(this);
	}

	public int getNumberEvents() {
		return getStorage().getNumberEventsOfCase(this);
	}
		
}
