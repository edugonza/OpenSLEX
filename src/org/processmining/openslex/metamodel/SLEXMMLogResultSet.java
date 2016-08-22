/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMLogResultSet.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMLogResultSet extends SLEXMMAbstractResultSetObject {
	
	/**
	 * Instantiates a new SLEXMM log result set.
	 *
	 * @param storage the storage
	 * @param rset the rset
	 */
	public SLEXMMLogResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset);
	}
	
	/**
	 * Gets the next.
	 *
	 * @return the next
	 */
	public SLEXMMLog getNext() {
		SLEXMMLog log = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				int id = this.rset.getInt("id");
				String name = this.rset.getString("name");
				int process_id = this.rset.getInt("process_id");
				log = new SLEXMMLog((SLEXMMStorageMetaModel) storage);
				log.setId(id);
				log.setName(name);
				log.setProcessId(process_id);
				log.setDirty(false);
				log.setInserted(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (log == null) {
				close();
			}
		}
		return log;
	}
	
	private boolean doNext = true;
	
	public SLEXMMLog getNextWithAttributes() {
		SLEXMMLog log = null;
		try {
			boolean resNext = true;
			
			if (doNext) {
				resNext = this.rset.next();
			}
			
			if (this.rset != null && resNext) {
				
				int id = this.rset.getInt("id");
				String name = this.rset.getString("name");
				int process_id = this.rset.getInt("process_id");
				log = new SLEXMMLog((SLEXMMStorageMetaModel) storage);
				log.setId(id);
				log.setName(name);
				log.setProcessId(process_id);
				log.setDirty(false);
				log.setInserted(true);
							
				/**/
				HashMap<SLEXMMLogAttribute, SLEXMMLogAttributeValue> attributeValues = null;
				try {
					boolean stop = false;
					attributeValues = new HashMap<>();
					while (!stop) {
						int idaux = this.rset.getInt("id");
						if (idaux == id) {

							SLEXMMLogAttribute at = new SLEXMMLogAttribute((SLEXMMStorageMetaModel) storage);
							at.setId(rset.getInt("atId"));
							at.setName(rset.getString("atName"));
							at.setDirty(false);
							at.setInserted(true);

							SLEXMMLogAttributeValue atv = new SLEXMMLogAttributeValue(
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
				log.setAttributeValues(attributeValues);
				log.setDirty(false);
				log.setInserted(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (log == null) {
				close();
			}
		}
		return log;
	}
}
