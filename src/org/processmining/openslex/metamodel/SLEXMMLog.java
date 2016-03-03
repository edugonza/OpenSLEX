package org.processmining.openslex.metamodel;

public class SLEXMMLog extends SLEXMMAbstractDatabaseObject {

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
	
	public void setName(String name) {
		this.name = name;
		setDirty(true);
	}
	
	public boolean add(SLEXMMCase cs) {
		return getStorage().addCaseToLog(this.getId(),cs.getId());
	}
	
	public boolean add(int caseId) {
		return getStorage().addCaseToLog(this.getId(),caseId);
	}
	
	public int getProcessId() {
		return this.processId;
	}
	
	protected void setProcessId(int processId) {
		this.processId = processId;
		setDirty(true);
	}
	
	@Override
	boolean insert(SLEXMMAbstractDatabaseObject t) {
		return getStorage().insert((SLEXMMLog) t);
	}

	@Override
	boolean update(SLEXMMAbstractDatabaseObject t) {
		return getStorage().update((SLEXMMLog) t);
	}
	
	public SLEXMMCaseResultSet getCaseResultSet() {
		return getStorage().getCasesForLog(this.getId());
	}
	
	@Override
	public int hashCode() {
		return ("log#"+getId()).hashCode();
	}
}
