/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * The Interface SLEXMMStorageMetaModel.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public interface SLEXMMStorageMetaModel extends SLEXMMStorage {
	
	/**
	 * Creates the event.
	 *
	 * @param order the order
	 * @param activity_instance_id the activity_instance_id
	 * @param lifecycle the lifecycle
	 * @param resource the resource
	 * @param timestamp the timestamp
	 * @return the SLEXMM event
	 */
	public abstract SLEXMMEvent createEvent(int order, int activity_instance_id,
			String lifecycle, String resource, long timestamp);
	
	/**
	 * Creates the event attribute.
	 *
	 * @param name the name
	 * @return the SLEXMM event attribute
	 */
	public abstract SLEXMMEventAttribute createEventAttribute(String name);

	/**
	 * Creates the event attribute value.
	 *
	 * @param attributeId the attribute id
	 * @param eventId the event id
	 * @param value the value
	 * @param type the type
	 * @return the SLEXMM event attribute value
	 */
	public abstract SLEXMMEventAttributeValue createEventAttributeValue(int attributeId,
			int eventId, String value, String type);

	/**
	 * Creates the attribute value.
	 *
	 * @param attributeId the attribute id
	 * @param objectVersionId the object version id
	 * @param value the value
	 * @param type the type
	 * @return the SLEXMM attribute value
	 */
	public abstract SLEXMMAttributeValue createAttributeValue(int attributeId,
			int objectVersionId, String value, String type);

	/**
	 * Gets the attribute values for event.
	 *
	 * @param slexEvent the slex event
	 * @return the attribute values for event
	 */
	public abstract HashMap<SLEXMMEventAttribute, SLEXMMEventAttributeValue> getAttributeValuesForEvent(
			SLEXMMEvent slexEvent);
	
	/**
	 * Gets the attribute values for event.
	 *
	 * @param evId the ev id
	 * @return the attribute values for event
	 */
	public abstract HashMap<SLEXMMEventAttribute, SLEXMMEventAttributeValue> getAttributeValuesForEvent(int evId);

	/**
	 * Gets the events.
	 *
	 * @return the events
	 */
	public abstract SLEXMMEventResultSet getEvents();
	
	/**
	 * Gets the events ordered.
	 *
	 * @return the events ordered
	 */
	public abstract SLEXMMEventResultSet getEventsOrdered();
	
	/**
	 * Insert.
	 *
	 * @param e the e
	 * @return true, if successful
	 */
	public abstract boolean insert(SLEXMMEvent e);

	/**
	 * Update.
	 *
	 * @param e the e
	 * @return true, if successful
	 */
	public abstract boolean update(SLEXMMEvent e);

	/**
	 * Insert.
	 *
	 * @param at the at
	 * @return true, if successful
	 */
	public abstract boolean insert(SLEXMMAttributeValue at);

	/**
	 * Update.
	 *
	 * @param at the at
	 * @return true, if successful
	 */
	public abstract boolean update(SLEXMMAttributeValue at);
	
	/**
	 * Insert.
	 *
	 * @param at the at
	 * @return true, if successful
	 */
	public abstract boolean insert(SLEXMMEventAttributeValue at);

	/**
	 * Update.
	 *
	 * @param at the at
	 * @return true, if successful
	 */
	public abstract boolean update(SLEXMMEventAttributeValue at);
	
	/**
	 * Creates the case.
	 *
	 * @param name the name
	 * @return the SLEXMM case
	 */
	public abstract SLEXMMCase createCase(String name);

	/**
	 * Clone case.
	 *
	 * @param t the t
	 * @return the SLEXMM case
	 */
	public abstract SLEXMMCase cloneCase(SLEXMMCase t);

	/**
	 * Gets the cases.
	 *
	 * @return the cases
	 */
	public abstract SLEXMMCaseResultSet getCases();

	/**
	 * Adds the activity instance to case.
	 *
	 * @param caseId the case id
	 * @param actInsId the act ins id
	 * @return true, if successful
	 */
	public abstract boolean addActivityInstanceToCase(int caseId, int actInsId);
	
	/**
	 * Adds the activity instance to case.
	 *
	 * @param slexCase the slex case
	 * @param ai the ai
	 * @return true, if successful
	 */
	public abstract boolean addActivityInstanceToCase(SLEXMMCase slexCase, SLEXMMActivityInstance ai);

	/**
	 * Insert.
	 *
	 * @param t the t
	 * @return true, if successful
	 */
	public abstract boolean insert(SLEXMMCase t);

	/**
	 * Update.
	 *
	 * @param t the t
	 * @return true, if successful
	 */
	public abstract boolean update(SLEXMMCase t);

	/**
	 * Gets the events of case.
	 *
	 * @param c the c
	 * @return the events of case
	 */
	public abstract SLEXMMEventResultSet getEventsOfCase(SLEXMMCase c);
	
	/**
	 * Gets the events of case.
	 *
	 * @param is the case ids
	 * @return the events of case
	 */
	public abstract SLEXMMEventResultSet getEventsOfCase(int[] is);

	/**
	 * Gets the number events of case.
	 *
	 * @param slexCase the slex case
	 * @return the number events of case
	 */
	public abstract int getNumberEventsOfCase(SLEXMMCase slexCase);

	/**
	 * Gets the max case id.
	 *
	 * @return the max case id
	 */
	public abstract int getMaxCaseId();

	/**
	 * Creates the data model.
	 *
	 * @param name the name
	 * @return the SLEXMM data model
	 */
	public abstract SLEXMMDataModel createDataModel(String name);

	/**
	 * Creates the class.
	 *
	 * @param data_modelId the data_model id
	 * @param name the name
	 * @return the SLEXMM class
	 */
	public abstract SLEXMMClass createClass(int data_modelId, String name);

	/**
	 * Creates the attribute.
	 *
	 * @param classId the class id
	 * @param name the name
	 * @return the SLEXMM attribute
	 */
	public abstract SLEXMMAttribute createAttribute(int classId, String name);

	/**
	 * Creates the relationship.
	 *
	 * @param name the name
	 * @param sourceClassId the source class id
	 * @param targetClassId the target class id
	 * @return the SLEXMM relationship
	 */
	public abstract SLEXMMRelationship createRelationship(String name,
			int sourceClassId, int targetClassId);

	/**
	 * Gets the data models.
	 *
	 * @return the data models
	 */
	public abstract SLEXMMDataModelResultSet getDataModels();

	/**
	 * Gets the list attributes for class.
	 *
	 * @param cl the cl
	 * @return the list attributes for class
	 */
	public abstract List<SLEXMMAttribute> getListAttributesForClass(SLEXMMClass cl);
	
	/**
	 * Gets the list attributes for class.
	 *
	 * @param clId the cl id
	 * @return the list attributes for class
	 */
	public abstract List<SLEXMMAttribute> getListAttributesForClass(int clId);

	/**
	 * Gets the relationships for class.
	 *
	 * @param cl the cl
	 * @return the relationships for class
	 */
	public abstract List<SLEXMMRelationship> getRelationshipsForClass(SLEXMMClass cl);

	/**
	 * Insert.
	 *
	 * @param at the at
	 * @return true, if successful
	 */
	public abstract boolean insert(SLEXMMAttribute at);

	/**
	 * Update.
	 *
	 * @param at the at
	 * @return true, if successful
	 */
	public abstract boolean update(SLEXMMAttribute at);

	/**
	 * Insert.
	 *
	 * @param at the at
	 * @return true, if successful
	 */
	public abstract boolean insert(SLEXMMEventAttribute at);

	/**
	 * Update.
	 *
	 * @param at the at
	 * @return true, if successful
	 */
	public abstract boolean update(SLEXMMEventAttribute at);
	
	/**
	 * Insert.
	 *
	 * @param cl the cl
	 * @return true, if successful
	 */
	public abstract boolean insert(SLEXMMClass cl);

	/**
	 * Update.
	 *
	 * @param cl the cl
	 * @return true, if successful
	 */
	public abstract boolean update(SLEXMMClass cl);

	/**
	 * Insert.
	 *
	 * @param dm the dm
	 * @return true, if successful
	 */
	public abstract boolean insert(SLEXMMDataModel dm);

	/**
	 * Update.
	 *
	 * @param dm the dm
	 * @return true, if successful
	 */
	public abstract boolean update(SLEXMMDataModel dm);

	/**
	 * Insert.
	 *
	 * @param at the at
	 * @return true, if successful
	 */
	public abstract boolean insert(SLEXMMRelationship at);

	/**
	 * Update.
	 *
	 * @param at the at
	 * @return true, if successful
	 */
	public abstract boolean update(SLEXMMRelationship at);

	/**
	 * Insert.
	 *
	 * @param rt the rt
	 * @return true, if successful
	 */
	public abstract boolean insert(SLEXMMRelation rt);

	/**
	 * Update.
	 *
	 * @param rt the rt
	 * @return true, if successful
	 */
	public abstract boolean update(SLEXMMRelation rt);
	
	/**
	 * Insert.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	public abstract boolean insert(SLEXMMObject obj);

	/**
	 * Update.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	public abstract boolean update(SLEXMMObject obj);
	
	/**
	 * Insert.
	 *
	 * @param objv the objv
	 * @return true, if successful
	 */
	public abstract boolean insert(SLEXMMObjectVersion objv);

	/**
	 * Update.
	 *
	 * @param objv the objv
	 * @return true, if successful
	 */
	public abstract boolean update(SLEXMMObjectVersion objv);

	/**
	 * Gets the attribute values for object version.
	 *
	 * @param objv the objv
	 * @return the attribute values for object version
	 */
	public abstract HashMap<SLEXMMAttribute, SLEXMMAttributeValue> getAttributeValuesForObjectVersion(
			SLEXMMObjectVersion objv);
	
	/**
	 * Gets the attribute values for object version.
	 *
	 * @param objvId the objv id
	 * @return the attribute values for object version
	 */
	public abstract HashMap<SLEXMMAttribute, SLEXMMAttributeValue> getAttributeValuesForObjectVersion(
			int objvId);

	/**
	 * Creates the relation.
	 *
	 * @param sourceObjectVersionId the source object version id
	 * @param targetObjectVersionId the target object version id
	 * @param relationshipId the relationship id
	 * @param startTimestamp the start timestamp
	 * @param endTimestamp the end timestamp
	 * @return the SLEXMM relation
	 */
	public abstract SLEXMMRelation createRelation(int sourceObjectVersionId,
			int targetObjectVersionId, int relationshipId,
			long startTimestamp, long endTimestamp);

	/**
	 * Creates the object.
	 *
	 * @param classId the class id
	 * @return the SLEXMM object
	 */
	public abstract SLEXMMObject createObject(int classId);

	/**
	 * Creates the object version.
	 *
	 * @param objectId the object id
	 * @param startTimestamp the start timestamp
	 * @param endTimestamp the end timestamp
	 * @return the SLEXMM object version
	 */
	public abstract SLEXMMObjectVersion createObjectVersion(int objectId, 
			long startTimestamp, long endTimestamp);

	/**
	 * Gets the relations for source object.
	 *
	 * @param obj the obj
	 * @return the relations for source object
	 */
	public abstract SLEXMMRelationResultSet getRelationsForSourceObject(SLEXMMObject obj);

	/**
	 * Gets the relations for source object.
	 *
	 * @param objId the obj id
	 * @return the relations for source object
	 */
	public abstract SLEXMMRelationResultSet getRelationsForSourceObject(int objId);
	
	/**
	 * Gets the relations for target object.
	 *
	 * @param obj the obj
	 * @return the relations for target object
	 */
	public abstract SLEXMMRelationResultSet getRelationsForTargetObject(SLEXMMObject obj);
	
	/**
	 * Gets the relations for target object.
	 *
	 * @param objId the obj id
	 * @return the relations for target object
	 */
	public abstract SLEXMMRelationResultSet getRelationsForTargetObject(int objId);
	
	/**
	 * Gets the relations for source object ordered.
	 *
	 * @param obj the obj
	 * @return the relations for source object ordered
	 */
	public abstract SLEXMMRelationResultSet getRelationsForSourceObjectOrdered(SLEXMMObject obj);

	/**
	 * Gets the relations for source object ordered.
	 *
	 * @param objId the obj id
	 * @return the relations for source object ordered
	 */
	public abstract SLEXMMRelationResultSet getRelationsForSourceObjectOrdered(int objId);
	
	/**
	 * Gets the relations for target object ordered.
	 *
	 * @param obj the obj
	 * @return the relations for target object ordered
	 */
	public abstract SLEXMMRelationResultSet getRelationsForTargetObjectOrdered(SLEXMMObject obj);
	
	/**
	 * Gets the relations for target object ordered.
	 *
	 * @param objId the obj id
	 * @return the relations for target object ordered
	 */
	public abstract SLEXMMRelationResultSet getRelationsForTargetObjectOrdered(int objId);
	
	/**
	 * Gets the objects.
	 *
	 * @return the objects
	 */
	public abstract SLEXMMObjectResultSet getObjects();

	/**
	 * Insert.
	 *
	 * @param cl the cl
	 * @return true, if successful
	 */
	public abstract boolean insert(SLEXMMActivity cl);

	/**
	 * Update.
	 *
	 * @param cl the cl
	 * @return true, if successful
	 */
	public abstract boolean update(SLEXMMActivity cl);

	/**
	 * Insert.
	 *
	 * @param e the e
	 * @return true, if successful
	 */
	public abstract boolean insert(SLEXMMActivityInstance e);

	/**
	 * Update.
	 *
	 * @param e the e
	 * @return true, if successful
	 */
	public abstract boolean update(SLEXMMActivityInstance e);

	/**
	 * Insert.
	 *
	 * @param dm the dm
	 * @return true, if successful
	 */
	public abstract boolean insert(SLEXMMProcess dm);

	/**
	 * Update.
	 *
	 * @param dm the dm
	 * @return true, if successful
	 */
	public abstract boolean update(SLEXMMProcess dm);
	
	/**
	 * Creates the process.
	 *
	 * @param name the name
	 * @return the SLEXMM process
	 */
	public abstract SLEXMMProcess createProcess(String name);
	
	/**
	 * Creates the activity.
	 *
	 * @param processId the process id
	 * @param name the name
	 * @return the SLEXMM activity
	 */
	public abstract SLEXMMActivity createActivity(String name);

	/**
	 * Creates the activity instance.
	 *
	 * @param act the act
	 * @return the SLEXMM activity instance
	 */
	public abstract SLEXMMActivityInstance createActivityInstance(SLEXMMActivity act);
	
	/**
	 * Gets the event for id.
	 *
	 * @param evId the ev id
	 * @return the event for id
	 */
	public abstract SLEXMMEvent getEventForId(int evId);

	/**
	 * Gets the activities.
	 *
	 * @return the activities
	 */
	public abstract SLEXMMActivityResultSet getActivities();

	/**
	 * Gets the classes.
	 *
	 * @return the classes
	 */
	public abstract SLEXMMClassResultSet getClasses();

	
	/**
	 * Gets the object versions.
	 *
	 * @return the object versions
	 */
	public abstract SLEXMMObjectVersionResultSet getObjectVersions();

	/**
	 * Gets the versions related to object version.
	 *
	 * @param ob the ob
	 * @return the versions related to object version
	 */
	public abstract SLEXMMObjectVersionResultSet getVersionsRelatedToObjectVersion(
			SLEXMMObjectVersion ob);

	/**
	 * Gets the versions related to object versions.
	 *
	 * @param verIds the version Ids
	 * @return the versions related to object versions
	 */
	public abstract SLEXMMObjectVersionResultSet getVersionsRelatedToObjectVersions(int[] verIds);

	
	/**
	 * Gets the object per id.
	 *
	 * @param objectId the object id
	 * @return the object per id
	 */
	public abstract SLEXMMObject getObjectPerId(int objectId);

	/**
	 * Adds the event to object version.
	 *
	 * @param ov the ov
	 * @param ev the ev
	 * @param label the label
	 * @return true, if successful
	 */
	public abstract boolean addEventToObjectVersion(
			SLEXMMObjectVersion ov, SLEXMMEvent ev, String label);
	
	/**
	 * Adds the event to object version.
	 *
	 * @param ovId the ov id
	 * @param evId the ev id
	 * @param label the label
	 * @return true, if successful
	 */
	public abstract boolean addEventToObjectVersion(int ovId, int evId, String label);

	/**
	 * Gets the relations.
	 *
	 * @return the relations
	 */
	public abstract SLEXMMRelationResultSet getRelations();

	/**
	 * Gets the relationships.
	 *
	 * @return the relationships
	 */
	public abstract List<SLEXMMRelationship> getRelationships();

	/**
	 * Gets the activity instances.
	 *
	 * @return the activity instances
	 */
	public abstract SLEXMMActivityInstanceResultSet getActivityInstances();
	
	/**
	 * Gets the attributes.
	 *
	 * @return the attributes
	 */
	public abstract SLEXMMAttributeResultSet getAttributes();
	
	/**
	 * Execute sql.
	 *
	 * @param query the query
	 * @return the SLEXMMSQL result set
	 * @throws Exception the exception
	 */
	public abstract SLEXMMSQLResultSet executeSQL(String query) throws Exception;
	
	/**
	 * Gets the event attributes.
	 *
	 * @return the event attributes
	 */
	public abstract SLEXMMEventAttributeResultSet getEventAttributes();

	/**
	 * Gets the objects for event.
	 *
	 * @param eventId the event id
	 * @return the objects for event
	 */
	public abstract SLEXMMObjectResultSet getObjectsForEvent(int eventId);
	
	/**
	 * Gets the objects for events.
	 *
	 * @param eventIds the event ids
	 * @return the objects for events
	 */
	public abstract SLEXMMObjectResultSet getObjectsForEvents(int[] eventIds);
	
	/**
	 * Gets the objects for case.
	 *
	 * @param caseId the case id
	 * @return the objects for case
	 */
	public abstract SLEXMMObjectResultSet getObjectsForCase(int caseId);
	
	/**
	 * Gets the objects for cases.
	 *
	 * @param caseIds the case ids
	 * @return the objects for cases
	 */
	public abstract SLEXMMObjectResultSet getObjectsForCases(int[] caseIds);

	/**
	 * Gets the objects for activity.
	 *
	 * @param activityId the activity id
	 * @return the objects for activity
	 */
	public abstract SLEXMMObjectResultSet getObjectsForActivity(int activityId);
	
	/**
	 * Gets the objects for activities.
	 *
	 * @param activityIds the activity ids
	 * @return the objects for activities
	 */
	public abstract SLEXMMObjectResultSet getObjectsForActivities(int[] activityIds);

	/**
	 * Gets the objects for class.
	 *
	 * @param classId the class id
	 * @return the objects for class
	 */
	public abstract SLEXMMObjectResultSet getObjectsForClass(int classId);
	
	/**
	 * Gets the objects for classes.
	 *
	 * @param classIds the class ids
	 * @return the objects for classes
	 */
	public abstract SLEXMMObjectResultSet getObjectsForClasses(int[] classIds);
	
	/**
	 * Gets the objects for activity instance.
	 *
	 * @param activityInstanceId the activity instance id
	 * @return the objects for activity instance
	 */
	public abstract SLEXMMObjectResultSet getObjectsForActivityInstance(int activityInstanceId);
	
	/**
	 * Gets the objects for activity instances.
	 *
	 * @param activityInstanceIds the activity instance ids
	 * @return the objects for activity instances
	 */
	public abstract SLEXMMObjectResultSet getObjectsForActivityInstances(int[] activityInstanceIds);

	/**
	 * Gets the objects for relation.
	 *
	 * @param relationId the relation id
	 * @return the objects for relation
	 */
	public abstract SLEXMMObjectResultSet getObjectsForRelation(int relationId);
	
	/**
	 * Gets the objects for relations.
	 *
	 * @param relationIds the relation ids
	 * @return the objects for relations
	 */
	public abstract SLEXMMObjectResultSet getObjectsForRelations(int[] relationIds);

	/**
	 * Gets the objects for object version.
	 *
	 * @param objectVersionId the object version id
	 * @return the objects for object version
	 */
	public abstract SLEXMMObjectResultSet getObjectsForObjectVersion(int objectVersionId);
	
	/**
	 * Gets the objects for object versions.
	 *
	 * @param objectVersionIds the object version ids
	 * @return the objects for object versions
	 */
	public abstract SLEXMMObjectResultSet getObjectsForObjectVersions(int[] objectVersionIds);
	
	/**
	 * Gets the objects for relationship.
	 *
	 * @param relationshipId the relationship id
	 * @return the objects for relationship
	 */
	public abstract SLEXMMObjectResultSet getObjectsForRelationship(int relationshipId);
	
	/**
	 * Gets the objects for relationships.
	 *
	 * @param relationshipIds the relationship ids
	 * @return the objects for relationships
	 */
	public abstract SLEXMMObjectResultSet getObjectsForRelationships(int[] relationshipIds);
	
	/**
	 * Gets the objects for attribute.
	 *
	 * @param attributeId the attribute id
	 * @return the objects for attribute
	 */
	public abstract SLEXMMObjectResultSet getObjectsForAttribute(int attributeId);
	
	/**
	 * Gets the objects for attributes.
	 *
	 * @param attributeIds the attribute ids
	 * @return the objects for attributes
	 */
	public abstract SLEXMMObjectResultSet getObjectsForAttributes(int[] attributeIds);

	/**
	 * Gets the cases for log.
	 *
	 * @param logId the log id
	 * @return the cases for log
	 */
	public abstract SLEXMMCaseResultSet getCasesForLog(int logId);
	
	/**
	 * Gets the cases for logs.
	 *
	 * @param logIds the log ids
	 * @return the cases for logs
	 */
	public abstract SLEXMMCaseResultSet getCasesForLogs(int[] logIds);
	
	/**
	 * Gets the cases for process.
	 *
	 * @param processId the process id
	 * @return the cases for process
	 */
	public abstract SLEXMMCaseResultSet getCasesForProcess(int processId);
	
	/**
	 * Gets the cases for process.
	 *
	 * @param processIds the process ids
	 * @return the cases for process
	 */
	public abstract SLEXMMCaseResultSet getCasesForProcess(int[] processIds);
	
	/**
	 * Gets the cases for object.
	 *
	 * @param objectId the object id
	 * @return the cases for object
	 */
	public abstract SLEXMMCaseResultSet getCasesForObject(int objectId);
	
	/**
	 * Gets the cases for objects.
	 *
	 * @param objectIds the object ids
	 * @return the cases for objects
	 */
	public abstract SLEXMMCaseResultSet getCasesForObjects(int[] objectIds);

	/**
	 * Gets the cases for event.
	 *
	 * @param eventId the event id
	 * @return the cases for event
	 */
	public abstract SLEXMMCaseResultSet getCasesForEvent(int eventId);
	
	/**
	 * Gets the cases for events.
	 *
	 * @param eventIds the event ids
	 * @return the cases for events
	 */
	public abstract SLEXMMCaseResultSet getCasesForEvents(int[] eventIds);

	/**
	 * Gets the cases for activity.
	 *
	 * @param activityId the activity id
	 * @return the cases for activity
	 */
	public abstract SLEXMMCaseResultSet getCasesForActivity(int activityId);
	
	/**
	 * Gets the cases for activities.
	 *
	 * @param activityIds the activity ids
	 * @return the cases for activities
	 */
	public abstract SLEXMMCaseResultSet getCasesForActivities(int[] activityIds);

	/**
	 * Gets the cases for class.
	 *
	 * @param classId the class id
	 * @return the cases for class
	 */
	public abstract SLEXMMCaseResultSet getCasesForClass(int classId);
	
	/**
	 * Gets the cases for classes.
	 *
	 * @param classIds the class ids
	 * @return the cases for classes
	 */
	public abstract SLEXMMCaseResultSet getCasesForClasses(int[] classIds);

	/**
	 * Gets the cases for relationship.
	 *
	 * @param RelationshipId the relationship id
	 * @return the cases for relationship
	 */
	public abstract SLEXMMCaseResultSet getCasesForRelationship(int RelationshipId);
	
	/**
	 * Gets the cases for relationships.
	 *
	 * @param RelationshipIds the relationship ids
	 * @return the cases for relationships
	 */
	public abstract SLEXMMCaseResultSet getCasesForRelationships(int[] RelationshipIds);

	/**
	 * Gets the cases for object version.
	 *
	 * @param objectVersionId the object version id
	 * @return the cases for object version
	 */
	public abstract SLEXMMCaseResultSet getCasesForObjectVersion(int objectVersionId);
	
	/**
	 * Gets the cases for object versions.
	 *
	 * @param objectVersionIds the object version ids
	 * @return the cases for object versions
	 */
	public abstract SLEXMMCaseResultSet getCasesForObjectVersions(int[] objectVersionIds);

	/**
	 * Gets the cases for relation.
	 *
	 * @param relationId the relation id
	 * @return the cases for relation
	 */
	public abstract SLEXMMCaseResultSet getCasesForRelation(int relationId);
	
	/**
	 * Gets the cases for relations.
	 *
	 * @param relationIds the relation ids
	 * @return the cases for relations
	 */
	public abstract SLEXMMCaseResultSet getCasesForRelations(int[] relationIds);

	/**
	 * Gets the cases for activity instance.
	 *
	 * @param activityInstanceId the activity instance id
	 * @return the cases for activity instance
	 */
	public abstract SLEXMMCaseResultSet getCasesForActivityInstance(int activityInstanceId);
	
	/**
	 * Gets the cases for activity instances.
	 *
	 * @param activityInstanceIds the activity instance ids
	 * @return the cases for activity instances
	 */
	public abstract SLEXMMCaseResultSet getCasesForActivityInstances(int[] activityInstanceIds);

	/**
	 * Gets the cases for attribute.
	 *
	 * @param attributeId the attribute id
	 * @return the cases for attribute
	 */
	public abstract SLEXMMCaseResultSet getCasesForAttribute(int attributeId);
	
	/**
	 * Gets the cases for attributes.
	 *
	 * @param attributeIds the attribute ids
	 * @return the cases for attributes
	 */
	public abstract SLEXMMCaseResultSet getCasesForAttributes(int[] attributeIds);

	/**
	 * Gets the events for object.
	 *
	 * @param objectId the object id
	 * @return the events for object
	 */
	public abstract SLEXMMEventResultSet getEventsForObject(int objectId);
	
	/**
	 * Gets the events for objects.
	 *
	 * @param objectIds the object ids
	 * @return the events for objects
	 */
	public abstract SLEXMMEventResultSet getEventsForObjects(int[] objectIds);

	/**
	 * Gets the events for case.
	 *
	 * @param caseId the case id
	 * @return the events for case
	 */
	public abstract SLEXMMEventResultSet getEventsForCase(int caseId);
	
	/**
	 * Gets the events for cases.
	 *
	 * @param caseIds the case ids
	 * @return the events for cases
	 */
	public abstract SLEXMMEventResultSet getEventsForCases(int[] caseIds);
	
	/**
	 * Gets the events for activity.
	 *
	 * @param activityId the activity id
	 * @return the events for activity
	 */
	public abstract SLEXMMEventResultSet getEventsForActivity(int activityId);
	
	/**
	 * Gets the events for activities.
	 *
	 * @param activityIds the activity ids
	 * @return the events for activities
	 */
	public abstract SLEXMMEventResultSet getEventsForActivities(int[] activityIds);
	
	/**
	 * Gets the events for class.
	 *
	 * @param classId the class id
	 * @return the events for class
	 */
	public abstract SLEXMMEventResultSet getEventsForClass(int classId);
	
	/**
	 * Gets the events for classes.
	 *
	 * @param classIds the class ids
	 * @return the events for classes
	 */
	public abstract SLEXMMEventResultSet getEventsForClasses(int[] classIds);

	/**
	 * Gets the events for relationship.
	 *
	 * @param relationshipId the relationship id
	 * @return the events for relationship
	 */
	public abstract SLEXMMEventResultSet getEventsForRelationship(int relationshipId);
	
	/**
	 * Gets the events for relationships.
	 *
	 * @param relationshipIds the relationship ids
	 * @return the events for relationships
	 */
	public abstract SLEXMMEventResultSet getEventsForRelationships(int[] relationshipIds);

	/**
	 * Gets the events for object version.
	 *
	 * @param objectVersionId the object version id
	 * @return the events for object version
	 */
	public abstract SLEXMMEventResultSet getEventsForObjectVersion(int objectVersionId);
	
	/**
	 * Gets the events for object versions.
	 *
	 * @param objectVersionIds the object version ids
	 * @return the events for object versions
	 */
	public abstract SLEXMMEventResultSet getEventsForObjectVersions(int[] objectVersionIds);
	
	/**
	 * Gets the events for relation.
	 *
	 * @param relationId the relation id
	 * @return the events for relation
	 */
	public abstract SLEXMMEventResultSet getEventsForRelation(int relationId);
	
	/**
	 * Gets the events for relations.
	 *
	 * @param relationIds the relation ids
	 * @return the events for relations
	 */
	public abstract SLEXMMEventResultSet getEventsForRelations(int[] relationIds);

	/**
	 * Gets the events for activity instance.
	 *
	 * @param activityInstanceId the activity instance id
	 * @return the events for activity instance
	 */
	public abstract SLEXMMEventResultSet getEventsForActivityInstance(int activityInstanceId);
	
	/**
	 * Gets the events for activity instances.
	 *
	 * @param activityInstanceIds the activity instance ids
	 * @return the events for activity instances
	 */
	public abstract SLEXMMEventResultSet getEventsForActivityInstances(int[] activityInstanceIds);
	
	/**
	 * Gets the events for attribute.
	 *
	 * @param attributeId the attribute id
	 * @return the events for attribute
	 */
	public abstract SLEXMMEventResultSet getEventsForAttribute(int attributeId);
	
	/**
	 * Gets the events for attributes.
	 *
	 * @param attributeIds the attribute ids
	 * @return the events for attributes
	 */
	public abstract SLEXMMEventResultSet getEventsForAttributes(int[] attributeIds);
	
	/**
	 * Gets the object versions for object.
	 *
	 * @param objId the obj id
	 * @return the object versions for object
	 */
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForObject(int objId);
	
	/**
	 * Gets the object versions for objects.
	 *
	 * @param objIds the obj ids
	 * @return the object versions for objects
	 */
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForObjects(int[] objIds);
	
	/**
	 * Gets the object versions for event.
	 *
	 * @param eventId the event id
	 * @return the object versions for event
	 */
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForEvent(
			int eventId);
	
	/**
	 * Gets the object versions for events.
	 *
	 * @param eventIds the event ids
	 * @return the object versions for events
	 */
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForEvents(
			int[] eventIds);
	
	/**
	 * Gets the object versions for activity.
	 *
	 * @param activityId the activity id
	 * @return the object versions for activity
	 */
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForActivity(
			int activityId);
	
	/**
	 * Gets the object versions for activities.
	 *
	 * @param activityIds the activity ids
	 * @return the object versions for activities
	 */
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForActivities(
			int[] activityIds);

	/**
	 * Gets the object versions for case.
	 *
	 * @param caseId the case id
	 * @return the object versions for case
	 */
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForCase(int caseId);
	
	/**
	 * Gets the object versions for cases.
	 *
	 * @param caseIds the case ids
	 * @return the object versions for cases
	 */
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForCases(int[] caseIds);

	/**
	 * Gets the object versions for class.
	 *
	 * @param classId the class id
	 * @return the object versions for class
	 */
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForClass(
			int classId);
	
	/**
	 * Gets the object versions for classes.
	 *
	 * @param classIds the class ids
	 * @return the object versions for classes
	 */
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForClasses(
			int[] classIds);

	/**
	 * Gets the object versions for relationship.
	 *
	 * @param relationshipId the relationship id
	 * @return the object versions for relationship
	 */
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForRelationship(
			int relationshipId);
	
	/**
	 * Gets the object versions for relationships.
	 *
	 * @param relationshipIds the relationship ids
	 * @return the object versions for relationships
	 */
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForRelationships(
			int[] relationshipIds);

	/**
	 * Gets the object versions for relation.
	 *
	 * @param relationId the relation id
	 * @return the object versions for relation
	 */
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForRelation(
			int relationId);
	
	/**
	 * Gets the object versions for relations.
	 *
	 * @param relationIds the relation ids
	 * @return the object versions for relations
	 */
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForRelations(
			int[] relationIds);

	/**
	 * Gets the object versions for activity instance.
	 *
	 * @param activityInstanceId the activity instance id
	 * @return the object versions for activity instance
	 */
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForActivityInstance(
			int activityInstanceId);
	
	/**
	 * Gets the object versions for activity instances.
	 *
	 * @param activityInstanceIds the activity instance ids
	 * @return the object versions for activity instances
	 */
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForActivityInstances(
			int[] activityInstanceIds);
	
	/**
	 * Gets the object versions for attribute.
	 *
	 * @param attributeId the attribute id
	 * @return the object versions for attribute
	 */
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForAttribute(
			int attributeId);
	
	/**
	 * Gets the object versions for attributes.
	 *
	 * @param attributeIds the attribute ids
	 * @return the object versions for attributes
	 */
	public abstract SLEXMMObjectVersionResultSet getObjectVersionsForAttributes(
			int[] attributeIds);

	/**
	 * Gets the activities for object.
	 *
	 * @param objectId the object id
	 * @return the activities for object
	 */
	public abstract SLEXMMActivityResultSet getActivitiesForObject(int objectId);
	
	/**
	 * Gets the activities for objects.
	 *
	 * @param objectIds the object ids
	 * @return the activities for objects
	 */
	public abstract SLEXMMActivityResultSet getActivitiesForObjects(int[] objectIds);

	/**
	 * Gets the activities for event.
	 *
	 * @param eventId the event id
	 * @return the activities for event
	 */
	public abstract SLEXMMActivityResultSet getActivitiesForEvent(int eventId);
	
	/**
	 * Gets the activities for events.
	 *
	 * @param eventIds the event ids
	 * @return the activities for events
	 */
	public abstract SLEXMMActivityResultSet getActivitiesForEvents(int[] eventIds);

	/**
	 * Gets the activities for case.
	 *
	 * @param caseId the case id
	 * @return the activities for case
	 */
	public abstract SLEXMMActivityResultSet getActivitiesForCase(int caseId);
	
	/**
	 * Gets the activities for cases.
	 *
	 * @param caseIds the case ids
	 * @return the activities for cases
	 */
	public abstract SLEXMMActivityResultSet getActivitiesForCases(int[] caseIds);

	/**
	 * Gets the activities for class.
	 *
	 * @param classId the class id
	 * @return the activities for class
	 */
	public abstract SLEXMMActivityResultSet getActivitiesForClass(int classId);
	
	/**
	 * Gets the activities for classes.
	 *
	 * @param classIds the class ids
	 * @return the activities for classes
	 */
	public abstract SLEXMMActivityResultSet getActivitiesForClasses(int[] classIds);

	/**
	 * Gets the activities for relationship.
	 *
	 * @param relationshipId the relationship id
	 * @return the activities for relationship
	 */
	public abstract SLEXMMActivityResultSet getActivitiesForRelationship(int relationshipId);
	
	/**
	 * Gets the activities for relationships.
	 *
	 * @param relationshipIds the relationship ids
	 * @return the activities for relationships
	 */
	public abstract SLEXMMActivityResultSet getActivitiesForRelationships(int[] relationshipIds);

	/**
	 * Gets the activities for object version.
	 *
	 * @param objectVersionId the object version id
	 * @return the activities for object version
	 */
	public abstract SLEXMMActivityResultSet getActivitiesForObjectVersion(int objectVersionId);
	
	/**
	 * Gets the activities for object versions.
	 *
	 * @param objectVersionIds the object version ids
	 * @return the activities for object versions
	 */
	public abstract SLEXMMActivityResultSet getActivitiesForObjectVersions(int[] objectVersionIds);

	/**
	 * Gets the activities for relation.
	 *
	 * @param relationId the relation id
	 * @return the activities for relation
	 */
	public abstract SLEXMMActivityResultSet getActivitiesForRelation(int relationId);
	
	/**
	 * Gets the activities for relations.
	 *
	 * @param relationIds the relation ids
	 * @return the activities for relations
	 */
	public abstract SLEXMMActivityResultSet getActivitiesForRelations(int[] relationIds);

	/**
	 * Gets the activities for activity instance.
	 *
	 * @param activityInstanceId the activity instance id
	 * @return the activities for activity instance
	 */
	public abstract SLEXMMActivityResultSet getActivitiesForActivityInstance(
			int activityInstanceId);
	
	/**
	 * Gets the activities for activity instances.
	 *
	 * @param activityInstanceIds the activity instance ids
	 * @return the activities for activity instances
	 */
	public abstract SLEXMMActivityResultSet getActivitiesForActivityInstances(
			int[] activityInstanceIds);

	/**
	 * Gets the activities for attribute.
	 *
	 * @param attributeId the attribute id
	 * @return the activities for attribute
	 */
	public abstract SLEXMMActivityResultSet getActivitiesForAttribute(int attributeId);
	
	/**
	 * Gets the activities for attributes.
	 *
	 * @param attributeIds the attribute ids
	 * @return the activities for attributes
	 */
	public abstract SLEXMMActivityResultSet getActivitiesForAttributes(int[] attributeIds);

	/**
	 * Gets the classes for object.
	 *
	 * @param objectId the object id
	 * @return the classes for object
	 */
	public abstract SLEXMMClassResultSet getClassesForObject(int objectId);
	
	/**
	 * Gets the classes for objects.
	 *
	 * @param objectIds the object ids
	 * @return the classes for objects
	 */
	public abstract SLEXMMClassResultSet getClassesForObjects(int[] objectIds);

	/**
	 * Gets the classes for event.
	 *
	 * @param eventId the event id
	 * @return the classes for event
	 */
	public abstract SLEXMMClassResultSet getClassesForEvent(int eventId);
	
	/**
	 * Gets the classes for events.
	 *
	 * @param eventIds the event ids
	 * @return the classes for events
	 */
	public abstract SLEXMMClassResultSet getClassesForEvents(int[] eventIds);

	/**
	 * Gets the classes for case.
	 *
	 * @param caseId the case id
	 * @return the classes for case
	 */
	public abstract SLEXMMClassResultSet getClassesForCase(int caseId);
	
	/**
	 * Gets the classes for cases.
	 *
	 * @param caseIds the case ids
	 * @return the classes for cases
	 */
	public abstract SLEXMMClassResultSet getClassesForCases(int[] caseIds);

	/**
	 * Gets the classes for activity.
	 *
	 * @param activityId the activity id
	 * @return the classes for activity
	 */
	public abstract SLEXMMClassResultSet getClassesForActivity(int activityId);
	
	/**
	 * Gets the classes for activities.
	 *
	 * @param activityIds the activity ids
	 * @return the classes for activities
	 */
	public abstract SLEXMMClassResultSet getClassesForActivities(int[] activityIds);

	/**
	 * Gets the classes for relationship.
	 *
	 * @param relationshipId the relationship id
	 * @return the classes for relationship
	 */
	public abstract SLEXMMClassResultSet getClassesForRelationship(int relationshipId);
	
	/**
	 * Gets the classes for relationships.
	 *
	 * @param relationshipIds the relationship ids
	 * @return the classes for relationships
	 */
	public abstract SLEXMMClassResultSet getClassesForRelationships(int[] relationshipIds);

	/**
	 * Gets the classes for object version.
	 *
	 * @param objectVersionId the object version id
	 * @return the classes for object version
	 */
	public abstract SLEXMMClassResultSet getClassesForObjectVersion(int objectVersionId);
	
	/**
	 * Gets the classes for object versions.
	 *
	 * @param objectVersionIds the object version ids
	 * @return the classes for object versions
	 */
	public abstract SLEXMMClassResultSet getClassesForObjectVersions(int[] objectVersionIds);

	/**
	 * Gets the classes for relation.
	 *
	 * @param relationId the relation id
	 * @return the classes for relation
	 */
	public abstract SLEXMMClassResultSet getClassesForRelation(int relationId);
	
	/**
	 * Gets the classes for relations.
	 *
	 * @param relationIds the relation ids
	 * @return the classes for relations
	 */
	public abstract SLEXMMClassResultSet getClassesForRelations(int[] relationIds);

	/**
	 * Gets the classes for activity instance.
	 *
	 * @param activityInstanceId the activity instance id
	 * @return the classes for activity instance
	 */
	public abstract SLEXMMClassResultSet getClassesForActivityInstance(int activityInstanceId);
	
	/**
	 * Gets the classes for activity instances.
	 *
	 * @param activityInstanceIds the activity instance ids
	 * @return the classes for activity instances
	 */
	public abstract SLEXMMClassResultSet getClassesForActivityInstances(int[] activityInstanceIds);

	/**
	 * Gets the classes for attribute.
	 *
	 * @param attributeId the attribute id
	 * @return the classes for attribute
	 */
	public abstract SLEXMMClassResultSet getClassesForAttribute(int attributeId);
	
	/**
	 * Gets the classes for attributes.
	 *
	 * @param attributeIds the attribute ids
	 * @return the classes for attributes
	 */
	public abstract SLEXMMClassResultSet getClassesForAttributes(int[] attributeIds);

	/**
	 * Gets the relations for object.
	 *
	 * @param objectId the object id
	 * @return the relations for object
	 */
	public abstract SLEXMMRelationResultSet getRelationsForObject(int objectId);
	
	/**
	 * Gets the relations for objects.
	 *
	 * @param objectIds the object ids
	 * @return the relations for objects
	 */
	public abstract SLEXMMRelationResultSet getRelationsForObjects(int[] objectIds);

	/**
	 * Gets the relations for event.
	 *
	 * @param eventId the event id
	 * @return the relations for event
	 */
	public abstract SLEXMMRelationResultSet getRelationsForEvent(int eventId);
	
	/**
	 * Gets the relations for events.
	 *
	 * @param eventIds the event ids
	 * @return the relations for events
	 */
	public abstract SLEXMMRelationResultSet getRelationsForEvents(int[] eventIds);

	/**
	 * Gets the relations for case.
	 *
	 * @param caseId the case id
	 * @return the relations for case
	 */
	public abstract SLEXMMRelationResultSet getRelationsForCase(int caseId);
	
	/**
	 * Gets the relations for cases.
	 *
	 * @param caseIds the case ids
	 * @return the relations for cases
	 */
	public abstract SLEXMMRelationResultSet getRelationsForCases(int[] caseIds);

	/**
	 * Gets the relations for activity.
	 *
	 * @param activityId the activity id
	 * @return the relations for activity
	 */
	public abstract SLEXMMRelationResultSet getRelationsForActivity(int activityId);
	
	/**
	 * Gets the relations for activities.
	 *
	 * @param activityIds the activity ids
	 * @return the relations for activities
	 */
	public abstract SLEXMMRelationResultSet getRelationsForActivities(int[] activityIds);

	/**
	 * Gets the relations for class.
	 *
	 * @param classId the class id
	 * @return the relations for class
	 */
	public abstract SLEXMMRelationResultSet getRelationsForClass(int classId);
	
	/**
	 * Gets the relations for classes.
	 *
	 * @param classIds the class ids
	 * @return the relations for classes
	 */
	public abstract SLEXMMRelationResultSet getRelationsForClasses(int[] classIds);

	/**
	 * Gets the relations for relationship.
	 *
	 * @param relationshipId the relationship id
	 * @return the relations for relationship
	 */
	public abstract SLEXMMRelationResultSet getRelationsForRelationship(int relationshipId);
	
	/**
	 * Gets the relations for relationships.
	 *
	 * @param relationshipIds the relationship ids
	 * @return the relations for relationships
	 */
	public abstract SLEXMMRelationResultSet getRelationsForRelationships(int[] relationshipIds);

	/**
	 * Gets the relations for object version.
	 *
	 * @param objectVersionId the object version id
	 * @return the relations for object version
	 */
	public abstract SLEXMMRelationResultSet getRelationsForObjectVersion(int objectVersionId);
	
	/**
	 * Gets the relations for object versions.
	 *
	 * @param objectVersionIds the object version ids
	 * @return the relations for object versions
	 */
	public abstract SLEXMMRelationResultSet getRelationsForObjectVersions(int[] objectVersionIds);

	/**
	 * Gets the relations for activity instance.
	 *
	 * @param activityInstanceId the activity instance id
	 * @return the relations for activity instance
	 */
	public abstract SLEXMMRelationResultSet getRelationsForActivityInstance(
			int activityInstanceId);
	
	/**
	 * Gets the relations for activity instances.
	 *
	 * @param activityInstanceIds the activity instance ids
	 * @return the relations for activity instances
	 */
	public abstract SLEXMMRelationResultSet getRelationsForActivityInstances(
			int[] activityInstanceIds);

	/**
	 * Gets the relations for attribute.
	 *
	 * @param attributeId the attribute id
	 * @return the relations for attribute
	 */
	public abstract SLEXMMRelationResultSet getRelationsForAttribute(int attributeId);
	
	/**
	 * Gets the relations for attributes.
	 *
	 * @param attributeIds the attribute ids
	 * @return the relations for attributes
	 */
	public abstract SLEXMMRelationResultSet getRelationsForAttributes(int[] attributeIds);

	/**
	 * Gets the relationships for object.
	 *
	 * @param objectId the object id
	 * @return the relationships for object
	 */
	public abstract SLEXMMRelationshipResultSet getRelationshipsForObject(int objectId);
	
	/**
	 * Gets the relationships for objects.
	 *
	 * @param objectIds the object ids
	 * @return the relationships for objects
	 */
	public abstract SLEXMMRelationshipResultSet getRelationshipsForObjects(int[] objectIds);

	/**
	 * Gets the relationships for event.
	 *
	 * @param eventId the event id
	 * @return the relationships for event
	 */
	public abstract SLEXMMRelationshipResultSet getRelationshipsForEvent(int eventId);
	
	/**
	 * Gets the relationships for events.
	 *
	 * @param eventIds the event ids
	 * @return the relationships for events
	 */
	public abstract SLEXMMRelationshipResultSet getRelationshipsForEvents(int[] eventIds);

	/**
	 * Gets the relationships for case.
	 *
	 * @param caseId the case id
	 * @return the relationships for case
	 */
	public abstract SLEXMMRelationshipResultSet getRelationshipsForCase(int caseId);
	
	/**
	 * Gets the relationships for cases.
	 *
	 * @param caseIds the case ids
	 * @return the relationships for cases
	 */
	public abstract SLEXMMRelationshipResultSet getRelationshipsForCases(int[] caseIds);

	/**
	 * Gets the relationships for activity.
	 *
	 * @param activityId the activity id
	 * @return the relationships for activity
	 */
	public abstract SLEXMMRelationshipResultSet getRelationshipsForActivity(
			int activityId);
	
	/**
	 * Gets the relationships for activities.
	 *
	 * @param activityIds the activity ids
	 * @return the relationships for activities
	 */
	public abstract SLEXMMRelationshipResultSet getRelationshipsForActivities(
			int[] activityIds);

	/**
	 * Gets the relationships for class.
	 *
	 * @param classId the class id
	 * @return the relationships for class
	 */
	public abstract SLEXMMRelationshipResultSet getRelationshipsForClass(int classId);
	
	/**
	 * Gets the relationships for classes.
	 *
	 * @param classIds the class ids
	 * @return the relationships for classes
	 */
	public abstract SLEXMMRelationshipResultSet getRelationshipsForClasses(int[] classIds);

	/**
	 * Gets the relationships for object version.
	 *
	 * @param objectVersionId the object version id
	 * @return the relationships for object version
	 */
	public abstract SLEXMMRelationshipResultSet getRelationshipsForObjectVersion(
			int objectVersionId);
	
	/**
	 * Gets the relationships for object versions.
	 *
	 * @param objectVersionIds the object version ids
	 * @return the relationships for object versions
	 */
	public abstract SLEXMMRelationshipResultSet getRelationshipsForObjectVersions(
			int[] objectVersionIds);

	/**
	 * Gets the relationships for relation.
	 *
	 * @param relationId the relation id
	 * @return the relationships for relation
	 */
	public abstract SLEXMMRelationshipResultSet getRelationshipsForRelation(
			int relationId);
	
	/**
	 * Gets the relationships for relations.
	 *
	 * @param relationIds the relation ids
	 * @return the relationships for relations
	 */
	public abstract SLEXMMRelationshipResultSet getRelationshipsForRelations(
			int[] relationIds);

	/**
	 * Gets the relationships for activity instance.
	 *
	 * @param activityInstanceId the activity instance id
	 * @return the relationships for activity instance
	 */
	public abstract SLEXMMRelationshipResultSet getRelationshipsForActivityInstance(
			int activityInstanceId);
	
	/**
	 * Gets the relationships for activity instances.
	 *
	 * @param activityInstanceIds the activity instance ids
	 * @return the relationships for activity instances
	 */
	public abstract SLEXMMRelationshipResultSet getRelationshipsForActivityInstances(
			int[] activityInstanceIds);

	/**
	 * Gets the relationships for attribute.
	 *
	 * @param attributeId the attribute id
	 * @return the relationships for attribute
	 */
	public abstract SLEXMMRelationshipResultSet getRelationshipsForAttribute(
			int attributeId);
	
	/**
	 * Gets the relationships for attributes.
	 *
	 * @param attributeIds the attribute ids
	 * @return the relationships for attributes
	 */
	public abstract SLEXMMRelationshipResultSet getRelationshipsForAttributes(
			int[] attributeIds);

	/**
	 * Gets the activity instances for object.
	 *
	 * @param objectId the object id
	 * @return the activity instances for object
	 */
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForObject(
			int objectId);
	
	/**
	 * Gets the activity instances for objects.
	 *
	 * @param objectIds the object ids
	 * @return the activity instances for objects
	 */
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForObjects(
			int[] objectIds);

	/**
	 * Gets the activity instances for event.
	 *
	 * @param eventId the event id
	 * @return the activity instances for event
	 */
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForEvent(
			int eventId);
	
	/**
	 * Gets the activity instances for events.
	 *
	 * @param eventIds the event ids
	 * @return the activity instances for events
	 */
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForEvents(
			int[] eventIds);

	/**
	 * Gets the activity instances for case.
	 *
	 * @param caseId the case id
	 * @return the activity instances for case
	 */
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForCase(
			int caseId);
	
	/**
	 * Gets the activity instances for cases.
	 *
	 * @param caseIds the case ids
	 * @return the activity instances for cases
	 */
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForCases(
			int[] caseIds);

	/**
	 * Gets the activity instances for activity.
	 *
	 * @param activityId the activity id
	 * @return the activity instances for activity
	 */
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForActivity(
			int activityId);
	
	/**
	 * Gets the activity instances for activities.
	 *
	 * @param activityIds the activity ids
	 * @return the activity instances for activities
	 */
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForActivities(
			int[] activityIds);

	/**
	 * Gets the activity instances for class.
	 *
	 * @param classId the class id
	 * @return the activity instances for class
	 */
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForClass(
			int classId);
	
	/**
	 * Gets the activity instances for classes.
	 *
	 * @param classIds the class ids
	 * @return the activity instances for classes
	 */
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForClasses(
			int[] classIds);

	/**
	 * Gets the activity instances for relationship.
	 *
	 * @param relationshipId the relationship id
	 * @return the activity instances for relationship
	 */
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForRelationship(
			int relationshipId);
	
	/**
	 * Gets the activity instances for relationships.
	 *
	 * @param relationshipIds the relationship ids
	 * @return the activity instances for relationships
	 */
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForRelationships(
			int[] relationshipIds);

	/**
	 * Gets the activity instances for object version.
	 *
	 * @param objectVersionId the object version id
	 * @return the activity instances for object version
	 */
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForObjectVersion(
			int objectVersionId);
	
	/**
	 * Gets the activity instances for object versions.
	 *
	 * @param objectVersionIds the object version ids
	 * @return the activity instances for object versions
	 */
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForObjectVersions(
			int[] objectVersionIds);

	/**
	 * Gets the activity instances for relation.
	 *
	 * @param relationId the relation id
	 * @return the activity instances for relation
	 */
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForRelation(
			int relationId);
	
	/**
	 * Gets the activity instances for relations.
	 *
	 * @param relationIds the relation ids
	 * @return the activity instances for relations
	 */
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForRelations(
			int[] relationIds);

	/**
	 * Gets the activity instances for attribute.
	 *
	 * @param attributeId the attribute id
	 * @return the activity instances for attribute
	 */
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForAttribute(
			int attributeId);
	
	/**
	 * Gets the activity instances for attributes.
	 *
	 * @param attributeIds the attribute ids
	 * @return the activity instances for attributes
	 */
	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForAttributes(
			int[] attributeIds);

	/**
	 * Gets the attributes for object.
	 *
	 * @param objectId the object id
	 * @return the attributes for object
	 */
	public abstract SLEXMMAttributeResultSet getAttributesForObject(int objectId);
	
	/**
	 * Gets the attributes for objects.
	 *
	 * @param objectIds the object ids
	 * @return the attributes for objects
	 */
	public abstract SLEXMMAttributeResultSet getAttributesForObjects(int[] objectIds);

	/**
	 * Gets the attributes for event.
	 *
	 * @param eventId the event id
	 * @return the attributes for event
	 */
	public abstract SLEXMMAttributeResultSet getAttributesForEvent(int eventId);
	
	/**
	 * Gets the attributes for events.
	 *
	 * @param eventIds the event ids
	 * @return the attributes for events
	 */
	public abstract SLEXMMAttributeResultSet getAttributesForEvents(int[] eventIds);

	/**
	 * Gets the attributes for case.
	 *
	 * @param caseId the case id
	 * @return the attributes for case
	 */
	public abstract SLEXMMAttributeResultSet getAttributesForCase(int caseId);
	
	/**
	 * Gets the attributes for cases.
	 *
	 * @param caseIds the case ids
	 * @return the attributes for cases
	 */
	public abstract SLEXMMAttributeResultSet getAttributesForCases(int[] caseIds);

	/**
	 * Gets the attributes for activity.
	 *
	 * @param activityId the activity id
	 * @return the attributes for activity
	 */
	public abstract SLEXMMAttributeResultSet getAttributesForActivity(int activityId);
	
	/**
	 * Gets the attributes for activities.
	 *
	 * @param activityIds the activity ids
	 * @return the attributes for activities
	 */
	public abstract SLEXMMAttributeResultSet getAttributesForActivities(int[] activityIds);

	/**
	 * Gets the attributes for class.
	 *
	 * @param classId the class id
	 * @return the attributes for class
	 */
	public abstract SLEXMMAttributeResultSet getAttributesForClass(int classId);
	
	/**
	 * Gets the attributes for classes.
	 *
	 * @param classIds the class ids
	 * @return the attributes for classes
	 */
	public abstract SLEXMMAttributeResultSet getAttributesForClasses(int[] classIds);

	/**
	 * Gets the attributes for relationship.
	 *
	 * @param relationshipId the relationship id
	 * @return the attributes for relationship
	 */
	public abstract SLEXMMAttributeResultSet getAttributesForRelationship(
			int relationshipId);
	
	/**
	 * Gets the attributes for relationships.
	 *
	 * @param relationshipIds the relationship ids
	 * @return the attributes for relationships
	 */
	public abstract SLEXMMAttributeResultSet getAttributesForRelationships(
			int[] relationshipIds);

	/**
	 * Gets the attributes for object version.
	 *
	 * @param objectVersionId the object version id
	 * @return the attributes for object version
	 */
	public abstract SLEXMMAttributeResultSet getAttributesForObjectVersion(
			int objectVersionId);
	
	/**
	 * Gets the attributes for object versions.
	 *
	 * @param objectVersionIds the object version ids
	 * @return the attributes for object versions
	 */
	public abstract SLEXMMAttributeResultSet getAttributesForObjectVersions(
			int[] objectVersionIds);

	/**
	 * Gets the attributes for relation.
	 *
	 * @param relationId the relation id
	 * @return the attributes for relation
	 */
	public abstract SLEXMMAttributeResultSet getAttributesForRelation(int relationId);
	
	/**
	 * Gets the attributes for relations.
	 *
	 * @param relationIds the relation ids
	 * @return the attributes for relations
	 */
	public abstract SLEXMMAttributeResultSet getAttributesForRelations(int[] relationIds);

	/**
	 * Gets the attributes for activity instance.
	 *
	 * @param activityInstanceId the activity instance id
	 * @return the attributes for activity instance
	 */
	public abstract SLEXMMAttributeResultSet getAttributesForActivityInstance(
			int activityInstanceId);
	
	/**
	 * Gets the attributes for activity instances.
	 *
	 * @param activityInstanceIds the activity instance ids
	 * @return the attributes for activity instances
	 */
	public abstract SLEXMMAttributeResultSet getAttributesForActivityInstances(
			int[] activityInstanceIds);

	/**
	 * Gets the periods for objects.
	 *
	 * @param objectIds the object ids
	 * @return the periods for objects
	 */
	public abstract SLEXMMPeriodResultSet getPeriodsForObjects(int[] objectIds);
	
	/**
	 * Insert.
	 *
	 * @param log the log
	 * @return true, if successful
	 */
	public abstract boolean insert(SLEXMMLog log);

	/**
	 * Update.
	 *
	 * @param log the log
	 * @return true, if successful
	 */
	public abstract boolean update(SLEXMMLog log);
	
	/**
	 * Adds the case to log.
	 *
	 * @param logId the log id
	 * @param caseId the case id
	 * @return true, if successful
	 */
	public abstract boolean addCaseToLog(int logId, int caseId);
	
	/**
	 * Adds the activity to process.
	 *
	 * @param processId the process id
	 * @param actId the act id
	 * @return true, if successful
	 */
	public abstract boolean addActivityToProcess(int processId, int actId);

	public abstract boolean insert(SLEXMMCaseAttribute at);

	public abstract boolean update(SLEXMMCaseAttribute at);

	public abstract boolean update(SLEXMMCaseAttributeValue at);

	public abstract boolean insert(SLEXMMCaseAttributeValue at);

	public abstract HashMap<SLEXMMCaseAttribute, SLEXMMCaseAttributeValue> getAttributeValuesForCase(
			SLEXMMCase slexmmCase);
	
	public abstract HashMap<SLEXMMCaseAttribute, SLEXMMCaseAttributeValue> getAttributeValuesForCase(
			int caseId);

	public abstract boolean update(SLEXMMLogAttributeValue at);

	public abstract boolean insert(SLEXMMLogAttributeValue at);

	public abstract boolean update(SLEXMMLogAttribute at);

	public abstract boolean insert(SLEXMMLogAttribute at);

	public abstract HashMap<SLEXMMLogAttribute, SLEXMMLogAttributeValue> getAttributeValuesForLog(SLEXMMLog slexmmLog);
	
	public abstract HashMap<SLEXMMLogAttribute, SLEXMMLogAttributeValue> getAttributeValuesForLog(int logId);

	public abstract List<SLEXMMClassifierAttribute> getListAttributesForClassifier(SLEXMMClassifier slexmmClassifier);
	
	public abstract List<SLEXMMClassifierAttribute> getListAttributesForClassifier(int classifierId);

	public abstract boolean insert(SLEXMMClassifier cl);

	public abstract boolean update(SLEXMMClassifier cl);

	public abstract boolean insert(SLEXMMClassifierAttribute at);

	public abstract boolean update(SLEXMMClassifierAttribute at);

	public abstract SLEXMMLogResultSet getLogsForProcess(int id);

	public abstract SLEXMMActivityResultSet getActivitiesForProcess(int id);
	
	public abstract SLEXMMLogResultSet getLogs();
	
	public abstract SLEXMMProcessResultSet getProcesses();

	public abstract SLEXMMObjectResultSet getObjectsForDatamodels(int[] is);

	public abstract SLEXMMObjectResultSet getObjectsForLogs(int[] is);

	public abstract SLEXMMObjectResultSet getObjectsForProcesses(int[] is);

	public abstract SLEXMMCaseResultSet getCasesForDatamodels(int[] is);

	public abstract SLEXMMCaseResultSet getCasesForProcesses(int[] is);

	public abstract SLEXMMEventResultSet getEventsForDatamodels(int[] is);

	public abstract SLEXMMEventResultSet getEventsForLogs(int[] is);

	public abstract SLEXMMEventResultSet getEventsForProcesses(int[] is);

	public abstract SLEXMMObjectVersionResultSet getVersionsForDatamodels(int[] is);

	public abstract SLEXMMObjectVersionResultSet getVersionsForLogs(int[] is);

	public abstract SLEXMMObjectVersionResultSet getVersionsForProcesses(int[] is);

	public abstract SLEXMMActivityResultSet getActivitiesForDatamodels(int[] is);

	public abstract SLEXMMActivityResultSet getActivitiesForLogs(int[] is);

	public abstract SLEXMMActivityResultSet getActivitiesForProcesses(int[] is);

	public abstract SLEXMMClassResultSet getClassesForDatamodels(int[] is);

	public abstract SLEXMMClassResultSet getClassesForLogs(int[] is);

	public abstract SLEXMMClassResultSet getClassesForProcesses(int[] is);

	public abstract SLEXMMRelationResultSet getRelationsForDatamodels(int[] is);

	public abstract SLEXMMRelationResultSet getRelationsForLogs(int[] is);

	public abstract SLEXMMRelationResultSet getRelationsForProcesses(int[] is);

	public abstract SLEXMMRelationshipResultSet getRelationshipsForDatamodels(int[] is);

	public abstract SLEXMMRelationshipResultSet getRelationshipsForLogs(int[] is);

	public abstract SLEXMMRelationshipResultSet getRelationshipsForProcesses(int[] is);

	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForDatamodels(int[] is);

	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForLogs(int[] is);

	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForProcesses(int[] is);

	public abstract SLEXMMAttributeResultSet getAttributesForDatamodels(int[] is);

	public abstract SLEXMMAttributeResultSet getAttributesForLogs(int[] is);

	public abstract SLEXMMAttributeResultSet getAttributesForProcesses(int[] is);

	public abstract SLEXMMDataModelResultSet getDatamodelsForObjects(int[] is);

	public abstract SLEXMMDataModelResultSet getDatamodelsForEvents(int[] is);

	public abstract SLEXMMDataModelResultSet getDatamodelsForCases(int[] is);

	public abstract SLEXMMDataModelResultSet getDatamodelsForActivities(int[] is);

	public abstract SLEXMMDataModelResultSet getDatamodelsForClasses(int[] is);

	public abstract SLEXMMDataModelResultSet getDatamodelsForRelationships(int[] is);

	public abstract SLEXMMDataModelResultSet getDatamodelsForObjectVersions(int[] is);

	public abstract SLEXMMDataModelResultSet getDatamodelsForRelations(int[] is);

	public abstract SLEXMMDataModelResultSet getDatamodelsForActivityInstances(int[] is);

	public abstract SLEXMMDataModelResultSet getDatamodelsForAttributes(int[] is);

	public abstract SLEXMMDataModelResultSet getDatamodelsForLogs(int[] is);

	public abstract SLEXMMDataModelResultSet getDatamodelsForProcesses(int[] is);

	public abstract SLEXMMProcessResultSet getProcessesForObjects(int[] is);

	public abstract SLEXMMProcessResultSet getProcessesForEvents(int[] is);

	public abstract SLEXMMProcessResultSet getProcessesForCases(int[] is);

	public abstract SLEXMMProcessResultSet getProcessesForActivities(int[] is);

	public abstract SLEXMMProcessResultSet getProcessesForClasses(int[] is);

	public abstract SLEXMMProcessResultSet getProcessesForRelationships(int[] is);

	public abstract SLEXMMProcessResultSet getProcessesForObjectVersions(int[] is);

	public abstract SLEXMMProcessResultSet getProcessesForRelations(int[] is);

	public abstract SLEXMMProcessResultSet getProcessesForActivityInstances(int[] is);

	public abstract SLEXMMProcessResultSet getProcessesForAttributes(int[] is);

	public abstract SLEXMMProcessResultSet getProcessesForDatamodels(int[] is);

	public abstract SLEXMMProcessResultSet getProcessesForLogs(int[] is);

	public abstract SLEXMMLogResultSet getLogsForObjects(int[] is);

	public abstract SLEXMMLogResultSet getLogsForEvents(int[] is);

	public abstract SLEXMMLogResultSet getLogsForCases(int[] is);

	public abstract SLEXMMLogResultSet getLogsForActivities(int[] is);

	public abstract SLEXMMLogResultSet getLogsForClasses(int[] is);

	public abstract SLEXMMLogResultSet getLogsForRelationships(int[] is);

	public abstract SLEXMMLogResultSet getLogsForObjectVersions(int[] is);

	public abstract SLEXMMLogResultSet getLogsForRelations(int[] is);

	public abstract SLEXMMLogResultSet getLogsForActivityInstances(int[] is);

	public abstract SLEXMMLogResultSet getLogsForAttributes(int[] is);

	public abstract SLEXMMLogResultSet getLogsForDatamodels(int[] is);

	public abstract SLEXMMLogResultSet getLogsForProcesses(int[] is);

	public abstract SLEXMMObjectResultSet getObjectsForPeriod(SLEXMMPeriod p);

	public abstract SLEXMMCaseResultSet getCasesForPeriod(SLEXMMPeriod p);

	public abstract SLEXMMEventResultSet getEventsForPeriod(SLEXMMPeriod p);

	public abstract SLEXMMObjectVersionResultSet getVersionsForPeriod(SLEXMMPeriod p);

	public abstract SLEXMMActivityResultSet getActivitiesForPeriod(SLEXMMPeriod p);

	public abstract SLEXMMClassResultSet getClassesForPeriod(SLEXMMPeriod p);

	public abstract SLEXMMRelationResultSet getRelationsForPeriod(SLEXMMPeriod p);

	public abstract SLEXMMRelationshipResultSet getRelationshipsForPeriod(SLEXMMPeriod p);

	public abstract SLEXMMActivityInstanceResultSet getActivityInstancesForPeriod(SLEXMMPeriod p);

	public abstract SLEXMMAttributeResultSet getAttributesForPeriod(SLEXMMPeriod p);

	public abstract SLEXMMDataModelResultSet getDatamodelsForPeriod(SLEXMMPeriod p);

	public abstract SLEXMMProcessResultSet getProcessesForPeriod(SLEXMMPeriod p);

	public abstract SLEXMMLogResultSet getLogsForPeriod(SLEXMMPeriod p);

	public abstract SLEXMMPeriodResultSet getPeriodsForEvents(int[] is);

	public abstract SLEXMMPeriodResultSet getPeriodsForCases(int[] is);

	public abstract SLEXMMPeriodResultSet getPeriodsForActivities(int[] is);

	public abstract SLEXMMPeriodResultSet getPeriodsForClasses(int[] is);

	public abstract SLEXMMPeriodResultSet getPeriodsForRelationships(int[] is);

	public abstract SLEXMMPeriodResultSet getPeriodsForVersions(int[] is);

	public abstract SLEXMMPeriodResultSet getPeriodsForRelations(int[] is);

	public abstract SLEXMMPeriodResultSet getPeriodsForActivityInstances(int[] is);

	public abstract SLEXMMPeriodResultSet getPeriodsForAttributes(int[] is);

	public abstract SLEXMMPeriodResultSet getPeriodsForDatamodels(int[] is);

	public abstract SLEXMMPeriodResultSet getPeriodsForLogs(int[] is);

	public abstract SLEXMMPeriodResultSet getPeriodsForProcesses(int[] is);

	public abstract SLEXMMLog createLog(int processId, String name);
	
	public abstract SLEXMMEventResultSet getEventsAndAttributeValues(Set<SLEXMMEvent> set);
	
	public abstract SLEXMMEventResultSet getEventsAndAttributeValues(int[] ids);
	
	public abstract SLEXMMObjectVersionResultSet getVersionsAndAttributeValues(Set<SLEXMMObjectVersion> set);
	
	public abstract SLEXMMObjectVersionResultSet getVersionsAndAttributeValues(int[] ids);
	
	public abstract SLEXMMCaseResultSet getCasesAndAttributeValues(Set<SLEXMMCase> set);
	
	public abstract SLEXMMCaseResultSet getCasesAndAttributeValues(int[] ids);
	
	public abstract SLEXMMLogResultSet getLogsAndAttributeValues(Set<SLEXMMLog> set);
	
	public abstract SLEXMMLogResultSet getLogsAndAttributeValues(int[] ids);
	
}
