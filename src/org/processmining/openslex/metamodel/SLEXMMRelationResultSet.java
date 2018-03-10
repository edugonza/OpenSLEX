/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMRelationResultSet.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMRelationResultSet extends AbstractRSetElement<SLEXMMRelation> {
	
	
	
	/**
	 * Instantiates a new SLEXMM relation result set.
	 *
	 * @param storage the storage
	 * @param rset the rset
	 */
	public SLEXMMRelationResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset, SLEXMMRelation.class);
	}
	
	/**
	 * Gets the next.
	 *
	 * @return the next
	 */
	public SLEXMMRelation getNext() {
		SLEXMMRelation ev = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				int id = this.rset.getInt("id");
				int sourceObjectId = this.rset.getInt("source_object_version_id");
				int targetObjectId = this.rset.getInt("target_object_version_id");
				int relationshipId = this.rset.getInt("relationship_id");
				long startTimestamp = this.rset.getLong("start_timestamp");
				long endTimestamp = this.rset.getLong("end_timestamp");
				ev = storage.getFromCache(SLEXMMRelation.class, id);
				if (ev == null) {
					ev = new SLEXMMRelation((SLEXMMStorageMetaModel) storage);
					ev.setId(id);
					ev.setSourceObjectVersionId(sourceObjectId);
					ev.setTargetObjectVersionId(targetObjectId);
					ev.setRelationshipId(relationshipId);
					ev.setStartTimestamp(startTimestamp);
					ev.setEndTimestamp(endTimestamp);
					ev.setDirty(false);
					ev.setInserted(true);
					storage.putInCache(ev);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ev == null) {
				close();
			}
		}
		return ev;
	}
	
}
