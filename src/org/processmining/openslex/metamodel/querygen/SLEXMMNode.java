package org.processmining.openslex.metamodel.querygen;

public class SLEXMMNode {
	
	private SLEXMMTables table = null;
	
	public SLEXMMNode(SLEXMMTables table) {
		this.table = table;
	}
	
	public SLEXMMTables getTable() {
		return this.table;
	}
	
	public String getName() {
		return this.table.toString();
	}
	
}
