/*
 * 
 */
package org.processmining.openslex.metamodel;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMRelationship.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMRelationship extends SLEXMMAbstractDatabaseObject {

	/** The name. */
	private String name = null;
	
	/** The source class id. */
	private int sourceClassId = -1;
	
	/** The target class id. */
	private int targetClassId = -1;
	
	/**
	 * Instantiates a new SLEXMM relationship.
	 *
	 * @param storage the storage
	 */
	protected SLEXMMRelationship(SLEXMMStorageMetaModel storage) {
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
	 * Gets the source class id.
	 *
	 * @return the source class id
	 */
	public int getSourceClassId() {
		return this.sourceClassId;
	}
	
	/**
	 * Gets the target class id.
	 *
	 * @return the target class id
	 */
	public int getTargetClassId() {
		return this.targetClassId;
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
	 * Sets the source class id.
	 *
	 * @param id the new source class id
	 */
	protected void setSourceClassId(int id) {
		this.sourceClassId = id;
		setDirty(true);
	}
	
	/**
	 * Sets the target class id.
	 *
	 * @param id the new target class id
	 */
	protected void setTargetClassId(int id) {
		this.targetClassId = id;
		setDirty(true);
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
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#insert(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean insert(SLEXMMAbstractDatabaseObject at) {
		return getStorage().insert((SLEXMMRelationship) at);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#update(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean update(SLEXMMAbstractDatabaseObject at) {
		return getStorage().update((SLEXMMRelationship) at);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return ("relationship#"+getId()).hashCode();
	}
}
