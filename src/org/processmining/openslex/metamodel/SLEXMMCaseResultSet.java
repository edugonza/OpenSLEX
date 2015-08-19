package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

public class SLEXMMCaseResultSet extends SLEXMMAbstractResultSetObject {
	
	protected SLEXMMCaseResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset);
	}
	
	public SLEXMMCase getNext() {
		SLEXMMCase t = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				int id = this.rset.getInt("id");
				int logId = this.rset.getInt("log_id");
				t = new SLEXMMCase((SLEXMMStorageMetaModel) storage);
				t.setLogId(logId);
				t.setId(id);
				t.setDirty(false);
				t.setInserted(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (t == null) {
				close();
			}
		}
		return t;
	}
	
}