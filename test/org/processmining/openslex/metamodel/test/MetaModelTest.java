package org.processmining.openslex.metamodel.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.processmining.openslex.metamodel.*;


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
	
	@AfterClass
	public static void close() {
		mm.disconnect();
	}

}
