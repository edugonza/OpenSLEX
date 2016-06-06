/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.sql.ResultSet;
import java.sql.SQLException;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMAbstractResultSetObject.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
abstract class SLEXMMAbstractResultSetObject {
	
	/** The rset. */
	protected ResultSet rset = null;
	
	/** The storage. */
	protected SLEXMMStorage storage = null;
	
	/**
	 * Instantiates a new SLEXMM abstract result set object.
	 *
	 * @param storage the storage
	 * @param rset the rset
	 */
	protected SLEXMMAbstractResultSetObject(SLEXMMStorage storage, ResultSet rset) {
		this.storage = storage;
		this.rset = rset;
	}
	
	/**
	 * Close.
	 */
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
	
	/**
	 * Gets the origin id.
	 *
	 * @return the origin id
	 */
	public Integer getOriginId() {
		
		Integer originId = null;
		
		if (this.rset != null) {
			try {
				originId = this.rset.getInt("originIdQuery");
			} catch (SQLException e) {
				//e.printStackTrace();
			}
		}
		
		return originId;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}
}
