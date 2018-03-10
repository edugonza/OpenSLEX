/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMActivityResultSet.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMActivityResultSet extends AbstractRSetElement<SLEXMMActivity> {
	
	/**
	 * Instantiates a new SLEXMM activity result set.
	 *
	 * @param storage the storage
	 * @param rset the rset
	 */
	public SLEXMMActivityResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset, SLEXMMActivity.class);
	}
	
	/**
	 * Gets the next.
	 *
	 * @return the next
	 */
	public SLEXMMActivity getNext() {
		SLEXMMActivity ac = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				int id = this.rset.getInt("id");
				String name = this.rset.getString("name");
				ac = storage.getFromCache(SLEXMMActivity.class, id);
				if (ac == null) {
					ac = new SLEXMMActivity((SLEXMMStorageMetaModel) storage, name);
					ac.setId(id);
					ac.setDirty(false);
					ac.setInserted(true);
					storage.putInCache(ac);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ac == null) {
				close();
			}
		}
		return ac;
	}
	
}
