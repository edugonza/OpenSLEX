package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

public class SLEXMMActivityInstanceResultSet extends SLEXMMAbstractResultSetObject {
	
	protected SLEXMMActivityInstanceResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset);
	}
	
	public SLEXMMActivityInstance getNext() {
		SLEXMMActivityInstance ai = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				int id = this.rset.getInt("id");
				int activityId = this.rset.getInt("activity_id");
				ai = new SLEXMMActivityInstance((SLEXMMStorageMetaModel) storage);
				ai.setId(id);
				ai.setActivityId(activityId);
				ai.setDirty(false);
				ai.setInserted(true);
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
