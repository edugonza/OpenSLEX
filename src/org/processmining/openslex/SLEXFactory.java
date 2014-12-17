package org.processmining.openslex;

import java.io.File;

public class SLEXFactory {

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
	
	public static SLEXStorageCollection createStorageCollection() {
		File f = generateNameFile(SLEXStorage.PATH, SLEXStorage.COLLECTION_FILE_EXTENSION);
		SLEXStorageCollection st = null;
		try {
			st = new SLEXStorageImpl(f.getParent(), f.getName(), SLEXStorage.TYPE_COLLECTION);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return st;
	}
	
	public static SLEXStoragePerspective createStoragePerspective() {
		File f = generateNameFile(SLEXStorage.PATH, SLEXStorage.PERSPECTIVE_FILE_EXTENSION);
		SLEXStoragePerspective st = null;
		try {
			st = new SLEXStorageImpl(f.getParent(), f.getName(), SLEXStorage.TYPE_PERSPECTIVE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return st;
	}
	
	public static SLEXStorageDataModel createStorageDataModel() {
		File f = generateNameFile(SLEXStorage.PATH, SLEXStorage.DATAMODEL_FILE_EXTENSION);
		SLEXStorageDataModel st = null;
		try {
			st = new SLEXStorageImpl(f.getParent(), f.getName(), SLEXStorage.TYPE_DATAMODEL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return st;
	}
}
