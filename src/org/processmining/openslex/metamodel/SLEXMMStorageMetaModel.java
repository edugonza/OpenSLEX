package org.processmining.openslex.metamodel;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public interface SLEXMMStorageMetaModel extends SLEXMMStorage {
	
	public abstract SLEXMMEvent createEvent(int order, int activity_instance_id,
			String lifecycle, String resource, long timestamp);
	
	public abstract SLEXMMEventAttribute createEventAttribute(String name);

	public abstract SLEXMMEventAttributeValue createEventAttributeValue(int attributeId,
			int eventId, String value, String type);

	public abstract SLEXMMAttributeValue createAttributeValue(int attributeId,
			int objectVersionId, String value, String type);

	public abstract HashMap<SLEXMMEventAttribute, SLEXMMEventAttributeValue> getAttributeValuesForEvent(
			SLEXMMEvent slexEvent);
	
	public abstract HashMap<SLEXMMEventAttribute, SLEXMMEventAttributeValue> getAttributeValuesForEvent(int evId);

	public abstract SLEXMMEventResultSet getEvents();
	
	public abstract SLEXMMEventResultSet getEventsOrdered();

	public abstract SLEXMMEventResultSet getEventsForCaseOrdered(int caseId);
	
	public abstract SLEXMMEventResultSet getEventsForCaseOrdered(SLEXMMCase slexcase);
	
	public abstract boolean insert(SLEXMMEvent e);

	public abstract boolean update(SLEXMMEvent e);

	public abstract boolean insert(SLEXMMAttributeValue at);

	public abstract boolean update(SLEXMMAttributeValue at);
	
	public abstract boolean insert(SLEXMMEventAttributeValue at);

	public abstract boolean update(SLEXMMEventAttributeValue at);
	
	public abstract SLEXMMCase createCase(String name);

	public abstract SLEXMMCase cloneCase(SLEXMMCase t);

	public abstract SLEXMMCaseResultSet getCases();

	public abstract boolean addActivityInstanceToCase(int caseId, int actInsId);
	public abstract boolean addActivityInstanceToCase(SLEXMMCase slexCase, SLEXMMActivityInstance ai);

	public abstract boolean insert(SLEXMMCase t);

	public abstract boolean update(SLEXMMCase t);

	public abstract SLEXMMEventResultSet getEventsOfCase(SLEXMMCase c);
	public abstract SLEXMMEventResultSet getEventsOfCase(int caseId);

	public abstract int getNumberEventsOfCase(SLEXMMCase slexCase);

	public abstract int getMaxCaseId();

	public abstract SLEXMMDataModel createDataModel(String name);

	public abstract SLEXMMClass createClass(int data_modelId, String name);

	public abstract SLEXMMAttribute createAttribute(int classId, String name);

	public abstract SLEXMMRelationship createRelationship(String name,
			int sourceClassId, int targetClassId);

	public abstract SLEXMMClassResultSet getClassesForDataModel(
			SLEXMMDataModel dm);

	public abstract SLEXMMDataModelResultSet getDataModels();

	public abstract List<SLEXMMAttribute> getAttributesForClass(SLEXMMClass cl);
	
	public abstract List<SLEXMMAttribute> getAttributesForClass(int clId);

	public abstract List<SLEXMMRelationship> getRelationshipsForClass(SLEXMMClass cl);

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

	public abstract boolean insert(SLEXMMRelation rt);

	public abstract boolean update(SLEXMMRelation rt);
	
	public abstract boolean insert(SLEXMMObject obj);

	public abstract boolean update(SLEXMMObject obj);
	
	public abstract boolean insert(SLEXMMObjectVersion objv);

	public abstract boolean update(SLEXMMObjectVersion objv);

	public abstract HashMap<SLEXMMAttribute, SLEXMMAttributeValue> getAttributeValuesForObjectVersion(
			SLEXMMObjectVersion objv);
	
	public abstract HashMap<SLEXMMAttribute, SLEXMMAttributeValue> getAttributeValuesForObjectVersion(
			int objvId);

	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForObjectOrdered(
			SLEXMMObject obj);
	
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForObjectOrdered(int objId);

	public abstract SLEXMMRelation createRelation(int sourceObjectVersionId,
			int targetObjectVersionId, int relationshipId,
			long startTimestamp, long endTimestamp);

	public abstract SLEXMMObject createObject(int classId);

	public abstract SLEXMMObjectVersion createObjectVersion(int objectId, 
			long startTimestamp, long endTimestamp);

	public abstract SLEXMMRelationResultSet getRelationsForSourceObject(SLEXMMObject obj);

	public abstract SLEXMMRelationResultSet getRelationsForSourceObject(int objId);
	
	public abstract SLEXMMRelationResultSet getRelationsForTargetObject(SLEXMMObject obj);
	
	public abstract SLEXMMRelationResultSet getRelationsForTargetObject(int objId);
	
	public abstract SLEXMMRelationResultSet getRelationsForSourceObjectOrdered(SLEXMMObject obj);

	public abstract SLEXMMRelationResultSet getRelationsForSourceObjectOrdered(int objId);
	
	public abstract SLEXMMRelationResultSet getRelationsForTargetObjectOrdered(SLEXMMObject obj);
	
	public abstract SLEXMMRelationResultSet getRelationsForTargetObjectOrdered(int objId);
	
	public abstract SLEXMMObjectResultSet getObjects();

	public abstract boolean insert(SLEXMMActivity cl);

	public abstract boolean update(SLEXMMActivity cl);

	public abstract boolean insert(SLEXMMActivityInstance e);

	public abstract boolean update(SLEXMMActivityInstance e);

	public abstract SLEXMMEventResultSet getEventsForActivityInstanceOrdered(
			int aiId);
	
	public abstract SLEXMMEventResultSet getEventsForActivityInstanceOrdered(
			SLEXMMActivityInstance slexmmActivityInstance);

	public abstract boolean insert(SLEXMMProcess dm);

	public abstract boolean update(SLEXMMProcess dm);
	
	public abstract SLEXMMProcess createProcess(String name);
	
	public abstract SLEXMMActivity createActivity(int processId, String name);

	public abstract SLEXMMActivityInstance createActivityInstance(SLEXMMActivity act);
	
	public abstract SLEXMMEvent getEventForId(int evId);

	public abstract List<SLEXMMActivity> getActivities();

	public abstract SLEXMMObjectResultSet getObjectsPerClass(int classId);

	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForActivity(
			SLEXMMActivity ob);

	public abstract SLEXMMObjectVersionResultSet getObjectVersions();

	public abstract SLEXMMObjectVersionResultSet getVersionsRelatedToObjectVersion(
			SLEXMMObjectVersion ob);

	public abstract SLEXMMEventResultSet getEventsForActivity(SLEXMMActivity act);

	public abstract SLEXMMObjectResultSet getObjectsForEvent(SLEXMMEvent ob);

	public abstract SLEXMMObject getObjectPerId(int objectId);

	public abstract SLEXMMEventResultSet getEventsForObjectVersion(int id);

	public abstract boolean addEventToObjectVersion(
			SLEXMMObjectVersion ov, SLEXMMEvent ev, String label);
	public abstract boolean addEventToObjectVersion(int ovId, int evId, String label);

	public abstract SLEXMMRelationResultSet getRelations();

	public abstract Collection<? extends Object> getRelationships();

	public abstract SLEXMMActivityInstanceResultSet getActivityInstances();
	
	public abstract SLEXMMAttributeResultSet getAttributes();
	
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForCase(int caseId);
	
	public abstract SLEXMMSQLResultSet executeSQL(String query) throws Exception;
	
	public abstract SLEXMMEventAttributeResultSet getEventAttributes();

	public abstract SLEXMMObjectResultSet getObjectsForCase(int caseId);

	public abstract SLEXMMObjectResultSet getObjectsForActivity(int activityId);

	public abstract SLEXMMObjectResultSet getObjectsForActivityInstance(int activityInstanceId);

	public abstract SLEXMMObjectResultSet getObjectsForRelation(int relationId);

	public abstract SLEXMMObjectResultSet getObjectsForRelationship(int relationshipId);
	
	public abstract SLEXMMObjectResultSet getObjectsForAttribute(int attributeId);

	public abstract SLEXMMCaseResultSet getCasesForObject(int objectId);

	public abstract SLEXMMCaseResultSet getCasesForEvent(int eventId);

	public abstract SLEXMMCaseResultSet getCasesForActivity(int activityId);

	public abstract SLEXMMCaseResultSet getCasesForClass(int classId);

	public abstract SLEXMMCaseResultSet getCasesForRelationship(int RelationshipId);

	public abstract SLEXMMCaseResultSet getCasesForObjectVersion(int objectVersionId);

	public abstract SLEXMMCaseResultSet getCasesForRelation(int relationId);

	public abstract SLEXMMCaseResultSet getCasesForActivityInstance(int activityInstanceId);

	public abstract SLEXMMCaseResultSet getCasesForAttribute(int attributeId);

	public abstract SLEXMMEventResultSet getEventsForObject(int objectId);

	public abstract SLEXMMEventResultSet getEventsForClass(int classId);

	public abstract SLEXMMEventResultSet getEventsForRelationship(int relationshipId);

	public abstract SLEXMMEventResultSet getEventsForRelation(int relationId);

	public abstract SLEXMMEventResultSet getEventsForActivityInstance(int activityInstanceId);

	public abstract SLEXMMEventResultSet getEventsForAttribute(int attributeId);

	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForEvent(
			int eventId);

	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForCase(int caseId);

	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForClass(
			int classId);

	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForRelationship(
			int relationshipId);

	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForRelation(
			int relationId);

	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForActivityInstance(
			int activityInstanceId);

	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForAttribute(
			int attributeId);

	public abstract SLEXMMActivityResultSet getActivitiesForObject(int objectId);

	public abstract SLEXMMActivityResultSet getActivitiesForEvent(int eventId);

	public abstract SLEXMMActivityResultSet getActivitiesForCase(int caseId);

	public abstract SLEXMMActivityResultSet getActivitiesForClass(int classId);

	public abstract SLEXMMActivityResultSet getActivitiesForRelationship(int relationshipId);

	public abstract SLEXMMActivityResultSet getActivitiesForObjectVersion(int objectVersionId);

	public abstract SLEXMMActivityResultSet getActivitiesForRelation(int relationId);

	public abstract SLEXMMActivityResultSet getActivitiesForActivityInstance(
			int activityInstanceId);

	public abstract SLEXMMActivityResultSet getActivitiesForAttribute(int attributeId);

	public abstract SLEXMMClassResultSet getClassesForObject(int objectId);

	public abstract SLEXMMClassResultSet getClassesForEvent(int eventId);

	public abstract SLEXMMClassResultSet getClassesForCase(int caseId);

	public abstract SLEXMMClassResultSet getClassesForActivity(int activityId);

	public abstract SLEXMMClassResultSet getClassesForRelationship(int relationshipId);

	public abstract SLEXMMClassResultSet getClassesForObjectVersion(int objectVersionId);

	public abstract SLEXMMClassResultSet getClassesForRelation(int relationId);

	public abstract SLEXMMClassResultSet getClassesForActivityInstance(int activityInstanceId);

	public abstract SLEXMMClassResultSet getClassesForAttribute(int attributeId);

	public abstract SLEXMMRelationResultSet getRelationsForObject(int objectId);

	public abstract SLEXMMRelationResultSet getRelationsForEvent(int eventId);

	public abstract SLEXMMRelationResultSet getRelationsForCase(int caseId);
	public abstract SLEXMMRelationResultSet getRelationsForCases(int[] caseIds);

	public abstract SLEXMMRelationResultSet getRelationsForActivity(int activityId);

	public abstract SLEXMMRelationResultSet getRelationsForClass(int classId);
	public abstract SLEXMMRelationResultSet getRelationsForClasses(int[] classIds);

	public abstract SLEXMMRelationResultSet getRelationsForRelationship(int relationshipId);

	public abstract SLEXMMRelationResultSet getRelationsForObjectVersion(int objectVersionId);

	public abstract SLEXMMRelationResultSet getRelationsForActivityInstance(
			int activityInstanceId);

	public abstract SLEXMMRelationResultSet getRelationsForAttribute(int attributeId);

	public abstract SLEXMMRelationshipResultSet getRelationshipsForObject(int objectId);

	public abstract SLEXMMRelationshipResultSet getRelationshipsForEvent(int eventId);

	public abstract SLEXMMRelationshipResultSet getRelationshipsForCase(int caseId);

	public abstract SLEXMMRelationshipResultSet getRelationshipsForActivity(
			int activityId);

	public abstract SLEXMMRelationshipResultSet getRelationshipsForClass(int classId);

	public abstract SLEXMMRelationshipResultSet getRelationshipsForObjectVersion(
			int objectVersionId);

	public abstract SLEXMMRelationshipResultSet getRelationshipsForRelation(
			int relationId);

	public abstract SLEXMMRelationshipResultSet getRelationshipsForActivityInstance(
			int activityInstanceId);

	public abstract SLEXMMRelationshipResultSet getRelationshipsForAttribute(
			int attributeId);
	
}
