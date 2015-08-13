package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

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
				ev = new SLEXMMObjectVersion((SLEXMMStorageMetaModel) storage);
				ev.setId(id);
				ev.setObjectId(objectId);
				ev.setEventId(eventId);
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
