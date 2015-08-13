package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

public class SLEXMMDataModelResultSet extends SLEXMMAbstractResultSetObject {
	
	protected SLEXMMDataModelResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset);
	}
	
	public SLEXMMDataModel getNext() {
		SLEXMMDataModel dm = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				String name = this.rset.getString("name");
				int id = this.rset.getInt("id");
				dm = new SLEXMMDataModel(storage);
				dm.setName(name);
				dm.setId(id);
				dm.setDirty(false);
				dm.setInserted(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dm == null) {
				close();
			}
		}
		return dm;
	}
	
}
