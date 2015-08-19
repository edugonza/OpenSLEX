package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

public class SLEXMMEventResultSet extends SLEXMMAbstractResultSetObject {
	
	
	
	protected SLEXMMEventResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset);
	}
	
	public SLEXMMEvent getNext() {
		SLEXMMEvent ev = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				int id = this.rset.getInt("id");
				int collectionId = this.rset.getInt("collection_id");
				int order = this.rset.getInt("ordering");
				ev = new SLEXMMEvent((SLEXMMStorageMetaModel) storage);
				ev.setId(id);
				ev.setCollectionId(collectionId);
				ev.setOrder(order);
				ev.retrieveAttributeValues();
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