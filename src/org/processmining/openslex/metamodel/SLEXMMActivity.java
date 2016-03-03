package org.processmining.openslex.metamodel;

public class SLEXMMActivity extends SLEXMMAbstractDatabaseObject {

	private String name = null;
	//private int processId = -1;
	
	protected SLEXMMActivity(SLEXMMStorageMetaModel storage, String name/*, int processId*/) {
		super(storage);
		this.name = name;
		//this.processId = processId;
	}
	
	public SLEXMMStorageMetaModel getStorage() {
		return (SLEXMMStorageMetaModel) super.storage;
	}
	
	public String getName() {
		return this.name;
	}
	
//	public int getProcessId() {
//		return this.processId;
//	}
	
	protected void setName(String name) {
		this.name = name;
		setDirty(true);
	}
	
//	protected void setProcessId(int processId) {
//		this.processId = processId;
//		setDirty(true);
//	}
	
	@Override
	boolean insert(SLEXMMAbstractDatabaseObject cl) {
		return getStorage().insert((SLEXMMActivity) cl);
	}

	@Override
	boolean update(SLEXMMAbstractDatabaseObject cl) {
		return getStorage().update((SLEXMMActivity) cl);
	}

	@Override
	public int hashCode() {
		return ("activity#"+getId()).hashCode();
	}
	
}
