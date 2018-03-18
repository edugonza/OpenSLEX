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
 * @param <T>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public abstract class AbstractRSetElement<T> {
	
	/** The rset. */
	protected ResultSet rset = null;
	
	/** The storage. */
	protected SLEXMMStorageMetaModel storage = null;
	
	protected Class<T> typeClass = null;
	
	protected Class<T> getTypeClass() {
		return typeClass;
	}
			
	public abstract T getNext();
	
	/**
	 * Instantiates a new SLEXMM abstract result set object.
	 *
	 * @param storage the storage
	 * @param rset the rset
	 */
	protected AbstractRSetElement(SLEXMMStorageMetaModel storage, ResultSet rset, Class<T> type) {
		this.storage = storage;
		this.rset = rset;
		this.typeClass = type;
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
				//e.printStackTrace();
			}
		}
	}
	
//	/**
//	 * Gets the origin id.
//	 *
//	 * @return the origin id
//	 */
//	public Integer getOriginId() {
//		
//		Integer originId = null;
//		
//		if (this.rset != null) {
//			try {
//				originId = this.rset.getInt("originIdQuery");
//			} catch (SQLException e) {
//				//e.printStackTrace();
//			}
//		}
//		
//		return originId;
//	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}
}
