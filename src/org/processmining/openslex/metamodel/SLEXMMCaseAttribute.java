/*
 * 
 */
package org.processmining.openslex.metamodel;

import org.processmining.openslex.utils.MMUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMCaseAttribute.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMCaseAttribute extends AbstractAttDBElement {

	/** The name. */
	private String name = null;
	
	/**
	 * Instantiates a new SLEXMM case attribute.
	 *
	 * @param storage the storage
	 */
	protected SLEXMMCaseAttribute(SLEXMMStorageMetaModel storage) {
		super(storage);
	}
	
	/**
	 * Gets the storage.
	 *
	 * @return the storage
	 */
	public SLEXMMStorageMetaModel getStorage() {
		return (SLEXMMStorageMetaModel) super.storage;
	}
	
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#insert(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean insert(AbstractDBElement at) {
		return getStorage().insert((SLEXMMCaseAttribute) at);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#update(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean update(AbstractDBElement at) {
		return getStorage().update((SLEXMMCaseAttribute) at);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
//	@Override
//	public int hashCode() {
//		return ("case_attribute#"+getId()).hashCode();
//	}
}
