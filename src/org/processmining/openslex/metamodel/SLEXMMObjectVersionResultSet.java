/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMObjectVersionResultSet.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMObjectVersionResultSet extends SLEXMMAbstractResultSetObject {
	
	
	
	/**
	 * Instantiates a new SLEXMM object version result set.
	 *
	 * @param storage the storage
	 * @param rset the rset
	 */
	protected SLEXMMObjectVersionResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset);
	}
	
	/**
	 * Gets the next.
	 *
	 * @return the next
	 */
	public SLEXMMObjectVersion getNext() {
		SLEXMMObjectVersion ev = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				int id = this.rset.getInt("id");
				int objectId = this.rset.getInt("object_id");
//				int eventId = this.rset.getInt("event_id");
//				String eventLabel = this.rset.getString("event_label");
				long startTimestamp = this.rset.getLong("start_timestamp");
				long endTimestamp = this.rset.getLong("end_timestamp");
				ev = new SLEXMMObjectVersion((SLEXMMStorageMetaModel) storage);
				ev.setId(id);
				ev.setObjectId(objectId);
//				ev.setEventId(eventId);
//				ev.setEventLabel(eventLabel);
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
