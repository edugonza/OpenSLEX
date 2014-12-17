package org.processmining.openslex;

import java.util.List;

public interface SLEXStorageDataModel extends SLEXStorage {

	public abstract SLEXDMDataModel createDMDataModel(String name);

	public abstract SLEXDMClass createDMClass(int data_modelId, String name,
			boolean common);

	public abstract SLEXDMAttribute createDMAttribute(int classId, String name,
			boolean common);

	public abstract SLEXDMKey createDMKey(int classId, String name, int type,
			int refers_to_key);

	public abstract SLEXDMKeyAttribute createDMKeyAttribute(int keyId,
			int attributeId, int refersToAttributeId, int position);


	public abstract SLEXDMClassResultSet getClassesForDataModel(
			SLEXDMDataModel dm);

	public abstract SLEXDMDataModelResultSet getDataModels();

	public abstract List<SLEXDMAttribute> getAttributesForDMClass(SLEXDMClass cl);

	public abstract List<SLEXDMKey> getKeysForDMClass(SLEXDMClass cl);

	public abstract List<SLEXDMKeyAttribute> getKeyAttributesForDMKey(
			SLEXDMKey k);

	public abstract boolean insert(SLEXDMAttribute at);

	public abstract boolean update(SLEXDMAttribute at);

	public abstract boolean insert(SLEXDMClass cl);

	public abstract boolean update(SLEXDMClass cl);

	public abstract boolean insert(SLEXDMDataModel dm);

	public abstract boolean update(SLEXDMDataModel dm);

	public abstract boolean insert(SLEXDMKey at);

	public abstract boolean update(SLEXDMKey at);

	public abstract boolean insert(SLEXDMKeyAttribute at);

	public abstract boolean update(SLEXDMKeyAttribute at);
	
}
