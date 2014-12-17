package org.processmining.openslex;

import java.sql.ResultSet;

public class SLEXPerspectiveResultSet extends SLEXAbstractResultSetObject {
	
	protected SLEXPerspectiveResultSet(SLEXStoragePerspective storage, ResultSet rset) {
		super(storage, rset);
	}
	
	public SLEXPerspective getNext() {
		SLEXPerspective p = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				String name = this.rset.getString("name");
				int id = this.rset.getInt("id");
				int collectionId = this.rset.getInt("collectionID");
				String collectionFileName = this.rset.getString("collectionFileName");
				p = new SLEXPerspective((SLEXStoragePerspective) storage);
				p.setCollectionId(collectionId);
				p.setId(id);
				p.setName(name);
				p.setCollectionFileName(collectionFileName);
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
