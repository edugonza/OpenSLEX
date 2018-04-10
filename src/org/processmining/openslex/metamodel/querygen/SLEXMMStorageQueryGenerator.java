package org.processmining.openslex.metamodel.querygen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.KShortestPaths;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.processmining.openslex.metamodel.SLEXMMPeriod;
import org.processmining.openslex.metamodel.SLEXMMStorageMetaModelImpl;
import org.processmining.openslex.utils.MMUtils;

public class SLEXMMStorageQueryGenerator {

	private DirectedWeightedMultigraph<SLEXMMNode, SLEXMMEdge> wmgraph = null;
	private HashMap<SLEXMMTables,SLEXMMNode> nodesMap = null;
	private HashMap<SLEXMMNode,List<String>> fieldsMap = null;
	private final static String METAMODEL_ALIAS = SLEXMMStorageMetaModelImpl.METAMODEL_ALIAS;
	
	private final static HashMap<SLEXMMTables,String[]> tablesMap;
	
	private final static int MAX_PATHS = 4;
	
	static {
		tablesMap = new HashMap<>();
		tablesMap.put(SLEXMMTables.T_CLASS, new String[] { "id", "datamodel_id", "name" });
		tablesMap.put(SLEXMMTables.T_DATAMODEL, new String[] { "id", "name" });
		tablesMap.put(SLEXMMTables.T_OBJECT, new String[] { "id", "class_id" });
		tablesMap.put(SLEXMMTables.T_OBJECT_VERSION,
				new String[] { "id", "object_id", "start_timestamp", "end_timestamp" });
		tablesMap.put(SLEXMMTables.T_LOG, new String[] { "id", "process_id", "name" });
		tablesMap.put(SLEXMMTables.T_CASE, new String[] { "id", "name" });
		tablesMap.put(SLEXMMTables.T_CASE_TO_LOG, new String[] { "case_id", "log_id" });
		tablesMap.put(SLEXMMTables.T_PROCESS, new String[] { "id", "name" });
		tablesMap.put(SLEXMMTables.T_ACTIVITY_TO_PROCESS, new String[] { "process_id", "activity_id" });
		tablesMap.put(SLEXMMTables.T_ACTIVITY, new String[] { "id", "name" });
		tablesMap.put(SLEXMMTables.T_ACTIVITY_INSTANCE, new String[] { "id", "activity_id" });
		tablesMap.put(SLEXMMTables.T_ACTIVITY_INSTANCE_TO_CASE, new String[] { "activity_instance_id", "case_id" });
		tablesMap.put(SLEXMMTables.T_EVENT,
				new String[] { "id", "activity_instance_id", "ordering", "timestamp", "lifecycle", "resource" });
		tablesMap.put(SLEXMMTables.T_EVENT_TO_OBJECT_VERSION,
				new String[] { "event_id", "object_version_id", "label" });
		tablesMap.put(SLEXMMTables.T_ATTRIBUTE_NAME, new String[] { "id", "class_id", "name" });
		tablesMap.put(SLEXMMTables.T_RELATIONSHIP, new String[] { "id", "source", "target", "name" });
		tablesMap.put(SLEXMMTables.T_RELATION, new String[] { "id", "source_object_version_id",
				"target_object_version_id", "relationship_id", "start_timestamp", "end_timestamp" });
		tablesMap.put(SLEXMMTables.T_ATTRIBUTE_VALUE,
				new String[] { "id", "object_version_id", "attribute_name_id", "value", "type" });
		tablesMap.put(SLEXMMTables.T_EVENT_ATTRIBUTE_VALUE,
				new String[] { "id", "event_id", "event_attribute_name_id", "value", "type" });
		tablesMap.put(SLEXMMTables.T_EVENT_ATTRIBUTE_NAME, new String[] { "id", "name" });
		tablesMap.put(SLEXMMTables.T_CASE_ATTRIBUTE_NAME, new String[] { "id", "name" });
		tablesMap.put(SLEXMMTables.T_CASE_ATTRIBUTE_VALUE,
				new String[] { "id", "case_id", "case_attribute_name_id", "value", "type" });
		tablesMap.put(SLEXMMTables.T_LOG_ATTRIBUTE_NAME, new String[] { "id", "name" });
		tablesMap.put(SLEXMMTables.T_LOG_ATTRIBUTE_VALUE,
				new String[] { "id", "log_attribute_name_id", "log_id", "value", "type" });
		tablesMap.put(SLEXMMTables.T_CLASSIFIER, new String[] { "id", "log_id", "name" });
		tablesMap.put(SLEXMMTables.T_CLASSIFIER_ATTRIBUTES,
				new String[] { "id", "classifier_id", "event_attribute_name_id" });
	}
	
