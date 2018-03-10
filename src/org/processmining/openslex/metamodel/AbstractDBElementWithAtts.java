package org.processmining.openslex.metamodel;

import java.util.HashMap;

public abstract class AbstractDBElementWithAtts<AT extends AbstractAttDBElement, ATV extends AbstractDBElement>
		extends AbstractDBElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6965170960598915101L;

	transient private HashMap<AT, ATV> attributeValues = null;
	
	transient private HashMap<String, AT> attributeNamesMap = null;
	
	public AbstractDBElementWithAtts(SLEXMMStorageMetaModel storage) {
		super(storage);
	}

	protected void setAttributeValues(HashMap<AT, ATV> attributeValues, HashMap<String,AT> attributeNamesMap) {
		this.attributeValues = attributeValues;
		this.attributeNamesMap = attributeNamesMap;
//		HashMap<String,AT> atNamesMap = new HashMap<>();
//		for (AT at: attributeValues.keySet()) {
//			atNamesMap.put(at.getName(), at);
//		}
//		this.attributeNamesMap = atNamesMap;
	}
	
	protected abstract HashMap<AT, ATV> queryAttributeValues();
	
	/**
	 * Retrieve attribute values.
	 */
	protected void retrieveAttributeValues() {
		HashMap<AT,ATV> map = getStorage().getAttsFromCache(this.getClass(), getId());
		
		HashMap<String,AT> mapNames = getStorage().getAttNamesFromCache(this.getClass(), getId());
//		if (map == null) {
//			map = queryAttributeValues();
//			getStorage().putAttsInCache(this, map);
//		}
		this.setAttributeValues(map,mapNames);
	}

	/**
	 * Gets the attribute values.
	 *
	 * @return the attribute values
	 */
	public HashMap<AT, ATV> getAttributeValues() {
		if (attributeValues == null || attributeNamesMap == null) {
			retrieveAttributeValues();
		}
		return attributeValues;
	}

	public ATV getAttributeValue(String attribute) {
		HashMap<AT, ATV> atMap = getAttributeValues();
		AT at = getAttribute(attribute);
		
		if (at != null) {
			return atMap.get(at);
		}
		return null;
	}
	
	public AT getAttribute(String attribute) {
		if (attributeNamesMap == null) {
			retrieveAttributeValues();
		}
		return attributeNamesMap.get(attribute);
	}
	
}