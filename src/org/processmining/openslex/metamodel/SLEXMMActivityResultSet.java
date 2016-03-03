package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

public class SLEXMMActivityResultSet extends SLEXMMAbstractResultSetObject {
	
	protected SLEXMMActivityResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset);
	}
	
	public SLEXMMActivity getNext() {
		SLEXMMActivity ac = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				int id = this.rset.getInt("id");
				//int processId = this.rset.getInt("process_id");
				String name = this.rset.getString("name");
				ac = new SLEXMMActivity((SLEXMMStorageMetaModel) storage, name/*, processId*/);
				ac.setId(id);
				ac.setDirty(false);
				ac.setInserted(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ac == null) {
				close();
			}
		}
		return ac;
	}
	
}
