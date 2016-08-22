/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMCaseResultSet.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMCaseResultSet extends SLEXMMAbstractResultSetObject {
	
	/**
	 * Instantiates a new SLEXMM case result set.
	 *
	 * @param storage the storage
	 * @param rset the rset
	 */
	public SLEXMMCaseResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset);
	}
	
	/**
	 * Gets the next.
	 *
	 * @return the next
	 */
	public SLEXMMCase getNext() {
		SLEXMMCase t = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				int id = this.rset.getInt("id");
				String name = this.rset.getString("name");
				t = new SLEXMMCase((SLEXMMStorageMetaModel) storage);
				t.setId(id);
				t.setName(name);
				t.setDirty(false);
				t.setInserted(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (t == null) {
				close();
			}
		}
		return t;
	}
	
	private boolean doNext = true;
	
	public SLEXMMCase getNextWithAttributes() {
		SLEXMMCase c = null;
		try {
			boolean resNext = true;
			
			if (doNext) {
				resNext = this.rset.next();
			}
			
			if (this.rset != null && resNext) {
				
				int id = this.rset.getInt("id");
				String name = this.rset.getString("name");
				c = new SLEXMMCase((SLEXMMStorageMetaModel) storage);
				c.setId(id);
				c.setName(name);
				c.setDirty(false);
				c.setInserted(true);
							
				/**/
				HashMap<SLEXMMCaseAttribute, SLEXMMCaseAttributeValue> attributeValues = null;
				try {
					boolean stop = false;
					attributeValues = new HashMap<>();
					while (!stop) {
						int idaux = this.rset.getInt("id");
						if (idaux == id) {

							SLEXMMCaseAttribute at = new SLEXMMCaseAttribute((SLEXMMStorageMetaModel) storage);
							at.setId(rset.getInt("atId"));
							at.setName(rset.getString("atName"));
							at.setDirty(false);
							at.setInserted(true);

							SLEXMMCaseAttributeValue atv = new SLEXMMCaseAttributeValue(
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
				c.setAttributeValues(attributeValues);
				c.setDirty(false);
				c.setInserted(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c == null) {
				close();
			}
		}
		return c;
	}
	
}
