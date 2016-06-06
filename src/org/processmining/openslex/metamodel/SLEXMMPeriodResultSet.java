/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMPeriodResultSet.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMPeriodResultSet extends SLEXMMAbstractResultSetObject {
	
	/**
	 * Instantiates a new SLEXMM period result set.
	 *
	 * @param storage the storage
	 * @param rset the rset
	 */
	public SLEXMMPeriodResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset);
	}
	
	/**
	 * Gets the next.
	 *
	 * @return the next
	 */
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
