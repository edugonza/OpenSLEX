/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMSQLResultSet.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMSQLResultSet extends SLEXMMAbstractResultSetObject {
	
	/** The metadata. */
	private ResultSetMetaData metadata = null;
	
	/** The num columns. */
	private int numColumns = 0;
	
	/** The column names. */
	private String[] columnNames = null;
	
	/** The row count. */
	private int rowCount = 0;
	
	/**
	 * Instantiates a new SLEXMMSQL result set.
	 *
	 * @param storage the storage
	 * @param rset the rset
	 */
	public SLEXMMSQLResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset);
		
		try {
			
			metadata = this.rset.getMetaData();
			
			numColumns = metadata.getColumnCount();
			
			columnNames = new String[numColumns];
			
			rowCount = this.rset.getFetchSize();
			
			for (int i = 1; i <= numColumns; i++) {
				columnNames[i-1] = metadata.getColumnName(i);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the column names.
	 *
	 * @return the column names
	 */
	public String[] getColumnNames() {
		return this.columnNames;
	}
	
	/**
	 * Gets the row count.
	 *
	 * @return the row count
	 */
	public int getRowCount() {
		return this.rowCount;
	}
	
	/**
	 * Gets the next.
	 *
	 * @return the next
	 */
	public SLEXMMSQLResult getNext() {
		SLEXMMSQLResult r = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				String[] values = new String[numColumns];
				
				for (int i = 1; i <= numColumns; i++) {
					values[i-1] = this.rset.getString(i);
				}
				
				r = new SLEXMMSQLResult(values, columnNames);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (r == null) {
				close();
			}
		}
		return r;
	}
	
}
