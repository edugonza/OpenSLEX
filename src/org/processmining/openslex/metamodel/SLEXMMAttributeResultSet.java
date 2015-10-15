package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

public class SLEXMMAttributeResultSet extends SLEXMMAbstractResultSetObject {
	
	protected SLEXMMAttributeResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset);
	}
	
	public SLEXMMAttribute getNext() {
		SLEXMMAttribute ai = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				int id = this.rset.getInt("id");
				int classId = this.rset.getInt("class_id");
				String name = this.rset.getString("name");
				ai = new SLEXMMAttribute((SLEXMMStorageMetaModel) storage);
				ai.setId(id);
				ai.setClassId(classId);
				ai.setName(name);
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
