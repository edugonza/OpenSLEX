package org.processmining.openslex.metamodel;

public class SLEXMMLog extends SLEXMMAbstractDatabaseObject {

	private int id = -1;
	private int collectionId = -1;
	private String name = null;
	private int processId = -1;
	
	protected SLEXMMLog(SLEXMMStorageMetaModel storage) {
		super(storage);
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
	
	public int getCollectionId() {
		return this.collectionId;
	}
	
	protected void setId(int id) {
		this.id = id;
	}
	
	protected void setCollectionId(int collectionId) {
		this.collectionId = collectionId;
		setDirty(true);
	}
	
	protected void setName(String name) {
		this.name = name;
		setDirty(true);
	}
	
	public int getProcessId() {
		return this.processId;
	}
	
	protected void setProcessId(int processId) {
		this.processId = processId;
		setDirty(true);
	}
	
	public SLEXMMCaseResultSet getTracesResultSet() {
		return getStorage().getCasesOfLog(this);
	}
	
	@Override
	boolean insert(SLEXMMAbstractDatabaseObject p) {
		return getStorage().insert((SLEXMMLog) p);
	}

	@Override
	boolean update(SLEXMMAbstractDatabaseObject p) {
		return getStorage().update((SLEXMMLog) p);
	}

	public boolean remove(SLEXMMCase t) {
		return getStorage().removeCaseFromLog(this,t);
	}

	@Override
	public String toString() {
		return String.valueOf(this.getId());
	}
}
