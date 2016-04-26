/*
 * 
 */
package org.processmining.openslex.metamodel;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMSQLResult.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMSQLResult {
	
	/** The values. */
	private String[] values = null;
	
	/** The column names. */
	private String[] columnNames = null;
	
	/**
	 * Instantiates a new SLEXMMSQL result.
	 *
	 * @param values the values
	 * @param columnNames the column names
	 */
	protected SLEXMMSQLResult(String[] values, String[] columnNames) {
		this.values = values;
		this.columnNames = columnNames;
	}
	
	/**
	 * Gets the values.
	 *
	 * @return the values
	 */
	public String[] getValues() {
		return this.values;
	}
	
	/**
	 * Gets the column names.
	 *
	 * @return the column names
	 */
	public String[] getColumnNames() {
		return this.columnNames;
	}
	
}
