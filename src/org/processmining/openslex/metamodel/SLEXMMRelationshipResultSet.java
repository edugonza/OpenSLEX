/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMRelationshipResultSet.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMRelationshipResultSet extends AbstractRSetElement<SLEXMMRelationship> {
	
	
	
	/**
	 * Instantiates a new SLEXMM relationship result set.
	 *
	 * @param storage the storage
	 * @param rset the rset
	 */
	public SLEXMMRelationshipResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset, SLEXMMRelationship.class);
	}
	
	/**
	 * Gets the next.
	 *
	 * @return the next
	 */
	public SLEXMMRelationship getNext() {
		SLEXMMRelationship rs = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				int id = this.rset.getInt("id");
				int sourceId = this.rset.getInt("source");
				int targetId = this.rset.getInt("target");
				String name = this.rset.getString("name");
				rs = storage.getFromCache(SLEXMMRelationship.class, id);
				if (rs == null) {
					rs = new SLEXMMRelationship((SLEXMMStorageMetaModel) storage);
					rs.setId(id);
					rs.setName(name);
					rs.setSourceClassId(sourceId);
					rs.setTargetClassId(targetId);
					rs.setDirty(false);
					rs.setInserted(true);
					storage.putInCache(rs);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs == null) {
				close();
			}
		}
		return rs;
	}
	
}
