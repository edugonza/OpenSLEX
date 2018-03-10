/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMLogAttributeResultSet.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMLogAttributeResultSet extends AbstractRSetElement<SLEXMMLogAttribute> {
	
	/**
	 * Instantiates a new SLEXMM log attribute result set.
	 *
	 * @param storage the storage
	 * @param rset the rset
	 */
	public SLEXMMLogAttributeResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset, SLEXMMLogAttribute.class);
	}
	
	/**
	 * Gets the next.
	 *
	 * @return the next
	 */
	public SLEXMMLogAttribute getNext() {
		SLEXMMLogAttribute ai = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				int id = this.rset.getInt("id");
				String name = this.rset.getString("name");
				ai = storage.getFromCache(SLEXMMLogAttribute.class, id);
				if (ai == null) {
					ai = new SLEXMMLogAttribute((SLEXMMStorageMetaModel) storage);
					ai.setId(id);
					ai.setName(name);
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
