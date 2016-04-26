/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

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
	protected SLEXMMLogResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
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
	
}
