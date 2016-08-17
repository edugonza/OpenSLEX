/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

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
	public SLEXMMObjectVersionResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
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
				long startTimestamp = this.rset.getLong("start_timestamp");
				long endTimestamp = this.rset.getLong("end_timestamp");
				ev = new SLEXMMObjectVersion((SLEXMMStorageMetaModel) storage);
				ev.setId(id);
				ev.setObjectId(objectId);
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
	
	private boolean doNext = true;
	
	public SLEXMMObjectVersion getNextWithAttributes() {
		SLEXMMObjectVersion ev = null;
		try {
			boolean resNext = true;
			
			if (doNext) {
				resNext = this.rset.next();
			}
			
			if (this.rset != null && resNext) {
				
				int id = this.rset.getInt("id");
				int objectId = this.rset.getInt("object_id");
				long startTimestamp = this.rset.getLong("start_timestamp");
				long endTimestamp = this.rset.getLong("end_timestamp");
				ev = new SLEXMMObjectVersion((SLEXMMStorageMetaModel) storage);
				ev.setId(id);
				ev.setObjectId(objectId);
				ev.setStartTimestamp(startTimestamp);
				ev.setEndTimestamp(endTimestamp);
				ev.setDirty(false);
				ev.setInserted(true);
							
				/**/
				HashMap<SLEXMMAttribute, SLEXMMAttributeValue> attributeValues = null;
				try {
					boolean stop = false;
					attributeValues = new HashMap<>();
					while (!stop) {
						int idaux = this.rset.getInt("id");
						if (idaux == id) {

							SLEXMMAttribute at = new SLEXMMAttribute((SLEXMMStorageMetaModel) storage);
							at.setId(rset.getInt("atId"));
							at.setName(rset.getString("atName"));
							at.setDirty(false);
							at.setInserted(true);

							SLEXMMAttributeValue atv = new SLEXMMAttributeValue(
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
