package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

public class SLEXMMRelationResultSet extends SLEXMMAbstractResultSetObject {
	
	
	
	protected SLEXMMRelationResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset);
	}
	
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
				ev = new SLEXMMRelation((SLEXMMStorageMetaModel) storage);
				ev.setId(id);
				ev.setSourceObjectVersionId(sourceObjectId);
				ev.setTargetObjectVersionId(targetObjectId);
				ev.setRelationshipId(relationshipId);
				ev.setStartTimestamp(startTimestamp);
				ev.setEndTimestamp(endTimestamp);
				ev.setDirty(false);
				ev.setInserted(true);
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
