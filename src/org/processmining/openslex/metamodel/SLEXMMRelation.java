package org.processmining.openslex.metamodel;

import java.util.HashMap;

public class SLEXMMRelation extends SLEXMMAbstractDatabaseObject {

	private int id = -1;
	private int relationshipId = -1;
	private int sourceObjectId = -1;
	private int targetObjectId = -1;
	private int startSourceObjectVersionId = -1;
	private int endSourceObjectVersionId = -1;
	private int startTargetObjectVersionId = -1;
	private int endTargetObjectVersionId = -1;
	private int eventId = -1;
	private HashMap<SLEXMMRelationshipAttribute,SLEXMMRelationshipAttributeValue> attributeValues = null;
	
	protected SLEXMMRelation(SLEXMMStorageMetaModel storage) {
		super(storage);
	}
	
	public SLEXMMStorageMetaModel getStorage() {
		return (SLEXMMStorageMetaModel) super.storage;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getRelationshipId() {
		return this.relationshipId;
	}
	
	protected void setRelationshipId(int id) {
		this.relationshipId = id;
		setDirty(true);
	}
	
	protected void setId(int id) {
		this.id = id;
	}
	
	public int getSourceObjectId() {
		return this.sourceObjectId;
	}
	
	protected void setSourceObjectId(int id) {
		this.sourceObjectId = id;
		setDirty(true);
	}
	
	public int getTargetObjectId() {
		return this.targetObjectId;
	}
	
	protected void setTargetObjectId(int id) {
		this.targetObjectId = id;
		setDirty(true);
	}
	
	public int getStartSourceObjectVersionId() {
		return this.startSourceObjectVersionId;
	}
	
	protected void setStartSourceObjectVersionId(int id) {
		this.startSourceObjectVersionId = id;
		setDirty(true);
	}
	
	public int getEndSourceObjectVersionId() {
		return this.endSourceObjectVersionId;
	}
	
	protected void setEndSourceObjectVersionId(int id) {
		this.endSourceObjectVersionId = id;
		setDirty(true);
	}
	
	public int getStartTargetObjectVersionId() {
		return this.startTargetObjectVersionId;
	}
	
	protected void setStartTargetObjectVersionId(int id) {
		this.startTargetObjectVersionId = id;
		setDirty(true);
	}
	
	public int getEndTargetObjectVersionId() {
		return this.endTargetObjectVersionId;
	}
	
	protected void setEndTargetObjectVersionId(int id) {
		this.endTargetObjectVersionId = id;
		setDirty(true);
	}
	
	public int getEventId() {
		return this.eventId;
	}
	
	protected void setEventId(int id) {
		this.eventId = id;
		setDirty(true);
	}
	
	@Override
	boolean insert(SLEXMMAbstractDatabaseObject e) {
		return getStorage().insert((SLEXMMRelation)e);
	}

	@Override
	boolean update(SLEXMMAbstractDatabaseObject e) {
		return getStorage().update((SLEXMMRelation)e);
	}

	protected void retrieveRelationAttributes() {
		attributeValues = getStorage().getRelationAttributes(this);
	}
	
	public HashMap<SLEXMMRelationshipAttribute,SLEXMMRelationshipAttributeValue> getRelationAttributes() {
		if (attributeValues == null) {
			retrieveRelationAttributes();
		}
		return attributeValues;
	}

}
