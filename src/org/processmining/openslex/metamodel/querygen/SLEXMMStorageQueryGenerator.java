package org.processmining.openslex.metamodel.querygen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.processmining.openslex.metamodel.SLEXMMStorageMetaModel;
import org.processmining.openslex.metamodel.SLEXMMStorageMetaModelImpl;

public class SLEXMMStorageQueryGenerator {

	private DirectedWeightedMultigraph<SLEXMMNode, SLEXMMEdge> wmgraph = null;
	private HashMap<String,SLEXMMNode> nodesMap = null;
	private HashMap<SLEXMMNode,List<String>> fieldsMap = null;
	private final static String METAMODEL_ALIAS = SLEXMMStorageMetaModelImpl.METAMODEL_ALIAS;
	
	private final static String[][] nodesStr = new String[][] {
			{"class","id","datamodel_id","name"},
			{"datamodel","id","name"},
			{"object","id","class_id"},
			{"object_version","id","object_id","start_timestamp","end_timestamp"},
			{"log","id","process_id","name"},
			{"case","id","name"},
			{"case_to_log","case_id","log_id"},
			{"process","id","name"},
			{"activity_to_process","process_id","activity_id"},
			{"activity","id","name"},
			{"activity_instance","id","activity_id"},
			{"activity_instance_to_case","activity_instance_id","case_id"},
			{"event","id","activity_instance_id","ordering","timestamp","lifecycle","resource"},
			{"event_to_object_version","event_id","object_version_id","label"}
		};
	
	private final static String[][] edgesStr = new String[][] {
			{"class","datamodel","datamodel_id","id"},
			{"object","class","class_id","id"},
			{"object_version","object","object_id","id"},
			{"activity_instance","activity","activity_id","id"},
			{"activity_to_process","activity","activity_id","id"},
			{"activity_to_process","process","process_id","id"},
			{"log","process","process_id","id"},
			{"case_to_log","log","log_id","id"},
			{"case_to_log","case","case_id","id"},
			{"activity_instance_to_case","case","case_id","id"},
			{"activity_instance_to_case","activity_instance","activity_instance_id","id"},
			{"event","activity_instance","activity_instance_id","id"},
			{"event_to_object_version","event","event_id","id"},
			{"event_to_object_version","object_version","object_version_id","id"}
		};		
	
	public SLEXMMStorageQueryGenerator() {
		init();
	}
	
	private void init() {
		nodesMap = new HashMap<>();
		fieldsMap = new HashMap<>();
		wmgraph = new DirectedWeightedMultigraph<>(SLEXMMEdge.class);
							
		for (String[] nodeInfo: nodesStr) {
			SLEXMMNode n = new SLEXMMNode(nodeInfo[0]);
			nodesMap.put(nodeInfo[0],n);
			for (int i = 1; i < nodeInfo.length; i++) {
				List<String> fields = fieldsMap.get(n);
				if (fields == null) {
					fields = new ArrayList<>();
					fieldsMap.put(n, fields);
				}
				fields.add(nodeInfo[i]);
			}
			wmgraph.addVertex(n);
		}
		
		for (String[] edgeS: edgesStr) {
			SLEXMMNode nsource = nodesMap.get(edgeS[0]);
			SLEXMMNode ntarget = nodesMap.get(edgeS[1]);
			String sourceField = edgeS[2];
			String targetField = edgeS[3];
			SLEXMMEdge e = new SLEXMMEdge(nsource,ntarget,sourceField,targetField);
			SLEXMMEdge eI = new SLEXMMEdge(ntarget,nsource,targetField,sourceField);
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
	
	public String getQuery(List<SLEXMMEdge> path, int[] ids) {
		StringBuilder strbldr = new StringBuilder();
		
		SLEXMMEdge lastEdge = path.get(path.size()-1);
		
		strbldr.append("SELECT DISTINCT t"+(path.size()+1)+"."+lastEdge.getTargetField()+
				" as originIdQuery, t1.* ");
		
		strbldr.append("FROM ");
		
		int i = 1;
		
		for (SLEXMMEdge e: path) {
			SLEXMMNode n = e.getSourceNode();
			strbldr.append(METAMODEL_ALIAS+".");
			strbldr.append(n.getName());
			strbldr.append(" as t");
			strbldr.append(i);
			strbldr.append(", ");
			i++;
		}
		
		strbldr.append(METAMODEL_ALIAS+".");
		strbldr.append(lastEdge.getTargetNode().getName());
		strbldr.append(" as t");
		strbldr.append(i);
		
		strbldr.append(" WHERE ");
		
		String idsStr = SLEXMMStorageMetaModelImpl.buildStringFromArray(ids);
		
		strbldr.append(" t"+i+"."+lastEdge.getTargetField()+" IN ("+idsStr+")");
		i = 1;
		for (SLEXMMEdge e: path) {
			strbldr.append(" AND t"+i+"."+e.getSourceField()+
					" = t"+(i+1)+"."+e.getTargetField());
			i++;
		}
		
		return strbldr.toString();
	}
	
	public static void main(String[] args) {
		SLEXMMStorageQueryGenerator slxmmsqgen = new SLEXMMStorageQueryGenerator();
		String orig = "datamodel";
		String dest = "version";
		List<SLEXMMEdge> path = slxmmsqgen.getPath(orig, dest);
		System.out.println(orig+" to "+dest);
		System.out.println(path);
		
		System.out.println("Query: "+slxmmsqgen.getQuery(path,new int[]{1,2,3}));
		
		orig = "case";
		dest = "process";
		path = slxmmsqgen.getPath(orig, dest);
		System.out.println(orig+" to "+dest);
		System.out.println(path);
		
		System.out.println("Query: "+slxmmsqgen.getQuery(path,new int[]{1,2,3}));
		
		orig = "case";
		dest = "datamodel";
		path = slxmmsqgen.getPath(orig, dest);
		System.out.println(orig+" to "+dest);
		System.out.println(path);
		
		System.out.println("Query: "+slxmmsqgen.getQuery(path,new int[]{1,2,3}));
		
	}
	
}
