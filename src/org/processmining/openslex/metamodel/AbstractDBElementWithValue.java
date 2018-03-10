package org.processmining.openslex.metamodel;

import org.processmining.openslex.utils.MMUtils;

public abstract class AbstractDBElementWithValue extends AbstractDBElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3172317520038251690L;

	public AbstractDBElementWithValue(SLEXMMStorageMetaModel storage) {
		super(storage);
	}

	/** The value */
	private String value = null;
	
	/** The type */
	private String type = null;
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return this.value;
	}
	
	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	protected void setValue(String value) {
		this.value = MMUtils.intern(value);
		setDirty(true);
	}
	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}
	
	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	protected void setType(String type) {
		this.type = MMUtils.intern(type);
		setDirty(true);
	}
	
}
