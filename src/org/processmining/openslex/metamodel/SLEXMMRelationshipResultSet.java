package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

public class SLEXMMRelationshipResultSet extends SLEXMMAbstractResultSetObject {
	
	
	
	protected SLEXMMRelationshipResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset);
	}
	
	public SLEXMMRelationship getNext() {
		SLEXMMRelationship rs = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				int id = this.rset.getInt("id");
				int sourceId = this.rset.getInt("source");
				int targetId = this.rset.getInt("target");
				String name = this.rset.getString("name");
				rs = new SLEXMMRelationship((SLEXMMStorageMetaModel) storage);
				rs.setId(id);
				rs.setName(name);
				rs.setSourceClassId(sourceId);
				rs.setTargetClassId(targetId);
				rs.setDirty(false);
				rs.setInserted(true);
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
