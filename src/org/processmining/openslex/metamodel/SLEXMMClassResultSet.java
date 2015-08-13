package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

public class SLEXMMClassResultSet extends SLEXMMAbstractResultSetObject {
	
	protected SLEXMMClassResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset);
	}
	
	public SLEXMMClass getNext() {
		SLEXMMClass cl = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				int id = this.rset.getInt("id");
				int dataModelId = this.rset.getInt("datamodel_id");
				String name = this.rset.getString("name");
				cl = new SLEXMMClass((SLEXMMStorageMetaModel)storage,name,dataModelId);
				cl.setId(id);
				cl.retrieveAttributesAndKeys();
				cl.setDirty(false);
				cl.setInserted(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cl == null) {
				close();
			}
		}
		return cl;
	}
	
}
