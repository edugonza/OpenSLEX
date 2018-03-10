package org.processmining.openslex.metamodel;

import org.processmining.openslex.utils.MMUtils;

public abstract class AbstractAttDBElement extends AbstractDBElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5034928764254897190L;

	public AbstractAttDBElement(SLEXMMStorageMetaModel storage) {
		super(storage);
	}

	/** The name. */
	private String name = null;
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	protected void setName(String name) {
		this.name = MMUtils.intern(name);
		setDirty(true);
	}
	
}
