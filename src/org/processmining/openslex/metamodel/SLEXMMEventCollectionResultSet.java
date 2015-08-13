package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

public class SLEXMMEventCollectionResultSet extends SLEXMMAbstractResultSetObject {
	
	protected SLEXMMEventCollectionResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset);
	}
	
	public SLEXMMEventCollection getNext() {
		SLEXMMEventCollection ec = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				String name = this.rset.getString("name");
				int id = this.rset.getInt("id");
				ec = new SLEXMMEventCollection((SLEXMMStorageMetaModel) storage, name);
				ec.setId(id);
				ec.setDirty(false);
				ec.setInserted(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ec == null) {
				close();
			}
		}
		return ec;
	}
	
}
