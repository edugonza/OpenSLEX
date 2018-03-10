/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.util.HashMap;

import org.processmining.openslex.utils.MMUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMEvent.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMEvent extends AbstractDBElementWithAtts<SLEXMMEventAttribute,SLEXMMEventAttributeValue> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5582256466655309436L;

	/** The activity instance id. */
	private int activityInstanceId = -1;
	
	/** The order. */
	private int order = -1;
	
	/** The lifecycle. */
	private String lifecycle = null;
	
	/** The timestamp. */
	private long timestamp = -1;
	
	/** The resource. */
	private String resource = null;
	
	/**
	 * Instantiates a new SLEXMM event.
	 *
	 * @param storage the storage
	 */
	protected SLEXMMEvent(SLEXMMStorageMetaModel storage) {
		super(storage);
	}
	
	/**
	 * Gets the activity instance id.
	 *
	 * @return the activity instance id
	 */
	public int getActivityInstanceId() {
		return this.activityInstanceId;
	}
	
	/**
	 * Sets the order.
	 *
	 * @param order the new order
	 */
	public void setOrder(int order) {
		this.order = order;
		setDirty(true);
	}
	
	/**
	 * Sets the activity instance id.
	 *
	 * @param id the new activity instance id
	 */
	public void setActivityInstanceId(int id) {
		this.activityInstanceId = id;
		setDirty(true);
	}
	
	/**
	 * Gets the order.
	 *
	 * @return the order
	 */
	public int getOrder() {
		return this.order;
	}
	
	/**
	 * Gets the lifecycle.
	 *
	 * @return the lifecycle
	 */
	public String getLifecycle() {
		return this.lifecycle;
	}
	
	/**
	 * Gets the resource.
	 *
	 * @return the resource
	 */
	public String getResource() {
		return this.resource;
	}
	
	/**
	 * Gets the timestamp.
	 *
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return this.timestamp;
	}
	
	/**
	 * Sets the lifecycle.
	 *
	 * @param lifecycle the new lifecycle
	 */
	protected void setLifecycle(String lifecycle) {
		this.lifecycle = MMUtils.intern(lifecycle);
		setDirty(true);
	}
	
	/**
	 * Sets the resource.
	 *
	 * @param resource the new resource
	 */
	protected void setResource(String resource) {
		this.resource = MMUtils.intern(resource);
		setDirty(true);
	}
	
	/**
	 * Sets the timestamp.
	 *
	 * @param timestamp the new timestamp
	 */
	protected void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
		setDirty(true);
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#insert(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean insert(AbstractDBElement e) {
		return getStorage().insert((SLEXMMEvent)e);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject#update(org.processmining.openslex.metamodel.SLEXMMAbstractDatabaseObject)
	 */
	@Override
	boolean update(AbstractDBElement e) {
		return getStorage().update((SLEXMMEvent)e);
	}

	@Override
	protected HashMap<SLEXMMEventAttribute, SLEXMMEventAttributeValue> queryAttributeValues() {
		return getStorage().getAttributeValuesForEvent(this);
	}
	
}
