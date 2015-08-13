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
				int relationshipd = this.rset.getInt("relationship_id");
				int sourceObjectId = this.rset.getInt("source_object_id");
				int targetObjectId = this.rset.getInt("target_object_id");
				int startSourceObjectVersionId = this.rset.getInt("start_source_object_version_id");
				int endSourceObjectVersionId = this.rset.getInt("end_source_object_version_id");
				int startTargetObjectVersionId = this.rset.getInt("start_target_object_version_id");
				int endTargetObjectVersiontId = this.rset.getInt("end_target_object_version_id");
				int eventId = this.rset.getInt("event_id");
				ev = new SLEXMMRelation((SLEXMMStorageMetaModel) storage);
				ev.setId(id);
				ev.setRelationshipId(relationshipd);
				ev.setSourceObjectId(sourceObjectId);
				ev.setTargetObjectId(targetObjectId);
				ev.setStartSourceObjectVersionId(startSourceObjectVersionId);
				ev.setEndSourceObjectVersionId(endSourceObjectVersionId);
				ev.setStartTargetObjectVersionId(startTargetObjectVersionId);
				ev.setEndTargetObjectVersionId(endTargetObjectVersiontId);
				ev.setEventId(eventId);
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
