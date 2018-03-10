/*
 * 
 */
package org.processmining.openslex.metamodel;

import org.processmining.openslex.utils.MMUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMCaseAttributeValue.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMCaseAttributeValue extends AbstractDBElementWithValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4552795010649037397L;

	/** The attribute id. */
	private int attributeId = -1;
	
	/** The case id. */
	private int caseId = -1;
	
	/**
	 * Instantiates a new SLEXMM case attribute value.
	 *
	 * @param storage the storage
	 * @param attributeId the attribute id
	 * @param caseId the case id
	 */
	protected SLEXMMCaseAttributeValue(SLEXMMStorageMetaModel storage,int attributeId, int caseId) {
		super(storage);
		this.attributeId = attributeId;
		this.caseId = caseId;
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
	 * Gets the case id.
	 *
	 * @return the case id
	 */
	public int getCaseId() {
		return this.caseId;
	}
	
	/**
	 * Gets the attribute id.
	 *
	 * @return the attribute id
	 */
	public int getAttributeId() {
		return this.attributeId;
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#insert(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean insert(AbstractDBElement at) {
		return getStorage().insert((SLEXMMCaseAttributeValue) at);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#update(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean update(AbstractDBElement at) {
		return getStorage().update((SLEXMMCaseAttributeValue) at);
	}

}
