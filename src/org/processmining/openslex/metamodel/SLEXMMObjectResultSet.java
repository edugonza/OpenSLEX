package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

public class SLEXMMObjectResultSet extends SLEXMMAbstractResultSetObject {
	
	protected SLEXMMObjectResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset);
	}
	
	public SLEXMMObject getNext() {
		SLEXMMObject ev = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				int id = this.rset.getInt("id");
				int classId = this.rset.getInt("class_id");
				ev = new SLEXMMObject((SLEXMMStorageMetaModel) storage);
				ev.setId(id);
				ev.setClassId(classId);
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
