package org.processmining.openslex.metamodel;

import java.sql.ResultSet;
import java.util.Date;

public class SLEXMMObjectVersionResultSet extends SLEXMMAbstractResultSetObject {
	
	
	
	protected SLEXMMObjectVersionResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset);
	}
	
	public SLEXMMObjectVersion getNext() {
		SLEXMMObjectVersion ev = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				int id = this.rset.getInt("id");
				int objectId = this.rset.getInt("object_id");
				int eventId = this.rset.getInt("event_id");
				String eventLabel = this.rset.getString("event_label");
				Date startTimestamp = this.rset.getDate("start_timestamp");
				Date endTimestamp = this.rset.getDate("end_timestamp");
				ev = new SLEXMMObjectVersion((SLEXMMStorageMetaModel) storage);
				ev.setId(id);
				ev.setObjectId(objectId);
				ev.setEventId(eventId);
				ev.setEventLabel(eventLabel);
				ev.setStartTimestamp(startTimestamp);
				ev.setEndTimestamp(endTimestamp);
				ev.setDirty(false);
				ev.setInserted(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ev == null) {
				close();
			}
		}
		return ev;
	}
	
}
