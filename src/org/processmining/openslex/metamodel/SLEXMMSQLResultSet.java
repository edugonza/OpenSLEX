package org.processmining.openslex.metamodel;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class SLEXMMSQLResultSet extends SLEXMMAbstractResultSetObject {
	
	private ResultSetMetaData metadata = null;
	private int numColumns = 0;
	private String[] columnNames = null;
	private int rowCount = 0;
	
	protected SLEXMMSQLResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
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
	
	public String[] getColumnNames() {
		return this.columnNames;
	}
	
	public int getRowCount() {
		return this.rowCount;
	}
	
	public SLEXMMSQLResult getNext() {
		SLEXMMSQLResult r = null;
		try {
			if (this.rset != null && this.rset.next()) {
				
				String[] values = new String[numColumns];
				
				for (int i = 1; i <= numColumns; i++) {
					values[i-1] = this.rset.getString(i);
				}
				
				r = new SLEXMMSQLResult((SLEXMMStorageMetaModel) storage, values, columnNames);
				
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
