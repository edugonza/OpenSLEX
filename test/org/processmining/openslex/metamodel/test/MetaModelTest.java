package org.processmining.openslex.metamodel.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
	
	@Test
	public void getThings() throws Exception {
    	check(mm.getEvents());
    	check(mm.getObjects());
    	check(mm.getObjectVersions());
    	check(mm.getActivities());
    	check(mm.getProcesses());
    	check(mm.getClasses());
    	check(mm.getAttributes());
    	check(mm.getRelationships());
    	check(mm.getRelations());
    	check(mm.getActivityInstances());
    	check(mm.getCases());
    	check(mm.getLogs());
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
