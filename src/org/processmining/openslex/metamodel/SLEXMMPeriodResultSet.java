package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

public class SLEXMMPeriodResultSet extends SLEXMMAbstractResultSetObject {
	
	protected SLEXMMPeriodResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset);
	}
	
	public SLEXMMPeriod getNext() {
		SLEXMMPeriod p = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				long startTimestamp = this.rset.getLong("start");
				long end1Timestamp = this.rset.getLong("end");
				long end2Timestamp = this.rset.getLong("end2");
				long endTimestamp = end1Timestamp;
				if (end2Timestamp == -1) {
					endTimestamp = end2Timestamp;
				}
				p = new SLEXMMPeriod(startTimestamp,endTimestamp);
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
