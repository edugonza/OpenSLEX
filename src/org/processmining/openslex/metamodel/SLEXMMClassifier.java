/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.util.List;

import org.processmining.openslex.utils.MMUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMClassifier.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMClassifier extends AbstractDBElement {

	/** The name. */
	private String name = null;
	
	/** The log id. */
	private int logId = -1;
	
	/** The attributes. */
	private List<SLEXMMClassifierAttribute> attributes = null;
	
	/**
	 * Instantiates a new SLEXMM classifier.
	 *
	 * @param storage the storage
	 * @param name the name
	 * @param logId the log id
	 */
	protected SLEXMMClassifier(SLEXMMStorageMetaModel storage, String name, int logId) {
		super(storage);
		setName(name);
		setLogId(logId);
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
	 * Gets the log id.
	 *
	 * @return the log id
	 */
	public int getLogId() {
		return this.logId;
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
	
	/**
	 * Sets the log id.
	 *
	 * @param logId the new log id
	 */
	protected void setLogId(int logId) {
		this.logId = logId;
		setDirty(true);
	}
	
	/**
	 * Retrieve attributes.
	 */
	protected void retrieveAttributes() {		
		this.attributes = getStorage().getListAttributesForClassifier(this);
	}
	
	/**
	 * Gets the attributes.
	 *
	 * @return the attributes
	 */
	public List<SLEXMMClassifierAttribute> getAttributes() {
		if (this.attributes == null) {
			retrieveAttributes();
		}
		
		return this.attributes;
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#insert(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean insert(AbstractDBElement cl) {
		return getStorage().insert((SLEXMMClassifier) cl);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#update(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean update(AbstractDBElement cl) {
		return getStorage().update((SLEXMMClassifier) cl);
	}

//	/* (non-Javadoc)
//	 * @see java.lang.Object#hashCode()
//	 */
//	@Override
//	public int hashCode() {
//		return ("classifier#"+getId()).hashCode();
//	}
}
