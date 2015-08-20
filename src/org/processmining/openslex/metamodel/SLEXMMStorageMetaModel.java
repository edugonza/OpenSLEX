package org.processmining.openslex.metamodel;

import java.util.HashMap;
import java.util.List;

public interface SLEXMMStorageMetaModel extends SLEXMMStorage {

	public abstract SLEXMMEventCollection createEventCollection(String name);
	
	public abstract SLEXMMEvent createEvent(int collectionId, int order);
	
	public abstract SLEXMMEventAttribute createEventAttribute(int collectionId, String name);

	public abstract SLEXMMEventAttributeValue createEventAttributeValue(int attributeId,
			int eventId, String value, String type);

	public abstract SLEXMMAttributeValue createAttributeValue(int attributeId,
			int objectVersionId, String value, String type);

	public abstract SLEXMMEventCollectionResultSet getEventCollections();

	public abstract SLEXMMEventResultSet getEventsOfCollectionBetweenDatesOrderedBy(
			SLEXMMEventCollection ec, List<SLEXMMEventAttribute> orderAttributes, SLEXMMEventAttribute timestampAttribute,
			String startDate, String endDate);

	public abstract SLEXMMEventCollection getEventCollection(int id);
	
	public abstract HashMap<SLEXMMEventAttribute, SLEXMMEventAttributeValue> getAttributeValuesForEvent(
			SLEXMMEvent slexEvent);
	
	public abstract HashMap<SLEXMMEventAttribute, SLEXMMEventAttributeValue> getAttributeValuesForEvent(int evId);

	public abstract boolean insert(SLEXMMEventCollection ec);

	public abstract boolean update(SLEXMMEventCollection ec);

	public abstract SLEXMMEventResultSet getEventsOfCollection(
			SLEXMMEventCollection slexEventCollection);

	public abstract SLEXMMEventResultSet getEventsOfCollection(int ecId);
	
	public abstract SLEXMMEventResultSet getEventsOfCollectionOrdered(int ecId);
	
	public abstract SLEXMMEventResultSet getEventsOfCollectionOrderedBy(
			SLEXMMEventCollection slexEventCollection,
			List<SLEXMMEventAttribute> orderAttributes);

	public abstract boolean insert(SLEXMMEvent e);

	public abstract boolean update(SLEXMMEvent e);

	public abstract boolean insert(SLEXMMAttributeValue at);

	public abstract boolean update(SLEXMMAttributeValue at);
	
	public abstract boolean insert(SLEXMMEventAttributeValue at);

	public abstract boolean update(SLEXMMEventAttributeValue at);
	
	public abstract SLEXMMLogResultSet getLogs();

	public abstract SLEXMMLogResultSet getLogsOfCollection(
			SLEXMMEventCollection ec);
	
	public abstract SLEXMMCase createCase(int logId);

	public abstract SLEXMMLog createLog(
			SLEXMMEventCollection evCol, String name, int process_id);

	public abstract SLEXMMCase cloneCase(SLEXMMCase t);

	public abstract SLEXMMCaseResultSet getCasesOfLog(
			SLEXMMLog slexLog);

	public abstract boolean insert(SLEXMMLog p);

	public abstract boolean update(SLEXMMLog p);

	public abstract boolean removeCaseFromLog(
			SLEXMMLog slexLog, SLEXMMCase t);

	public abstract boolean addEventToCase(int caseId, int eventId);
	public abstract boolean addEventToCase(SLEXMMCase slexCase, SLEXMMEvent e);

	public abstract boolean insert(SLEXMMCase t);

	public abstract boolean update(SLEXMMCase t);

	public abstract SLEXMMEventResultSet getEventsOfCase(SLEXMMCase slexCase);

	public abstract int getNumberEventsOfCase(SLEXMMCase slexCase);

	public abstract int getMaxCaseId();

	public abstract SLEXMMDataModel createDataModel(String name);

	public abstract SLEXMMClass createClass(int data_modelId, String name);

	public abstract SLEXMMAttribute createAttribute(int classId, String name);

