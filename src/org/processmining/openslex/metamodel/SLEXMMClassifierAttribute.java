/*
 * 
 */
package org.processmining.openslex.metamodel;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMClassifierAttribute.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMClassifierAttribute extends AbstractDBElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4540045327093018394L;

	/** The classifier id. */
	private int classifier_id = -1;
	
	/** The event attribute name id. */
	private int event_attribute_name_id = -1;
	
	/**
	 * Instantiates a new SLEXMM classifier attribute.
	 *
	 * @param storage the storage
	 */
	protected SLEXMMClassifierAttribute(SLEXMMStorageMetaModel storage) {
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
	 * Gets the classifier id.
	 *
	 * @return the classifier id
	 */
	public int getClassifierId() {
		return this.classifier_id;
	}
	
	/**
	 * Sets the classifier id.
	 *
	 * @param id the new classifier id
	 */
	protected void setClassifierId(int id) {
		this.classifier_id = id;
		setDirty(true);
	}
	
	/**
	 * Gets the event attribute name id.
	 *
	 * @return the event attribute name id
	 */
	public int getEventAttributeNameId() {
		return this.event_attribute_name_id;
	}
	
	/**
	 * Sets the event attribute name id.
	 *
	 * @param id the new event attribute name id
	 */
	protected void setEventAttributeNameId(int id) {
		this.event_attribute_name_id = id;
		setDirty(true);
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#insert(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean insert(AbstractDBElement at) {
		return getStorage().insert((SLEXMMClassifierAttribute) at);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#update(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean update(AbstractDBElement at) {
		return getStorage().update((SLEXMMClassifierAttribute) at);
	}

//	/* (non-Javadoc)
//	 * @see java.lang.Object#hashCode()
//	 */
//	@Override
//	public int hashCode() {
//		return ("classifier_attribute#"+getId()).hashCode();
//	}
}
