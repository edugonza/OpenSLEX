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
	}
	
	protected abstract HashMap<AT, ATV> queryAttributeValues();
	
	/**
	 * Retrieve attribute values.
	 */
	@SuppressWarnings("unchecked")
	protected void retrieveAttributeValues() {
		HashMap<AT,ATV> map = (HashMap<AT, ATV>) getStorage().getAttsFromCache(this.getClass(), getId());
		HashMap<String,AT> mapNames = (HashMap<String, AT>) getStorage().getAttNamesFromCache(this.getClass(), getId());
		
		if (map == null) {
			map = this.queryAttributeValues();
			mapNames = new HashMap<>();
			for (AT at : map.keySet()) {
				mapNames.put(at.getName(), at);
			}
		}
		
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