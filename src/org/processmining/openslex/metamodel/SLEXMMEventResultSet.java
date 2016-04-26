/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMEventResultSet.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMEventResultSet extends SLEXMMAbstractResultSetObject {
	
	
	
	/**
	 * Instantiates a new SLEXMM event result set.
	 *
	 * @param storage the storage
	 * @param rset the rset
	 */
	protected SLEXMMEventResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset);
	}
	
	/**
	 * Gets the next.
	 *
	 * @return the next
	 */
	public SLEXMMEvent getNext() {
		SLEXMMEvent ev = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				int id = this.rset.getInt("id");
				int activityInstanceId = this.rset.getInt("activity_instance_id");
				int order = this.rset.getInt("ordering");
				String lifecycle = this.rset.getString("lifecycle");
				String resource = this.rset.getString("resource");
				long timestamp = this.rset.getLong("timestamp");
				ev = new SLEXMMEvent((SLEXMMStorageMetaModel) storage);
				ev.setId(id);
				ev.setActivityInstanceId(activityInstanceId);
				ev.setOrder(order);
				ev.setTimestamp(timestamp);
				ev.setLifecycle(lifecycle);
				ev.setResource(resource);
				//ev.retrieveAttributeValues();
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
