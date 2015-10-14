package org.processmining.openslex.metamodel;

public class SLEXMMSQLResult {
	
	private SLEXMMStorageMetaModel strg = null;
	private String[] values = null;
	private String[] columnNames = null;
	
	protected SLEXMMSQLResult(SLEXMMStorageMetaModel strg, String[] values, String[] columnNames) {
		this.strg = strg;
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
