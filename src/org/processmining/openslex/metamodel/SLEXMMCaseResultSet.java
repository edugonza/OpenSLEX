/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

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
	
}
