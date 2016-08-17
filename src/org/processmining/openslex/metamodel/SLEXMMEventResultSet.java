/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

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
	public SLEXMMEventResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
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
	
	private boolean doNext = true;
	
	public SLEXMMEvent getNextWithAttributes() {
		SLEXMMEvent ev = null;
		try {
			boolean resNext = true;
			
			if (doNext) {
				resNext = this.rset.next();
			}
			
			if (this.rset != null && resNext) {
				
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
							
				/**/
				HashMap<SLEXMMEventAttribute, SLEXMMEventAttributeValue> attributeValues = null;
				try {
					boolean stop = false;
					attributeValues = new HashMap<>();
					while (!stop) {
						int idaux = this.rset.getInt("id");
						if (idaux == id) {

							SLEXMMEventAttribute at = new SLEXMMEventAttribute((SLEXMMStorageMetaModel) storage);
							at.setId(rset.getInt("atId"));
							at.setName(rset.getString("atName"));
							at.setDirty(false);
							at.setInserted(true);

							SLEXMMEventAttributeValue atv = new SLEXMMEventAttributeValue(
									(SLEXMMStorageMetaModel) storage, at.getId(), id);
							atv.setValue(rset.getString("atValue"));
							atv.setType(rset.getString("atType"));
							atv.setDirty(false);
							atv.setInserted(true);
							attributeValues.put(at, atv);

							if (!this.rset.next()) {
								stop = true;
								doNext = true;
							}
						} else {
							stop = true;
							doNext = false;
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				/**/
				ev.setAttributeValues(attributeValues);
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
