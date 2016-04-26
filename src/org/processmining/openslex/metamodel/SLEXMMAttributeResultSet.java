/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMAttributeResultSet.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMAttributeResultSet extends SLEXMMAbstractResultSetObject {
	
	/**
	 * Instantiates a new SLEXMM attribute result set.
	 *
	 * @param storage the storage
	 * @param rset the rset
	 */
	protected SLEXMMAttributeResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset);
	}
	
	/**
	 * Gets the next.
	 *
	 * @return the next
	 */
	public SLEXMMAttribute getNext() {
		SLEXMMAttribute ai = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				int id = this.rset.getInt("id");
				int classId = this.rset.getInt("class_id");
				String name = this.rset.getString("name");
				ai = new SLEXMMAttribute((SLEXMMStorageMetaModel) storage);
				ai.setId(id);
				ai.setClassId(classId);
				ai.setName(name);
				ai.setDirty(false);
				ai.setInserted(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ai == null) {
				close();
			}
		}
		return ai;
	}
	
}
