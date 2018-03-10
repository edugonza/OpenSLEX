/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.util.HashMap;

import org.processmining.openslex.utils.MMUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMCase.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMCase extends AbstractDBElementWithAtts<SLEXMMCaseAttribute, SLEXMMCaseAttributeValue> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1589450152725134477L;
	/** The name. */
	private String name = null;
	
	/**
	 * Instantiates a new SLEXMM case.
	 *
	 * @param storage the storage
	 */
	protected SLEXMMCase(SLEXMMStorageMetaModel storage) {
		super(storage);
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
	public void setName(String name) {
		this.name = MMUtils.intern(name);
		setDirty(true);
	}
	
	/**
	 * Adds the.
	 *
	 * @param ai the ai
	 * @return true, if successful
	 */
	public boolean add(SLEXMMActivityInstance ai) {
		return getStorage().addActivityInstanceToCase(this,ai);
	}
	
	/**
	 * Adds the.
	 *
	 * @param activityInstanceId the activity instance id
	 * @return true, if successful
	 */
	public boolean add(int activityInstanceId) {
		return getStorage().addActivityInstanceToCase(this.getId(),activityInstanceId);
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#insert(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean insert(AbstractDBElement t) {
		return getStorage().insert((SLEXMMCase) t);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#update(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean update(AbstractDBElement t) {
		return getStorage().update((SLEXMMCase) t);
	}

	/**
	 * Gets the event result set.
	 *
	 * @return the event result set
	 */
	public SLEXMMEventResultSet getEventResultSet() {
		return getStorage().getEventsOfCase(this);
	}

	@Override
	protected HashMap<SLEXMMCaseAttribute, SLEXMMCaseAttributeValue> queryAttributeValues() {
		return getStorage().getAttributeValuesForCase(this);
	}
	
}
