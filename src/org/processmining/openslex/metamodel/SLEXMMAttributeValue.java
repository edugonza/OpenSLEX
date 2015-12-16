package org.processmining.openslex.metamodel;

public class SLEXMMAttributeValue extends SLEXMMAbstractDatabaseObject {

	private int attributeId = -1;
	private int objectVersionId = -1;
	private String value = null;
	private String type = null;
	
	protected SLEXMMAttributeValue(SLEXMMStorageMetaModel storage,int attributeId, int objectVersionId) {
		super(storage);
		this.attributeId = attributeId;
		this.objectVersionId = objectVersionId;
	}
	
	public SLEXMMStorageMetaModel getStorage() {
		return (SLEXMMStorageMetaModel) super.storage;
	}
	
	public int getObjectVersionId() {
		return this.objectVersionId;
	}
	
	public int getAttributeId() {
		return this.attributeId;
	}
	
	public String getValue() {
		return this.value;
	}
	
	protected void setValue(String value) {
		this.value = value;
		setDirty(true);
	}
	
	public String getType() {
		return this.type;
	}
	
	public void setType(String type) {
		this.type = type;
		setDirty(true);
	}
	
	@Override
	boolean insert(SLEXMMAbstractDatabaseObject at) {
		return getStorage().insert((SLEXMMAttributeValue) at);
	}

	@Override
	boolean update(SLEXMMAbstractDatabaseObject at) {
		return getStorage().update((SLEXMMAttributeValue) at);
	}
	
	@Override
	public int hashCode() {
		return ("attribute_value#"+getId()).hashCode();
	}

}
