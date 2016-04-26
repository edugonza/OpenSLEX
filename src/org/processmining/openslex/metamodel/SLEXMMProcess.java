/*
 * 
 */
package org.processmining.openslex.metamodel;


// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMProcess.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMProcess extends SLEXMMAbstractDatabaseObject {

	/** The name. */
	private String name = null;
	
	/**
	 * Instantiates a new SLEXMM process.
	 *
	 * @param storage the storage
	 */
	public SLEXMMProcess(SLEXMMStorage storage) {
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
	 * Adds the.
	 *
	 * @param ac the ac
	 * @return true, if successful
	 */
	public boolean add(SLEXMMActivity ac) {
		return getStorage().addActivityToProcess(this.getId(),ac.getId());
	}
	
	/**
	 * Adds the.
	 *
	 * @param activityId the activity id
	 * @return true, if successful
	 */
	public boolean add(int activityId) {
		return getStorage().addActivityToProcess(this.getId(),activityId);
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#insert(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean insert(SLEXMMAbstractDatabaseObject dm) {
		return getStorage().insert((SLEXMMProcess) dm);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#update(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean update(SLEXMMAbstractDatabaseObject dm) {
		return getStorage().update((SLEXMMProcess) dm);
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
		this.name = name;
		setDirty(true);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return ("process#"+getId()).hashCode();
	}

}
