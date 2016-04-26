/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMClass.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMClass extends SLEXMMAbstractDatabaseObject {

	/** The name. */
	private String name = null;
	
	/** The datamodel id. */
	private int datamodelId = -1;
	
	/** The attributes. */
	private List<SLEXMMAttribute> attributes = null;
	
	/** The keys. */
	private List<SLEXMMRelationship> keys = null;
	
	/**
	 * Instantiates a new SLEXMM class.
	 *
	 * @param storage the storage
	 * @param name the name
	 * @param data_modelId the data_model id
	 */
	protected SLEXMMClass(SLEXMMStorageMetaModel storage, String name, int data_modelId) {
		super(storage);
		this.name = name;
		this.datamodelId = data_modelId;
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
	 * Gets the data model id.
	 *
	 * @return the data model id
	 */
	public int getDataModelId() {
		return this.datamodelId;
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
	
	/**
	 * Sets the data model id.
	 *
	 * @param datamodelId the new data model id
	 */
	protected void setDataModelId(int datamodelId) {
		this.datamodelId = datamodelId;
		setDirty(true);
	}
	
	/**
	 * Retrieve attributes and keys.
	 */
	protected void retrieveAttributesAndKeys() {
		getAttributes();
		getKeys();
	}
	
	/**
	 * Retrieve attributes.
	 */
	protected void retrieveAttributes() {		
		this.attributes = getStorage().getListAttributesForClass(this);
	}
	
	/**
	 * Retrieve relationships.
	 */
	protected void retrieveRelationships() {
		this.keys = getStorage().getRelationshipsForClass(this);
	}
	
	/**
	 * Gets the keys.
	 *
	 * @return the keys
	 */
	public List<SLEXMMRelationship> getKeys() {
		if (this.keys == null) {
			retrieveRelationships();
		}
		
		return this.keys;
	} 
	
	/**
	 * Gets the attributes.
	 *
	 * @return the attributes
	 */
	public List<SLEXMMAttribute> getAttributes() {
		if (this.attributes == null) {
			retrieveAttributes();
		}
		
		return this.attributes;
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#insert(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean insert(SLEXMMAbstractDatabaseObject cl) {
		return getStorage().insert((SLEXMMClass) cl);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#update(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean update(SLEXMMAbstractDatabaseObject cl) {
		return getStorage().update((SLEXMMClass) cl);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return ("class#"+getId()).hashCode();
	}
}
