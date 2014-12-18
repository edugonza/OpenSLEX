package org.processmining.openslex;

import java.io.File;

public class SLEXFactory {
	
	private String path = null;
	
	public SLEXFactory(String path) {
		this.path = path;
		if (path == null) {
			this.path = SLEXStorage.PATH;
		}
	}
	
	private synchronized static File generateNameFile(String path, String ext) {
		File f = null;
		
		try {
			String millis = String.valueOf(System.currentTimeMillis());
		
			f = new File(path+File.separator+millis+ext);
		
			while (f.exists()) {
				millis = String.valueOf(System.currentTimeMillis());
				f = new File(path+File.separator+millis+ext);
			}
		
			f.createNewFile();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return f;
	}
	
	public SLEXStorageCollection createStorageCollection() {
		File f = generateNameFile(SLEXStorage.PATH, SLEXStorage.COLLECTION_FILE_EXTENSION);
		SLEXStorageCollection st = null;
		try {
			st = new SLEXStorageImpl(f.getParent(), f.getName(), SLEXStorage.TYPE_COLLECTION);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return st;
	}
	
	public SLEXStoragePerspective createStoragePerspective() {
		File f = generateNameFile(SLEXStorage.PATH, SLEXStorage.PERSPECTIVE_FILE_EXTENSION);
		SLEXStoragePerspective st = null;
		try {
			st = new SLEXStorageImpl(f.getParent(), f.getName(), SLEXStorage.TYPE_PERSPECTIVE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return st;
	}
	
	public SLEXStorageDataModel createStorageDataModel() {
		File f = generateNameFile(SLEXStorage.PATH, SLEXStorage.DATAMODEL_FILE_EXTENSION);
		SLEXStorageDataModel st = null;
		try {
			st = new SLEXStorageImpl(f.getParent(), f.getName(), SLEXStorage.TYPE_DATAMODEL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return st;
	}
	
	public SLEXEventCollection createEventCollection(String name) {
		SLEXStorageCollection st = createStorageCollection();
		SLEXEventCollection ec = null;
		
		if (st != null) {
			ec = st.createEventCollection(name);
		}
		
		return ec;
	}
	
	public SLEXPerspective createPerspective(SLEXEventCollection ec, String name) {
		SLEXStoragePerspective st = createStoragePerspective();
		SLEXPerspective p = null;
		
		if (st != null) {
			p = st.createPerspective(ec, name);
		}
		
		return p;
	}
	
}