	public abstract SLEXMMRelationship createRelationship(String name,
			int sourceClassId, int targetClassId);

	public abstract SLEXMMRelationshipAttribute createRelationshipAttribute(int relationshipId,
			int sourceAttributeId, int targetAttributeId);


	public abstract SLEXMMClassResultSet getClassesForDataModel(
			SLEXMMDataModel dm);

	public abstract SLEXMMDataModelResultSet getDataModels();

	public abstract List<SLEXMMAttribute> getAttributesForClass(SLEXMMClass cl);
	
	public abstract List<SLEXMMAttribute> getAttributesForClass(int clId);

	public abstract List<SLEXMMRelationship> getRelationshipsForClass(SLEXMMClass cl);

	public abstract List<SLEXMMRelationshipAttribute> getRelationshipAttributes(
			SLEXMMRelationship k);

	public abstract boolean insert(SLEXMMAttribute at);

	public abstract boolean update(SLEXMMAttribute at);

	public abstract boolean insert(SLEXMMEventAttribute at);

	public abstract boolean update(SLEXMMEventAttribute at);
	
	public abstract boolean insert(SLEXMMClass cl);

	public abstract boolean update(SLEXMMClass cl);

	public abstract boolean insert(SLEXMMDataModel dm);

	public abstract boolean update(SLEXMMDataModel dm);

	public abstract boolean insert(SLEXMMRelationship at);

	public abstract boolean update(SLEXMMRelationship at);

	public abstract boolean insert(SLEXMMRelationshipAttribute at);

	public abstract boolean update(SLEXMMRelationshipAttribute at);
	
	public abstract boolean insert(SLEXMMRelationshipAttributeValue atv);

	public abstract boolean update(SLEXMMRelationshipAttributeValue atv);

	public abstract boolean insert(SLEXMMRelation rt);

	public abstract boolean update(SLEXMMRelation rt);
	
	public abstract boolean insert(SLEXMMObject obj);

	public abstract boolean update(SLEXMMObject obj);
	
	public abstract boolean insert(SLEXMMObjectVersion objv);

	public abstract boolean update(SLEXMMObjectVersion objv);

	public abstract HashMap<SLEXMMRelationshipAttribute, SLEXMMRelationshipAttributeValue> getRelationAttributes(
			SLEXMMRelation rt);

	public abstract HashMap<SLEXMMAttribute, SLEXMMAttributeValue> getAttributeValuesForObjectVersion(
			SLEXMMObjectVersion objv);

	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForObjectOrdered(
			SLEXMMObject obj);
	
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForObjectOrdered(int objId);
	

	public abstract SLEXMMRelationshipAttributeValue createRelationshipAttributeValue(
			int relationId, int relationshipAttributeId, String value,
			String type);

	public abstract SLEXMMRelation createRelation(int relationshipId, int sourceObjectId,
			int targetObjectId, int startSourceObjectVersionId,
			int endSourceObjectVersionId, int startTargetObjectVersionId,
			int endTargetObjectVersionId, int eventId);

	public abstract SLEXMMObject createObject(int classId);

	public abstract SLEXMMObjectVersion createObjectVersion(int objectId, int eventId);

	public abstract SLEXMMRelationResultSet getRelationsForSourceObject(SLEXMMObject obj);

	public abstract SLEXMMRelationResultSet getRelationsForSourceObject(int objId);
	
	public abstract SLEXMMRelationResultSet getRelationsForTargetObject(SLEXMMObject obj);
	
	public abstract SLEXMMRelationResultSet getRelationsForTargetObject(int objId);
	
	public abstract SLEXMMRelationResultSet getRelationsForSourceObjectOrdered(SLEXMMObject obj);

	public abstract SLEXMMRelationResultSet getRelationsForSourceObjectOrdered(int objId);
	
	public abstract SLEXMMRelationResultSet getRelationsForTargetObjectOrdered(SLEXMMObject obj);
	
	public abstract SLEXMMRelationResultSet getRelationsForTargetObjectOrdered(int objId);
	
	public abstract SLEXMMObjectResultSet getObjects();
	
}
