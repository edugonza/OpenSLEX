package org.processmining.openslex.metamodel.querygen;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SLEXMMEdge extends DefaultWeightedEdge {

	private SLEXMMNode nodeSource = null;
	private SLEXMMNode nodeTarget = null;
	
	public SLEXMMEdge(SLEXMMNode source, SLEXMMNode target) {
		this.nodeSource = source;
		this.nodeTarget = target;
	}
	
	public SLEXMMNode getSourceNode() {
		return this.nodeSource;
	}
	
	public SLEXMMNode getTargetNode() {
		return this.nodeTarget;
	}
	
	@Override
	public String toString() {
		return nodeSource.getName()+"->"+nodeTarget.getName();
	}
}
