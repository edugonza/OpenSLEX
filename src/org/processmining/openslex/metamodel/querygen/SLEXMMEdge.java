package org.processmining.openslex.metamodel.querygen;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SLEXMMEdge extends DefaultWeightedEdge {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4608000824013747092L;
	private SLEXMMNode nodeSource = null;
	private SLEXMMNode nodeTarget = null;
	private String sourceField = null;
	private String targetField = null;
	
	public SLEXMMEdge(SLEXMMNode source, SLEXMMNode target, String sourceField, String targetField) {
		this.nodeSource = source;
		this.nodeTarget = target;
		this.sourceField = sourceField;
		this.targetField = targetField;
	}
	
	public SLEXMMNode getSourceNode() {
		return this.nodeSource;
	}
	
	public SLEXMMNode getTargetNode() {
		return this.nodeTarget;
	}
	
	public String getSourceField() {
		return this.sourceField;
	}
	
	public String getTargetField() {
		return this.targetField;
	}
	
	@Override
	public String toString() {
		return nodeSource.getName()+"#"+sourceField+"->"+nodeTarget.getName()+"#"+targetField;
	}
}
