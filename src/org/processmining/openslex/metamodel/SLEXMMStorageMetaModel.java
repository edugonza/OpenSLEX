package org.processmining.openslex.metamodel;

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

	public abstract List<SLEXMMAttribute> getListAttributesForClass(SLEXMMClass cl);
	
	public abstract List<SLEXMMAttribute> getListAttributesForClass(int clId);

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

	public abstract boolean insert(SLEXMMProcess dm);

	public abstract boolean update(SLEXMMProcess dm);
	
	public abstract SLEXMMProcess createProcess(String name);
	
	public abstract SLEXMMActivity createActivity(int processId, String name);

	public abstract SLEXMMActivityInstance createActivityInstance(SLEXMMActivity act);
	
	public abstract SLEXMMEvent getEventForId(int evId);

	public abstract List<SLEXMMActivity> getActivities();

	public abstract SLEXMMObjectVersionResultSet getObjectVersions();

	public abstract SLEXMMObjectVersionResultSet getVersionsRelatedToObjectVersion(
			SLEXMMObjectVersion ob);

	public abstract SLEXMMObject getObjectPerId(int objectId);

	public abstract boolean addEventToObjectVersion(
			SLEXMMObjectVersion ov, SLEXMMEvent ev, String label);
	public abstract boolean addEventToObjectVersion(int ovId, int evId, String label);

	public abstract SLEXMMRelationResultSet getRelations();

	public abstract List<SLEXMMRelationship> getRelationships();

	public abstract SLEXMMActivityInstanceResultSet getActivityInstances();
	
	public abstract SLEXMMAttributeResultSet getAttributes();
	
	public abstract SLEXMMSQLResultSet executeSQL(String query) throws Exception;
	
	public abstract SLEXMMEventAttributeResultSet getEventAttributes();

	public abstract SLEXMMObjectResultSet getObjectsForEvent(int eventId);
	public abstract SLEXMMObjectResultSet getObjectsForEvents(int[] eventIds);
	
	public abstract SLEXMMObjectResultSet getObjectsForCase(int caseId);
	public abstract SLEXMMObjectResultSet getObjectsForCases(int[] caseIds);

	public abstract SLEXMMObjectResultSet getObjectsForActivity(int activityId);
	public abstract SLEXMMObjectResultSet getObjectsForActivities(int[] activityIds);

	public abstract SLEXMMObjectResultSet getObjectsForClass(int classId);
	public abstract SLEXMMObjectResultSet getObjectsForClasses(int[] classIds);
	
	public abstract SLEXMMObjectResultSet getObjectsForActivityInstance(int activityInstanceId);
	public abstract SLEXMMObjectResultSet getObjectsForActivityInstances(int[] activityInstanceIds);

	public abstract SLEXMMObjectResultSet getObjectsForRelation(int relationId);
	public abstract SLEXMMObjectResultSet getObjectsForRelations(int[] relationIds);

	public abstract SLEXMMObjectResultSet getObjectsForObjectVersion(int objectVersionId);
	public abstract SLEXMMObjectResultSet getObjectsForObjectVersions(int[] objectVersionIds);
	
	public abstract SLEXMMObjectResultSet getObjectsForRelationship(int relationshipId);
	public abstract SLEXMMObjectResultSet getObjectsForRelationships(int[] relationshipIds);
	
	public abstract SLEXMMObjectResultSet getObjectsForAttribute(int attributeId);
	public abstract SLEXMMObjectResultSet getObjectsForAttributes(int[] attributeIds);

	public abstract SLEXMMCaseResultSet getCasesForLog(int logId);
	public abstract SLEXMMCaseResultSet getCasesForLogs(int[] logIds);
	
	public abstract SLEXMMCaseResultSet getCasesForProcess(int processId);
	public abstract SLEXMMCaseResultSet getCasesForProcess(int[] processIds);
	
	public abstract SLEXMMCaseResultSet getCasesForObject(int objectId);
	public abstract SLEXMMCaseResultSet getCasesForObjects(int[] objectIds);

	public abstract SLEXMMCaseResultSet getCasesForEvent(int eventId);
	public abstract SLEXMMCaseResultSet getCasesForEvents(int[] eventIds);

	public abstract SLEXMMCaseResultSet getCasesForActivity(int activityId);
	public abstract SLEXMMCaseResultSet getCasesForActivities(int[] activityIds);

	public abstract SLEXMMCaseResultSet getCasesForClass(int classId);
	public abstract SLEXMMCaseResultSet getCasesForClasses(int[] classIds);

	public abstract SLEXMMCaseResultSet getCasesForRelationship(int RelationshipId);
	public abstract SLEXMMCaseResultSet getCasesForRelationships(int[] RelationshipIds);

	public abstract SLEXMMCaseResultSet getCasesForObjectVersion(int objectVersionId);
	public abstract SLEXMMCaseResultSet getCasesForObjectVersions(int[] objectVersionIds);

	public abstract SLEXMMCaseResultSet getCasesForRelation(int relationId);
	public abstract SLEXMMCaseResultSet getCasesForRelations(int[] relationIds);

	public abstract SLEXMMCaseResultSet getCasesForActivityInstance(int activityInstanceId);
	public abstract SLEXMMCaseResultSet getCasesForActivityInstances(int[] activityInstanceIds);

	public abstract SLEXMMCaseResultSet getCasesForAttribute(int attributeId);
	public abstract SLEXMMCaseResultSet getCasesForAttributes(int[] attributeIds);

	public abstract SLEXMMEventResultSet getEventsForObject(int objectId);
	public abstract SLEXMMEventResultSet getEventsForObjects(int[] objectIds);

	public abstract SLEXMMEventResultSet getEventsForCase(int caseId);
	public abstract SLEXMMEventResultSet getEventsForCases(int[] caseIds);
	
	public abstract SLEXMMEventResultSet getEventsForActivity(int activityId);
	public abstract SLEXMMEventResultSet getEventsForActivities(int[] activityIds);
	
	public abstract SLEXMMEventResultSet getEventsForClass(int classId);
	public abstract SLEXMMEventResultSet getEventsForClasses(int[] classIds);

	public abstract SLEXMMEventResultSet getEventsForRelationship(int relationshipId);
	public abstract SLEXMMEventResultSet getEventsForRelationships(int[] relationshipIds);

	public abstract SLEXMMEventResultSet getEventsForObjectVersion(int objectVersionId);
	public abstract SLEXMMEventResultSet getEventsForObjectVersions(int[] objectVersionIds);
	
	public abstract SLEXMMEventResultSet getEventsForRelation(int relationId);
	public abstract SLEXMMEventResultSet getEventsForRelations(int[] relationIds);

	public abstract SLEXMMEventResultSet getEventsForActivityInstance(int activityInstanceId);
	public abstract SLEXMMEventResultSet getEventsForActivityInstances(int[] activityInstanceIds);
	
	public abstract SLEXMMEventResultSet getEventsForAttribute(int attributeId);
	public abstract SLEXMMEventResultSet getEventsForAttributes(int[] attributeIds);
	
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForObject(int objId);
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForObjects(int[] objIds);
	
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForEvent(
			int eventId);
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForEvents(
			int[] eventIds);
	
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForActivity(
			int activityId);
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForActivities(
			int[] activityIds);

	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForCase(int caseId);
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForCases(int[] caseIds);

	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForClass(
			int classId);
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForClasses(
			int[] classIds);

	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForRelationship(
			int relationshipId);
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForRelationships(
			int[] relationshipIds);

	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForRelation(
			int relationId);
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForRelations(
			int[] relationIds);

	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForActivityInstance(
			int activityInstanceId);
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForActivityInstances(
			int[] activityInstanceIds);
	
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForAttribute(
			int attributeId);
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForAttributes(
			int[] attributeIds);

	public abstract SLEXMMActivityResultSet getActivitiesForObject(int objectId);
	public abstract SLEXMMActivityResultSet getActivitiesForObjects(int[] objectIds);

	public abstract SLEXMMActivityResultSet getActivitiesForEvent(int eventId);
	public abstract SLEXMMActivityResultSet getActivitiesForEvents(int[] eventIds);

	public abstract SLEXMMActivityResultSet getActivitiesForCase(int caseId);
	public abstract SLEXMMActivityResultSet getActivitiesForCases(int[] caseIds);

	public abstract SLEXMMActivityResultSet getActivitiesForClass(int classId);
	public abstract SLEXMMActivityResultSet getActivitiesForClasses(int[] classIds);

	public abstract SLEXMMActivityResultSet getActivitiesForRelationship(int relationshipId);
	public abstract SLEXMMActivityResultSet getActivitiesForRelationships(int[] relationshipIds);

	public abstract SLEXMMActivityResultSet getActivitiesForObjectVersion(int objectVersionId);
	public abstract SLEXMMActivityResultSet getActivitiesForObjectVersions(int[] objectVersionIds);

	public abstract SLEXMMActivityResultSet getActivitiesForRelation(int relationId);
	public abstract SLEXMMActivityResultSet getActivitiesForRelations(int[] relationIds);

	public abstract SLEXMMActivityResultSet getActivitiesForActivityInstance(
			int activityInstanceId);
	public abstract SLEXMMActivityResultSet getActivitiesForActivityInstances(
			int[] activityInstanceIds);

	public abstract SLEXMMActivityResultSet getActivitiesForAttribute(int attributeId);
	public abstract SLEXMMActivityResultSet getActivitiesForAttributes(int[] attributeIds);

	public abstract SLEXMMClassResultSet getClassesForObject(int objectId);
	public abstract SLEXMMClassResultSet getClassesForObjects(int[] objectIds);

	public abstract SLEXMMClassResultSet getClassesForEvent(int eventId);
	public abstract SLEXMMClassResultSet getClassesForEvents(int[] eventIds);

	public abstract SLEXMMClassResultSet getClassesForCase(int caseId);
	public abstract SLEXMMClassResultSet getClassesForCases(int[] caseIds);

	public abstract SLEXMMClassResultSet getClassesForActivity(int activityId);
	public abstract SLEXMMClassResultSet getClassesForActivities(int[] activityIds);

	public abstract SLEXMMClassResultSet getClassesForRelationship(int relationshipId);
	public abstract SLEXMMClassResultSet getClassesForRelationships(int[] relationshipIds);

	public abstract SLEXMMClassResultSet getClassesForObjectVersion(int objectVersionId);
	public abstract SLEXMMClassResultSet getClassesForObjectVersions(int[] objectVersionIds);

	public abstract SLEXMMClassResultSet getClassesForRelation(int relationId);
	public abstract SLEXMMClassResultSet getClassesForRelations(int[] relationIds);

	public abstract SLEXMMClassResultSet getClassesForActivityInstance(int activityInstanceId);
	public abstract SLEXMMClassResultSet getClassesForActivityInstances(int[] activityInstanceIds);

	public abstract SLEXMMClassResultSet getClassesForAttribute(int attributeId);
	public abstract SLEXMMClassResultSet getClassesForAttributes(int[] attributeIds);

	public abstract SLEXMMRelationResultSet getRelationsForObject(int objectId);
	public abstract SLEXMMRelationResultSet getRelationsForObjects(int[] objectIds);

	public abstract SLEXMMRelationResultSet getRelationsForEvent(int eventId);
	public abstract SLEXMMRelationResultSet getRelationsForEvents(int[] eventIds);

	public abstract SLEXMMRelationResultSet getRelationsForCase(int caseId);
	public abstract SLEXMMRelationResultSet getRelationsForCases(int[] caseIds);

	public abstract SLEXMMRelationResultSet getRelationsForActivity(int activityId);
	public abstract SLEXMMRelationResultSet getRelationsForActivities(int[] activityIds);

	public abstract SLEXMMRelationResultSet getRelationsForClass(int classId);
	public abstract SLEXMMRelationResultSet getRelationsForClasses(int[] classIds);

	public abstract SLEXMMRelationResultSet getRelationsForRelationship(int relationshipId);
	public abstract SLEXMMRelationResultSet getRelationsForRelationships(int[] relationshipIds);

	public abstract SLEXMMRelationResultSet getRelationsForObjectVersion(int objectVersionId);
	public abstract SLEXMMRelationResultSet getRelationsForObjectVersions(int[] objectVersionIds);

	public abstract SLEXMMRelationResultSet getRelationsForActivityInstance(
			int activityInstanceId);
	public abstract SLEXMMRelationResultSet getRelationsForActivityInstances(
			int[] activityInstanceIds);

	public abstract SLEXMMRelationResultSet getRelationsForAttribute(int attributeId);
	public abstract SLEXMMRelationResultSet getRelationsForAttributes(int[] attributeIds);

	public abstract SLEXMMRelationshipResultSet getRelationshipsForObject(int objectId);
	public abstract SLEXMMRelationshipResultSet getRelationshipsForObjects(int[] objectIds);

	public abstract SLEXMMRelationshipResultSet getRelationshipsForEvent(int eventId);
	public abstract SLEXMMRelationshipResultSet getRelationshipsForEvents(int[] eventIds);

	public abstract SLEXMMRelationshipResultSet getRelationshipsForCase(int caseId);
	public abstract SLEXMMRelationshipResultSet getRelationshipsForCases(int[] caseIds);

	public abstract SLEXMMRelationshipResultSet getRelationshipsForActivity(
			int activityId);
	public abstract SLEXMMRelationshipResultSet getRelationshipsForActivities(
			int[] activityIds);

	public abstract SLEXMMRelationshipResultSet getRelationshipsForClass(int classId);
	public abstract SLEXMMRelationshipResultSet getRelationshipsForClasses(int[] classIds);

	public abstract SLEXMMRelationshipResultSet getRelationshipsForObjectVersion(
			int objectVersionId);
	public abstract SLEXMMRelationshipResultSet getRelationshipsForObjectVersions(
			int[] objectVersionIds);

	public abstract SLEXMMRelationshipResultSet getRelationshipsForRelation(
			int relationId);
	public abstract SLEXMMRelationshipResultSet getRelationshipsForRelations(
			int[] relationIds);

	public abstract SLEXMMRelationshipResultSet getRelationshipsForActivityInstance(
			int activityInstanceId);
	public abstract SLEXMMRelationshipResultSet getRelationshipsForActivityInstances(
			int[] activityInstanceIds);

	public abstract SLEXMMRelationshipResultSet getRelationshipsForAttribute(
			int attributeId);
	public abstract SLEXMMRelationshipResultSet getRelationshipsForAttributes(
			int[] attributeIds);

	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForObject(
			int objectId);
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForObjects(
			int[] objectIds);

	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForEvent(
			int eventId);
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForEvents(
			int[] eventIds);

	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForCase(
			int caseId);
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForCases(
			int[] caseIds);

	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForActivity(
			int activityId);
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForActivities(
			int[] activityIds);

	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForClass(
			int classId);
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForClasses(
			int[] classIds);

	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForRelationship(
			int relationshipId);
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForRelationships(
			int[] relationshipIds);

	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForObjectVersion(
			int objectVersionId);
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForObjectVersions(
			int[] objectVersionIds);

	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForRelation(
			int relationId);
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForRelations(
			int[] relationIds);

	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForAttribute(
			int attributeId);
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForAttributes(
			int[] attributeIds);

	public abstract SLEXMMAttributeResultSet getAttributesForObject(int objectId);
	public abstract SLEXMMAttributeResultSet getAttributesForObjects(int[] objectIds);

	public abstract SLEXMMAttributeResultSet getAttributesForEvent(int eventId);
	public abstract SLEXMMAttributeResultSet getAttributesForEvents(int[] eventIds);

	public abstract SLEXMMAttributeResultSet getAttributesForCase(int caseId);
	public abstract SLEXMMAttributeResultSet getAttributesForCases(int[] caseIds);

	public abstract SLEXMMAttributeResultSet getAttributesForActivity(int activityId);
	public abstract SLEXMMAttributeResultSet getAttributesForActivities(int[] activityIds);

	public abstract SLEXMMAttributeResultSet getAttributesForClass(int classId);
	public abstract SLEXMMAttributeResultSet getAttributesForClasses(int[] classIds);

	public abstract SLEXMMAttributeResultSet getAttributesForRelationship(
			int relationshipId);
	public abstract SLEXMMAttributeResultSet getAttributesForRelationships(
			int[] relationshipIds);

	public abstract SLEXMMAttributeResultSet getAttributesForObjectVersion(
			int objectVersionId);
	public abstract SLEXMMAttributeResultSet getAttributesForObjectVersions(
			int[] objectVersionIds);

	public abstract SLEXMMAttributeResultSet getAttributesForRelation(int relationId);
	public abstract SLEXMMAttributeResultSet getAttributesForRelations(int[] relationIds);

	public abstract SLEXMMAttributeResultSet getAttributesForActivityInstance(
			int activityInstanceId);
	public abstract SLEXMMAttributeResultSet getAttributesForActivityInstances(
			int[] activityInstanceIds);

	public abstract SLEXMMPeriodResultSet getPeriodsForObjects(int[] objectIds);
	
	public abstract boolean insert(SLEXMMLog log);

	public abstract boolean update(SLEXMMLog log);
	
	public abstract boolean addCaseToLog(int logId, int caseId);
	
	public abstract boolean addActivityToProcess(int processId, int actId);
	
}
