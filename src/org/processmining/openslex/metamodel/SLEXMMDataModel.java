/*
 * 
 */
package org.processmining.openslex.metamodel;


// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMDataModel.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMDataModel extends SLEXMMAbstractDatabaseObject {

	/** The name. */
	private String name = null;
	
	/**
	 * Instantiates a new SLEXMM data model.
	 *
	 * @param storage the storage
	 */
	public SLEXMMDataModel(SLEXMMStorage storage) {
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#insert(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean insert(SLEXMMAbstractDatabaseObject dm) {
		return getStorage().insert((SLEXMMDataModel) dm);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#update(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean update(SLEXMMAbstractDatabaseObject dm) {
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
		this.name = name;
		setDirty(true);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return ("datamodel#"+getId()).hashCode();
	}
}
