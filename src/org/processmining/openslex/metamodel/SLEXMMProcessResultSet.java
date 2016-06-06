/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMProcessResultSet.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMProcessResultSet extends SLEXMMAbstractResultSetObject {
	
	/**
	 * Instantiates a new SLEXMM process result set.
	 *
	 * @param storage the storage
	 * @param rset the rset
	 */
	public SLEXMMProcessResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset);
	}
	
	/**
	 * Gets the next.
	 *
	 * @return the next
	 */
	public SLEXMMProcess getNext() {
		SLEXMMProcess proc = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				int id = this.rset.getInt("id");
				String name = this.rset.getString("name");
				proc = new SLEXMMProcess((SLEXMMStorageMetaModel) storage);
				proc.setId(id);
				proc.setName(name);
				proc.setDirty(false);
				proc.setInserted(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (proc == null) {
				close();
			}
		}
		return proc;
	}
	
}
