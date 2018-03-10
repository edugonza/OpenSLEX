/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMActivityInstanceResultSet.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMActivityInstanceResultSet extends AbstractRSetElement<SLEXMMActivityInstance> {
	
	/**
	 * Instantiates a new SLEXMM activity instance result set.
	 *
	 * @param storage the storage
	 * @param rset the rset
	 */
	public SLEXMMActivityInstanceResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset, SLEXMMActivityInstance.class);
	}
	
	/**
	 * Gets the next.
	 *
	 * @return the next
	 */
	public SLEXMMActivityInstance getNext() {
		SLEXMMActivityInstance ai = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				int id = this.rset.getInt("id");
				int activityId = this.rset.getInt("activity_id");
				ai = storage.getFromCache(SLEXMMActivityInstance.class, id);
				if (ai == null) {
					ai = new SLEXMMActivityInstance((SLEXMMStorageMetaModel) storage);
					ai.setId(id);
					ai.setActivityId(activityId);
					ai.setDirty(false);
					ai.setInserted(true);
					storage.putInCache(ai);
				}
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
