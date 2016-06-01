package org.processmining.openslex.metamodel.querygen;

import java.util.HashMap;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DirectedWeightedMultigraph;

public class SLEXMMStorageQueryGenerator {

	private DirectedWeightedMultigraph<SLEXMMNode, SLEXMMEdge> wmgraph = null;
	private HashMap<String,SLEXMMNode> nodesMap = null;
	
	public SLEXMMStorageQueryGenerator() {
		init();
	}
	
	private void init() {
		nodesMap = new HashMap<>();
		wmgraph = new DirectedWeightedMultigraph<>(SLEXMMEdge.class);
		
		String[] nodesStr = new String[] {
				"class",
				"datamodel",
				"object",
				"version",
				"log",
				"case",
				"caseToLog",
				"process",
				"activityToProcess",
				"activity",
				"activityInstance",
				"activityInstanceToCase",
				"event",
				"eventToObjectVersion",
				};
		String[][] edgesStr = new String[][] {
			{"class","datamodel"},
			{"object","class"},
			{"version","object"},
			{"activityInstance","activity"},
			{"activityToProcess","activity"},
			{"activityToProcess","process"},
			{"log","process"},
			{"caseToLog","log"},
			{"caseToLog","case"},
			{"activityInstanceToCase","case"},
			{"activityInstanceToCase","activityInstance"},
			{"event","activityInstance"},
			{"eventToObjectVersion","event"},
			{"eventToObjectVersion","version"}
			};
		
		for (String nodeName: nodesStr) {
			SLEXMMNode n = new SLEXMMNode(nodeName);
			nodesMap.put(nodeName,n);
			wmgraph.addVertex(n);
		}
		
		for (String[] edgeS: edgesStr) {
			SLEXMMNode nsource = nodesMap.get(edgeS[0]);
			SLEXMMNode ntarget = nodesMap.get(edgeS[1]);
			SLEXMMEdge e = new SLEXMMEdge(nsource,ntarget);
			SLEXMMEdge eI = new SLEXMMEdge(ntarget,nsource);
			wmgraph.addEdge(e.getSourceNode(), e.getTargetNode(), e);
			wmgraph.addEdge(eI.getSourceNode(), eI.getTargetNode(), eI);
			wmgraph.setEdgeWeight(e,0.0);
			wmgraph.setEdgeWeight(eI,1.0);
		}
		
	}
	
	public List<SLEXMMEdge> getPath(String orig, String dest) {
		SLEXMMNode norig = nodesMap.get(orig);
		SLEXMMNode ndest = nodesMap.get(dest);
				
		return DijkstraShortestPath.findPathBetween(wmgraph, norig, ndest);
	}
	
	public static void main(String[] args) {
		SLEXMMStorageQueryGenerator slxmmsqgen = new SLEXMMStorageQueryGenerator();
		String orig = "datamodel";
		String dest = "version";
		List<SLEXMMEdge> path = slxmmsqgen.getPath(orig, dest);
		System.out.println(orig+" to "+dest);
		System.out.println(path);
		
		orig = "case";
		dest = "process";
		path = slxmmsqgen.getPath(orig, dest);
		System.out.println(orig+" to "+dest);
		System.out.println(path);
		
		orig = "case";
		dest = "datamodel";
		path = slxmmsqgen.getPath(orig, dest);
		System.out.println(orig+" to "+dest);
		System.out.println(path);
	}
	
}
