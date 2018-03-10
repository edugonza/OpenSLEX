/*
 * 
 */
package org.processmining.openslex.metamodel;

import org.processmining.openslex.utils.MMUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMDataModel.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMDataModel extends AbstractDBElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6724308569351269134L;
	
	/** The name. */
	private String name = null;
	
	/**
	 * Instantiates a new SLEXMM data model.
	 *
	 * @param storage the storage
	 */
	public SLEXMMDataModel(SLEXMMStorageMetaModel storage) {
		super(storage);
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#insert(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean insert(AbstractDBElement dm) {
		return getStorage().insert((SLEXMMDataModel) dm);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#update(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean update(AbstractDBElement dm) {
		return getStorage().update((SLEXMMDataModel) dm);
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
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
