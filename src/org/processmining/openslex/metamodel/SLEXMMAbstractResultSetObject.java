package org.processmining.openslex.metamodel;

import java.sql.ResultSet;
import java.sql.SQLException;

abstract class SLEXMMAbstractResultSetObject {
	
	protected ResultSet rset = null;
	protected SLEXMMStorage storage = null;
	
	protected SLEXMMAbstractResultSetObject(SLEXMMStorage storage, ResultSet rset) {
		this.storage = storage;
		this.rset = rset;
	}
	
	public void close() {
		if (rset != null) {
			try {
				storage.closeStatement(rset.getStatement());
				storage.closeResultSet(rset);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public Integer getOriginId() {
		
		Integer originId = null;
		
		if (this.rset != null) {
			try {
				originId = this.rset.getInt("originIdQuery");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return originId;
	}
	
	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}
}
