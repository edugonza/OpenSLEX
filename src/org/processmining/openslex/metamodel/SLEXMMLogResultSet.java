package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

public class SLEXMMLogResultSet extends SLEXMMAbstractResultSetObject {
	
	protected SLEXMMLogResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset);
	}
	
	public SLEXMMLog getNext() {
		SLEXMMLog p = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				String name = this.rset.getString("name");
				int id = this.rset.getInt("id");
				int collectionId = this.rset.getInt("collection_id");
				int processId = this.rset.getInt("process_id");
				p = new SLEXMMLog((SLEXMMStorageMetaModel) storage);
				p.setCollectionId(collectionId);
				p.setId(id);
				p.setName(name);
				p.setProcessId(processId);
				p.setDirty(false);
				p.setInserted(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (p == null) {
				close();
			}
		}
		return p;
	}
	
}
