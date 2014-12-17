package org.processmining.openslex;

public interface SLEXStorage {

	public static final int TYPE_COLLECTION = 1;
	public static final int TYPE_PERSPECTIVE = 2;
	public static final int TYPE_DATAMODEL = 3;
	public static final String COMMON_CLASS_NAME = "COMMON";
	public static final String COLLECTION_FILE_EXTENSION = ".slexcol";
	public static final String PERSPECTIVE_FILE_EXTENSION = ".slexper";
	public static final String DATAMODEL_FILE_EXTENSION = ".slexdm";

	public static final String PATH = "data";
	
	public abstract String getFilename();

	public abstract String getPath();

	public abstract int getType();

	public abstract void setAutoCommit(boolean autoCommit);

	public abstract void commit();

	public abstract void disconnect();

	public abstract void setAutoCommitOnCreation(boolean flag);

	public abstract boolean isAutoCommitOnCreationEnabled();

}