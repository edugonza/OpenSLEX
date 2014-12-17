package org.processmining.openslex;

import java.util.Hashtable;
import java.util.List;

public interface SLEXStorageCollection extends SLEXStorage {

	public abstract SLEXEventCollection createEventCollection(String name);
	
	public abstract SLEXEvent createEvent(int collectionId);

	public abstract SLEXClass findClass(String name);

	public abstract SLEXAttribute findAttribute(String className, String name);

	public abstract SLEXAttribute findOrCreateAttribute(String className,
			String name, boolean common);

	public abstract SLEXAttribute createAttribute(String className,
			String name, boolean common);

	public abstract SLEXAttributeValue createAttributeValue(int attributeId,
			int eventId, String value);

	public abstract SLEXClass createClass(String name, boolean common);

	public abstract SLEXEventCollectionResultSet getEventCollections();

	public abstract SLEXEventResultSet getEventsOfCollectionBetweenDatesOrderedBy(
			SLEXEventCollection ec, List<SLEXAttribute> orderAttributes,
			String startDate, String endDate);

	public abstract SLEXEventCollection getEventCollection(int id);
	
	public abstract Hashtable<SLEXAttribute, SLEXAttributeValue> getAttributeValuesForEvent(
			SLEXEvent slexEvent);

	public abstract boolean insert(SLEXEventCollection ec);

	public abstract boolean update(SLEXEventCollection ec);

	public abstract SLEXEventResultSet getEventsOfCollection(
			SLEXEventCollection slexEventCollection);

	public abstract SLEXEventResultSet getEventsOfCollectionOrderedBy(
			SLEXEventCollection slexEventCollection,
			List<SLEXAttribute> orderAttributes);

	public abstract boolean insert(SLEXClass cl);

	public abstract boolean update(SLEXClass cl);

	public abstract boolean insert(SLEXEvent e);

	public abstract boolean update(SLEXEvent e);

	public abstract boolean insert(SLEXAttribute at);

	public abstract boolean update(SLEXAttribute at);

	public abstract boolean insert(SLEXAttributeValue at);

	public abstract boolean update(SLEXAttributeValue at);

}