	private final static HashMap<Pair<SLEXMMTables>,Pair<String>> edgesMap;
	
	static {
		edgesMap = new HashMap<>();
		edgesMap.put(new Pair<>(SLEXMMTables.T_CLASS, SLEXMMTables.T_DATAMODEL), new Pair<>("datamodel_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_OBJECT, SLEXMMTables.T_CLASS), new Pair<>("class_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_OBJECT_VERSION, SLEXMMTables.T_OBJECT), new Pair<>("object_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_ACTIVITY_INSTANCE, SLEXMMTables.T_ACTIVITY),
				new Pair<>("activity_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_ACTIVITY_TO_PROCESS, SLEXMMTables.T_ACTIVITY),
				new Pair<>("activity_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_ACTIVITY_TO_PROCESS, SLEXMMTables.T_PROCESS),
				new Pair<>("process_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_LOG, SLEXMMTables.T_PROCESS), new Pair<>("process_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_CASE_TO_LOG, SLEXMMTables.T_LOG), new Pair<>("log_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_CASE_TO_LOG, SLEXMMTables.T_CASE), new Pair<>("case_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_ACTIVITY_INSTANCE_TO_CASE, SLEXMMTables.T_CASE),
				new Pair<>("case_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_ACTIVITY_INSTANCE_TO_CASE, SLEXMMTables.T_ACTIVITY_INSTANCE),
				new Pair<>("activity_instance_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_EVENT, SLEXMMTables.T_ACTIVITY_INSTANCE),
				new Pair<>("activity_instance_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_EVENT_TO_OBJECT_VERSION, SLEXMMTables.T_EVENT),
				new Pair<>("event_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_EVENT_TO_OBJECT_VERSION, SLEXMMTables.T_OBJECT_VERSION),
				new Pair<>("object_version_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_ATTRIBUTE_NAME, SLEXMMTables.T_CLASS), new Pair<>("class_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_RELATIONSHIP, SLEXMMTables.T_CLASS), new Pair<>("source", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_RELATIONSHIP, SLEXMMTables.T_CLASS), new Pair<>("target", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_RELATION, SLEXMMTables.T_OBJECT_VERSION),
				new Pair<>("source_object_version_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_RELATION, SLEXMMTables.T_OBJECT_VERSION),
				new Pair<>("target_object_version_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_RELATION, SLEXMMTables.T_RELATIONSHIP),
				new Pair<>("relationship_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_ATTRIBUTE_VALUE, SLEXMMTables.T_OBJECT_VERSION),
				new Pair<>("object_version_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_ATTRIBUTE_VALUE, SLEXMMTables.T_ATTRIBUTE_NAME),
				new Pair<>("attribute_name_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_EVENT_ATTRIBUTE_VALUE, SLEXMMTables.T_EVENT),
				new Pair<>("event_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_EVENT_ATTRIBUTE_VALUE, SLEXMMTables.T_EVENT_ATTRIBUTE_NAME),
				new Pair<>("event_attribute_name_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_CASE_ATTRIBUTE_VALUE, SLEXMMTables.T_CASE), new Pair<>("case_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_CASE_ATTRIBUTE_VALUE, SLEXMMTables.T_CASE_ATTRIBUTE_NAME),
				new Pair<>("case_attribute_name_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_LOG_ATTRIBUTE_VALUE, SLEXMMTables.T_LOG), new Pair<>("log_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_LOG_ATTRIBUTE_VALUE, SLEXMMTables.T_LOG_ATTRIBUTE_NAME),
				new Pair<>("log_attribute_name_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_CLASSIFIER, SLEXMMTables.T_LOG), new Pair<>("log_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_CLASSIFIER_ATTRIBUTES, SLEXMMTables.T_CLASSIFIER),
				new Pair<>("classifier_id", "id"));
		edgesMap.put(new Pair<>(SLEXMMTables.T_CLASSIFIER_ATTRIBUTES, SLEXMMTables.T_EVENT_ATTRIBUTE_NAME),
				new Pair<>("event_attribute_name_id", "id"));
	}
	
	private static SLEXMMEdge getEdge(SLEXMMTables a, SLEXMMTables b) throws Exception {
		Pair<SLEXMMTables> p = new Pair<>(a,b);
		Pair<SLEXMMTables> pInv = new Pair<>(b,a);
		Pair<String> fieldsEdge = null;
		
		if (edgesMap.containsKey(p)) {
			fieldsEdge = edgesMap.get(p);
		} else if (edgesMap.containsKey(pInv)) {
			Pair<String> feaux = edgesMap.get(pInv);
			fieldsEdge = new Pair<String>(feaux.b, feaux.a);
		} else {
			throw new Exception("pair of fields for edge ("+p.a+","+p.b+") not found");
		}
		
		SLEXMMEdge edge = new SLEXMMEdge(new SLEXMMNode(a), new SLEXMMNode(b),
				fieldsEdge.a,fieldsEdge.b);
		return edge;
	}
	
	private static List<SLEXMMEdge> createPresetPath(List<SLEXMMTables> tables) throws Exception {
		ArrayList<SLEXMMEdge> edges = new ArrayList<>();
		
		for (int i = 1; i < tables.size(); i++) {
			edges.add(getEdge(tables.get(i-1),tables.get(i)));
		}
		
		return edges;
	}
	
	private static List<SLEXMMEdge> createInvPresetPath(List<SLEXMMTables> tables) throws Exception {
		ArrayList<SLEXMMTables> tablesInv = new ArrayList<>(tables);
		Collections.reverse(tablesInv);
		return createPresetPath(tablesInv);
	}
	
	private final static HashMap<Pair<SLEXMMTables>,List<SLEXMMEdge>> presetPaths;
	
	private static void addPresetPath(SLEXMMTables from, SLEXMMTables to, SLEXMMTables... steps) throws Exception {
		Pair<SLEXMMTables> ppair = new Pair<SLEXMMTables>(from, to);
		Pair<SLEXMMTables> invppair = new Pair<SLEXMMTables>(to, from);
		
		List<SLEXMMTables> plist = Arrays.asList(steps);
		
		presetPaths.put(ppair,createPresetPath(plist));
		presetPaths.put(invppair, createInvPresetPath(plist));
	}
	
	static {
		presetPaths = new HashMap<>();
		try {
			addPresetPath(SLEXMMTables.T_LOG,SLEXMMTables.T_EVENT,
					SLEXMMTables.T_LOG,
					SLEXMMTables.T_CASE_TO_LOG,
					SLEXMMTables.T_CASE,
					SLEXMMTables.T_ACTIVITY_INSTANCE_TO_CASE,
					SLEXMMTables.T_ACTIVITY_INSTANCE,
					SLEXMMTables.T_EVENT);
			
			addPresetPath(SLEXMMTables.T_ACTIVITY, SLEXMMTables.T_CASE,
					SLEXMMTables.T_ACTIVITY,
					SLEXMMTables.T_ACTIVITY_INSTANCE,
					SLEXMMTables.T_ACTIVITY_INSTANCE_TO_CASE,
					SLEXMMTables.T_CASE);
			
			addPresetPath(SLEXMMTables.T_ACTIVITY, SLEXMMTables.T_LOG,
					SLEXMMTables.T_ACTIVITY,
					SLEXMMTables.T_ACTIVITY_INSTANCE,
					SLEXMMTables.T_ACTIVITY_INSTANCE_TO_CASE,
					SLEXMMTables.T_CASE,
					SLEXMMTables.T_CASE_TO_LOG,
					SLEXMMTables.T_LOG);
			
			addPresetPath(SLEXMMTables.T_RELATIONSHIP, SLEXMMTables.T_CASE,
					SLEXMMTables.T_RELATIONSHIP,
					SLEXMMTables.T_RELATION,
					SLEXMMTables.T_OBJECT_VERSION,
					SLEXMMTables.T_EVENT_TO_OBJECT_VERSION,
					SLEXMMTables.T_EVENT,
					SLEXMMTables.T_ACTIVITY_INSTANCE,
					SLEXMMTables.T_ACTIVITY_INSTANCE_TO_CASE,
					SLEXMMTables.T_CASE);
			
			addPresetPath(SLEXMMTables.T_ATTRIBUTE_NAME, SLEXMMTables.T_LOG,
					SLEXMMTables.T_ATTRIBUTE_NAME,
					SLEXMMTables.T_ATTRIBUTE_VALUE,
					SLEXMMTables.T_OBJECT_VERSION,
					SLEXMMTables.T_EVENT_TO_OBJECT_VERSION,
					SLEXMMTables.T_EVENT,
					SLEXMMTables.T_ACTIVITY_INSTANCE,
					SLEXMMTables.T_ACTIVITY_INSTANCE_TO_CASE,
					SLEXMMTables.T_CASE,
					SLEXMMTables.T_CASE_TO_LOG,
					SLEXMMTables.T_LOG);
			
			addPresetPath(SLEXMMTables.T_RELATIONSHIP, SLEXMMTables.T_LOG,
					SLEXMMTables.T_RELATIONSHIP,
					SLEXMMTables.T_RELATION,
					SLEXMMTables.T_OBJECT_VERSION,
					SLEXMMTables.T_EVENT_TO_OBJECT_VERSION,
					SLEXMMTables.T_EVENT,
					SLEXMMTables.T_ACTIVITY_INSTANCE,
					SLEXMMTables.T_ACTIVITY_INSTANCE_TO_CASE,
					SLEXMMTables.T_CASE,
					SLEXMMTables.T_CASE_TO_LOG,
					SLEXMMTables.T_LOG);
					
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public SLEXMMStorageQueryGenerator() {
		init();
	}
	
	private void init() {
		nodesMap = new HashMap<>();
		fieldsMap = new HashMap<>();
		wmgraph = new DirectedWeightedMultigraph<>(SLEXMMEdge.class);
							
		for (SLEXMMTables table: tablesMap.keySet()) {
			SLEXMMNode n = new SLEXMMNode(table);
			nodesMap.put(table,n);
			String[] nodeInfo = tablesMap.get(table);
			for (int i = 0; i < nodeInfo.length; i++) {
				List<String> fields = fieldsMap.get(n);
				if (fields == null) {
					fields = new ArrayList<>();
					fieldsMap.put(n, fields);
				}
				fields.add(nodeInfo[i]);
			}
			wmgraph.addVertex(n);
		}
		
		for (Pair<SLEXMMTables> edgeK: edgesMap.keySet()) {
			SLEXMMNode nsource = nodesMap.get(edgeK.a);
			SLEXMMNode ntarget = nodesMap.get(edgeK.b);
			String sourceField = edgesMap.get(edgeK).a;
			String targetField = edgesMap.get(edgeK).b;
			SLEXMMEdge e = new SLEXMMEdge(nsource,ntarget,sourceField,targetField);
			SLEXMMEdge eI = new SLEXMMEdge(ntarget,nsource,targetField,sourceField);
			wmgraph.addEdge(e.getSourceNode(), e.getTargetNode(), e);
			wmgraph.addEdge(eI.getSourceNode(), eI.getTargetNode(), eI);
			wmgraph.setEdgeWeight(e,1.0);
			wmgraph.setEdgeWeight(eI,0.0);
		}
		
	}
	
	public List<List<SLEXMMEdge>> getPaths(SLEXMMTables orig, SLEXMMTables dest) {
		SLEXMMNode norig = nodesMap.get(orig);
		SLEXMMNode ndest = nodesMap.get(dest);
		
		if (presetPaths.containsKey(new Pair<SLEXMMTables>(orig,dest))) {
			return Arrays.asList(presetPaths.get(new Pair<SLEXMMTables>(orig,dest)));
		}
		
		//List<SLEXMMEdge> path = DijkstraShortestPath.findPathBetween(wmgraph, norig, ndest);
		KShortestPaths<SLEXMMNode, SLEXMMEdge> kspc = new KShortestPaths<SLEXMMNode, SLEXMMEdge>(wmgraph, norig,
				MAX_PATHS);
		
		List<GraphPath<SLEXMMNode,SLEXMMEdge>> paths = null;
		
		if (norig != ndest) {
			paths = kspc.getPaths(ndest);
		} else {
			paths = new ArrayList<>();
		}
		
		List<List<SLEXMMEdge>> listPaths = new ArrayList<>();
		
		//int i = 0;
		double minWeight = Double.MAX_VALUE;
		int minLength = Integer.MAX_VALUE;
		
		for (GraphPath<SLEXMMNode,SLEXMMEdge> p : paths) {
			int length = p.getEdgeList().size();
			double weight = p.getWeight();
			
			if (length < minLength) {
				minLength = length;
			}
			
			if (weight < minWeight) {
				minWeight = weight;
			}
		}
		
		for (GraphPath<SLEXMMNode,SLEXMMEdge> p : paths) {
			int length = p.getEdgeList().size();
			double weight = p.getWeight();
			
			if (weight == minWeight) {
				if (length == minLength) {
					listPaths.add(p.getEdgeList());
				}
			}
		}
		
		if (listPaths.isEmpty()) {
			SLEXMMEdge e = new SLEXMMEdge(norig, ndest, "id", "id");
			List<SLEXMMEdge> path = new ArrayList<>();
			path.add(e);
			listPaths.add(path);
		}
		
		return listPaths;
	}
	
	private String getBetweens(String t, int[] ids) {
		
		StringBuilder strbld = new StringBuilder();
		
		if (ids.length == 0) {
			return " 1 ";
		} else if (ids.length == 1) {
			return t+" == "+ids[0];
		} else {
			ArrayList<Integer> arr = new ArrayList<>();
			ArrayList<Integer> ranges = new ArrayList<>();
			strbld.append(" ( ");
			int start = -1;
			int end = -1;
			int prevId = -1;
			for (int i = 0; i < ids.length; i++) {
				if (start == -1) {
					start = ids[i];
				} else if (ids[i] == prevId+1) {
					end = ids[i];
				} else {
					if (end != -1) {
						ranges.add(start);
						ranges.add(end);
						end = -1;
					} else {
						arr.add(start);
					}
					start = ids[i];
				}
				prevId = ids[i];
			}
			
			if (start != -1) {
				if (start != end) {
					if (end != -1) {
						ranges.add(start);
						ranges.add(end);
					} else {
						arr.add(start);
					}
				}
			}
			
			boolean first = true;
			if (!arr.isEmpty()) {
				String idsStr = SLEXMMStorageMetaModelImpl.buildStringFromArray(MMUtils.colAsArrayInt(arr));
				strbld.append(t+" IN ("+idsStr+")");
				first = false;
			}
			if (!ranges.isEmpty()) {
				for (int j = 0; j < ranges.size()-1; j+=2) {
					if (!first) {
						strbld.append(" OR ");
					}
					first = false;
					strbld.append(t);
					strbld.append(" BETWEEN ");
					strbld.append(ranges.get(j));
					strbld.append(" AND ");
					strbld.append(ranges.get(j+1));
					strbld.append(" ");
				}
			}
			strbld.append(" ) ");
			return strbld.toString();
		}
	}
	
	private String getFromAndWhereOfQuery(List<SLEXMMEdge> path, int[] ids) {
		StringBuilder strbldr = new StringBuilder();
		
		SLEXMMEdge lastEdge = path.get(path.size()-1);
		
		strbldr.append("FROM ");
		
		int i = 1;
		
		for (SLEXMMEdge e: path) {
			SLEXMMNode n = e.getSourceNode();
			strbldr.append(METAMODEL_ALIAS+".");
			strbldr.append('"'+n.getName()+'"');
			strbldr.append(" as t");
			strbldr.append(i);
			strbldr.append(", ");
			i++;
		}
		
		strbldr.append(METAMODEL_ALIAS+".");
		strbldr.append('"'+lastEdge.getTargetNode().getName()+'"');
		strbldr.append(" as t");
		strbldr.append(i);
		
		strbldr.append(" WHERE ");
		
		String idsStr = null; 
		
		if (ids != null) {
			idsStr = getBetweens("t"+i+".id",ids);
			strbldr.append(" ");
			strbldr.append(idsStr);
			//idsStr = SLEXMMStorageMetaModelImpl.buildStringFromArray(ids);
			//strbldr.append(" t"+i+".id IN ("+idsStr+")");
		} else {
			strbldr.append(" 1 ");
		}
		
		i = 1;
		for (SLEXMMEdge e: path) {
			strbldr.append(" AND t"+i+"."+e.getSourceField()+
					" = t"+(i+1)+"."+e.getTargetField());
			i++;
		}
		
		return strbldr.toString();
	}
	
	public String getSelectQuery(List<List<SLEXMMEdge>> paths, int[] ids) {
		StringBuilder strbldr = new StringBuilder();
		
		boolean first = true;

		for (List<SLEXMMEdge> path : paths) {

			if (!first) {
				strbldr.append(" UNION ");
			} else {
				first = false;
			}

			//SLEXMMEdge lastEdge = path.get(path.size() - 1);

			//strbldr.append("SELECT DISTINCT t" + (path.size() + 1) + ".id"
			strbldr.append("SELECT t" + (path.size() + 1) + ".id"
					+ " as originIdQuery, t1.* ");

			strbldr.append(getFromAndWhereOfQuery(path, ids));

		}
		
		//System.out.println("Query: "+strbldr.toString());
		
		return strbldr.toString();
	}
	
	public String getSelectQueryForPeriod(List<List<SLEXMMEdge>> paths, SLEXMMPeriod p) {
		StringBuilder strbldr = new StringBuilder();
		
		boolean first = true;

		for (List<SLEXMMEdge> path : paths) {

			if (!first) {
				strbldr.append(" UNION ");
			} else {
				first = false;
			}

			SLEXMMEdge lastEdge = path.get(path.size() - 1);
			SLEXMMNode lastNode = lastEdge.getTargetNode();
			String startTField = null;
			String endTField = null;

			switch (lastNode.getTable()) {
			case T_EVENT:
				startTField = "timestamp";
				endTField = "timestamp";
				break;
			case T_OBJECT_VERSION:
				startTField = "start_timestamp";
				endTField = "end_timestamp";
				break;
			case T_RELATION:
				startTField = "start_timestamp";
				endTField = "end_timestamp";
				break;
			default:
				return null;
			}

			//strbldr.append("SELECT DISTINCT t1.* ");
			strbldr.append("SELECT t1.* ");

			strbldr.append(getFromAndWhereOfQuery(path, null));

			strbldr.append(" AND (t" + (path.size() + 1) + "." + endTField + " >= " + p.getStart());
			strbldr.append(" OR t" + (path.size() + 1) + "." + endTField + " = -1)");

			if (p.getEnd() != -1) {
				strbldr.append(" AND t" + (path.size() + 1) + "." + startTField + " <= " + p.getEnd());
			}

		}
		
		//System.out.println("Query: "+strbldr.toString());
		
		return strbldr.toString();
	}
	
	public String getPeriodsQuery(List<List<SLEXMMEdge>> paths, int[] ids) {
		StringBuilder strbldr = new StringBuilder();
		
		boolean first = true;

		for (List<SLEXMMEdge> path : paths) {

			if (!first) {
				strbldr.append(" UNION ");
			} else {
				first = false;
			}

			//SLEXMMEdge lastEdge = path.get(path.size() - 1);
			SLEXMMEdge firstEdge = path.get(0);
			SLEXMMNode firstNode = firstEdge.getSourceNode();
			String startTField = null;
			String endTField = null;

			switch (firstNode.getTable()) {
			case T_EVENT:
				startTField = "timestamp";
				endTField = "timestamp";
				break;
			case T_OBJECT_VERSION:
				startTField = "start_timestamp";
				endTField = "end_timestamp";
				break;
			case T_RELATION:
				startTField = "start_timestamp";
				endTField = "end_timestamp";
				break;
			default:
				return null;
			}

			//strbldr.append("SELECT DISTINCT t" + (path.size() + 1) + ".id"
			strbldr.append("SELECT t" + (path.size() + 1) + ".id"
					+ " as originIdQuery, min(t1." + startTField + ") as start, " + " max(t1." + endTField
					+ ") as end, " + " min(t1." + endTField + ") as end2 ");

			strbldr.append(getFromAndWhereOfQuery(path, ids));

			strbldr.append(" GROUP BY originIdQuery ");

		}

		//System.out.println("Query: "+strbldr.toString());
		
		return strbldr.toString();
	}
	
}
