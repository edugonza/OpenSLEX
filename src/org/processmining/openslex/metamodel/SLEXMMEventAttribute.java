/*
 * 
 */
package org.processmining.openslex.metamodel;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMEventAttribute.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMEventAttribute extends AbstractAttDBElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8506990697283880676L;

	/**
	 * Instantiates a new SLEXMM event attribute.
	 *
	 * @param storage the storage
	 */
	protected SLEXMMEventAttribute(SLEXMMStorageMetaModel storage) {
		super(storage);
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#insert(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean insert(AbstractDBElement at) {
		return getStorage().insert((SLEXMMEventAttribute) at);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#update(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean update(AbstractDBElement at) {
		return getStorage().update((SLEXMMEventAttribute) at);
	}
	
}
