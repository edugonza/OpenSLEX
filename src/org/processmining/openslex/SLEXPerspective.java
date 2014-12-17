package org.processmining.openslex;

public class SLEXPerspective extends SLEXAbstractDatabaseObject {

	private int id = -1;
	private int collectionId = -1;
	private String name = null;
	private String collectionFileName = null;
	
	protected SLEXPerspective(SLEXStoragePerspective storage) {
		super(storage);
	}

	public SLEXStoragePerspective getStorage() {
		return (SLEXStoragePerspective) super.storage;
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
	
	public void setCollectionFileName(String filename) {
		this.collectionFileName = filename;
	}
	
	public String getCollectionFileName() {
		return this.collectionFileName;
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
	
	public SLEXTraceResultSet getTracesResultSet() {
		return getStorage().getTracesOfPerspective(this);
	}
	
	@Override
	boolean insert(SLEXAbstractDatabaseObject p) {
		return getStorage().insert((SLEXPerspective) p);
	}

	@Override
	boolean update(SLEXAbstractDatabaseObject p) {
		return getStorage().update((SLEXPerspective) p);
	}

	public boolean remove(SLEXTrace t) {
		return getStorage().removeTraceFromPerspective(this,t);
	}

	@Override
	public String toString() {
		return String.valueOf(this.getId());
	}
}
