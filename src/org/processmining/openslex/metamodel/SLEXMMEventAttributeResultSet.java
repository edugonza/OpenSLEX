package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

public class SLEXMMEventAttributeResultSet extends SLEXMMAbstractResultSetObject {
	
	protected SLEXMMEventAttributeResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset);
	}
	
	public SLEXMMEventAttribute getNext() {
		SLEXMMEventAttribute ai = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				int id = this.rset.getInt("id");
				String name = this.rset.getString("name");
				ai = new SLEXMMEventAttribute((SLEXMMStorageMetaModel) storage);
				ai.setId(id);
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
