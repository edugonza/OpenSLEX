package org.processmining.openslex.metamodel.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.processmining.openslex.metamodel.*;
import org.processmining.openslex.utils.MMUtils;


public class MetaModelTest {

	static SLEXMMStorageMetaModel mm;
	static String path = "data/";
	static String filename = "metamodel-RL.slexmm";
	
	@BeforeClass
	public static void initAll() throws Exception {
		mm = new SLEXMMStorageMetaModelImpl(path, filename);
	}
	
	@Before
	public void init() {
		
	}
	
	private void check(AbstractRSetElement<?> rset) {
		if (rset == null) {
			fail("Output was null");
		}
	}
	
	private void check(List<?> rset) {
		if (rset == null) {
			fail("Output was null");
		}
	}
	
	public static final List<String> listTypesFrom = new ArrayList<String>()
	{{
		add("Datamodels");
		add("Attributes");
		add("Relationships");
		add("Relations");
		add("Objects");
		add("Versions");
		add("Events");
		add("Cases");
		add("Logs");
		add("Processes");
		add("Activities");
		add("ActivityInstances");
	}};
	
	public static final List<String> listTypesFor = new ArrayList<String>()
	{{
		add("Datamodels");
		add("Attributes");
		add("Relationships");
		add("Relations");
		add("Objects");
		add("Versions");
		add("Events");
		add("Cases");
		add("Logs");
		add("Periods");
		add("Processes");
		add("Activities");
		add("ActivityInstances");
	}};
	
	@Test
	public void getThingsOfThings() throws Exception {
		for (String t: listTypesFrom) {
			getThingsOf(t);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void getThingsOf(String elname) throws Exception {
		Method methodGet = SLEXMMStorageMetaModel.class.getMethod("get"+elname);
		AbstractDBElement dbe = null;
		
		if (List.class.isAssignableFrom(methodGet.getReturnType())) {
			Vector<AbstractDBElement> v = (Vector<AbstractDBElement>) methodGet.invoke(mm);
			dbe = v.get(0);
		} else if (AbstractRSetElement.class.isAssignableFrom(methodGet.getReturnType())) {
			AbstractRSetElement<? extends AbstractDBElement> rset = 
					(AbstractRSetElement<? extends AbstractDBElement>) methodGet.invoke(mm);
			dbe = (AbstractDBElement) rset.getNext();
		} else {
			throw new Exception("Unknown return type: "+methodGet.getReturnType());
		}
		
		int[] ids = new int[] {dbe.getId()};
		for (String t: listTypesFor) {
			if (!t.equals(elname)) {
				Method m = SLEXMMStorageMetaModel.class.getMethod("get"+t+"For"+elname, int[].class);
				Object res = m.invoke(mm, ids);
				if (res instanceof AbstractRSetElement) {
					check((AbstractRSetElement<?>) res);
				} else if (res instanceof List){
					check((List<?>) res);
				}
			}
		}
	}
	
	@Test
	public void getElementsOfRepo() throws Exception {		
		SLEXMMActivityResultSet arset = mm.getActivities();
		SLEXMMActivity act = null;
		ArrayList<Integer> listActIds = new ArrayList<>();
		int fromClazzId = AbstractDBElement.ACT;
		int toClazzId = AbstractDBElement.EV;
		while ((act = arset.getNext()) != null) {
			listActIds.add(act.getId());
		}
		int[] actIds = new int[listActIds.size()];
		for (int i = 0; i < listActIds.size(); i++) {
			actIds[i] = listActIds.get(i);
		}
		SLEXMMEventResultSet erset = mm.getEventsForActivities(actIds);
		SLEXMMEvent ev = null;
		HashMap<Integer,HashSet<Integer>> mapping = new HashMap<>();
		while ((ev = erset.getNext()) != null) {
			HashSet<Integer> set = mapping.get(erset.getOriginId());
			if (set == null) {
				set = new HashSet<>();
			}
			set.add(ev.getId());
			mapping.put(erset.getOriginId(), set);
		}
		
		for (Integer id: mapping.keySet()) {
			int[] ids = MMUtils.colAsArrayInt(mapping.get(id));
			mm.putElementsOf(fromClazzId, toClazzId, id, ids);
		}
		
		for (Integer id: mapping.keySet()) {
			int[] ids = mm.getElementsOf(fromClazzId, toClazzId, id);
			List<Integer> list = Arrays.stream(ids).boxed().collect(Collectors.toList());
			Collections.sort(list);
			List<Integer> list2 = new ArrayList<>();
			SLEXMMEventResultSet eset = mm.getEventsForActivity(id);
			ev = null;
			while ((ev = eset.getNext()) != null) {
				list2.add(ev.getId());
			}
			Collections.sort(list2);
			assertArrayEquals(MMUtils.colAsArrayInt(list), MMUtils.colAsArrayInt(list2));
		}
	}
		
	@AfterClass
	public static void close() {
		mm.disconnect();
	}

}
