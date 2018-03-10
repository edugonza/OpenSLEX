/*
 * 
 */
package org.processmining.openslex.metamodel;

import org.processmining.openslex.utils.MMUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMEventAttributeValue.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMEventAttributeValue extends AbstractDBElementWithValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6856163030376884736L;

	/** The attribute id. */
	private int attributeId = -1;
	
	/** The event id. */
	private int eventId = -1;
	
	/**
	 * Instantiates a new SLEXMM event attribute value.
	 *
	 * @param storage the storage
	 * @param attributeId the attribute id
	 * @param eventId the event id
	 */
	protected SLEXMMEventAttributeValue(SLEXMMStorageMetaModel storage,int attributeId, int eventId) {
		super(storage);
		this.attributeId = attributeId;
		this.eventId = eventId;
	}
	
	/**
	 * Gets the event id.
	 *
	 * @return the event id
	 */
	public int getEventId() {
		return this.eventId;
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
		return getStorage().insert((SLEXMMEventAttributeValue) at);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#update(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean update(AbstractDBElement at) {
		return getStorage().update((SLEXMMEventAttributeValue) at);
	}
}
