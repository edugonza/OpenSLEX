package org.processmining.openslex.metamodel.test;

import static org.junit.Assert.*;

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
	
	@Test
	public void getProcesses() throws Exception {
    	SLEXMMEventResultSet out = mm.getEvents();
    	if (out == null) {
			fail("Output was null");
		}
	}
	
	@AfterClass
	public static void close() {
		mm.disconnect();
	}

}
