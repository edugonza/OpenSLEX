package org.processmining.openslex.metamodel;

public class SLEXMMSQLResult {
	
	private String[] values = null;
	private String[] columnNames = null;
	
	protected SLEXMMSQLResult(String[] values, String[] columnNames) {
		this.values = values;
		this.columnNames = columnNames;
	}
	
	public String[] getValues() {
		return this.values;
	}
	
	public String[] getColumnNames() {
		return this.columnNames;
	}
	
}
