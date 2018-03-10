/*
 * 
 */
package org.processmining.openslex.metamodel;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMRelation.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMRelation extends AbstractDBElement {

	/** The source object version id. */
	private int sourceObjectVersionId = -1;
	
	/** The target object version id. */
	private int targetObjectVersionId = -1;
	
	/** The relationship id. */
	private int relationshipId = -1;
	
	/** The start timestamp. */
	private long startTimestamp = -1;
	
	/** The end timestamp. */
	private long endTimestamp = -1;
	
	/**
	 * Instantiates a new SLEXMM relation.
	 *
	 * @param storage the storage
	 */
	protected SLEXMMRelation(SLEXMMStorageMetaModel storage) {
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
	 * Gets the source object version id.
	 *
	 * @return the source object version id
	 */
	public int getSourceObjectVersionId() {
		return this.sourceObjectVersionId;
	}
	
	/**
	 * Sets the source object version id.
	 *
	 * @param id the new source object version id
	 */
	protected void setSourceObjectVersionId(int id) {
		this.sourceObjectVersionId = id;
		setDirty(true);
	}
	
	/**
	 * Gets the target object version id.
	 *
	 * @return the target object version id
	 */
	public int getTargetObjectVersionId() {
		return this.targetObjectVersionId;
	}
	
	/**
	 * Sets the target object version id.
	 *
	 * @param id the new target object version id
	 */
	protected void setTargetObjectVersionId(int id) {
		this.targetObjectVersionId = id;
		setDirty(true);
	}
	
	/**
	 * Gets the relationship id.
	 *
	 * @return the relationship id
	 */
	public int getRelationshipId() {
		return this.relationshipId;
	}
	
	/**
	 * Sets the relationship id.
	 *
	 * @param id the new relationship id
	 */
	protected void setRelationshipId(int id) {
		this.relationshipId = id;
		setDirty(true);
	}
	
	/**
	 * Gets the start timestamp.
	 *
	 * @return the start timestamp
	 */
	public long getStartTimestamp() {
		return this.startTimestamp;
	}
	
	/**
	 * Gets the end timestamp.
	 *
	 * @return the end timestamp
	 */
	public long getEndTimestamp() {
		return this.endTimestamp;
	}
	
	/**
	 * Sets the start timestamp.
	 *
	 * @param timestamp the new start timestamp
	 */
	protected void setStartTimestamp(long timestamp) {
		this.startTimestamp = timestamp;
		setDirty(true);
	}
	
	/**
	 * Sets the end timestamp.
	 *
	 * @param timestamp the new end timestamp
	 */
	protected void setEndTimestamp(long timestamp) {
		this.endTimestamp = timestamp;
		setDirty(true);
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#insert(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean insert(AbstractDBElement e) {
		return getStorage().insert((SLEXMMRelation)e);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#update(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean update(AbstractDBElement e) {
		return getStorage().update((SLEXMMRelation)e);
	}

//	/* (non-Javadoc)
//	 * @see java.lang.Object#hashCode()
//	 */
//	@Override
//	public int hashCode() {
//		return ("relation#"+getId()).hashCode();
//	}
}
