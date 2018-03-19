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
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (super.equals(obj)) {
			return true;
		} else {
			if (obj instanceof SLEXMMNode) {
				return ((SLEXMMNode) obj).getTable().equals(this.getTable());
			}
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return table.hashCode();
	}
}
