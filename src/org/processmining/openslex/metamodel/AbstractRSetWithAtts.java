/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMObjectVersionResultSet.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public abstract class AbstractRSetWithAtts
		<T extends AbstractDBElementWithAtts<AT,ATV>,
		 AT extends AbstractAttDBElement,
		 ATV extends AbstractDBElementWithValue>
extends AbstractRSetElement<T> {
		
	/**
	 * Instantiates a new SLEXMM result set object.
	 *
	 * @param storage the storage
	 * @param rset the rset
	 */
	public AbstractRSetWithAtts(SLEXMMStorageMetaModel storage, ResultSet rset, Class<T> type,
			Class<AT> attType) {
		super(storage, rset, type);
		this.attributeTypeClass = attType;
	}
	
	protected Class<AT> attributeTypeClass = null;
	
	protected Class<AT> getAttributeTypeClass() {
		return attributeTypeClass;
	}
	
	protected abstract T createElementFromRset()  throws Exception;
	
	protected abstract AT createAttributeFromRset()  throws Exception;
	
	protected abstract ATV createValueFromRset(int atid, int eid)  throws Exception;
	
	/**
	 * Gets the next.
	 *
	 * @return the next
	 */
	public T getNext() {
		T ev = null;
		try {
			if (this.rset != null && this.rset.next()) {
				int id = this.rset.getInt("id");
				ev = storage.getFromCache(this.getTypeClass(), id);
				if (ev == null) {
					ev = createElementFromRset();
					ev.setDirty(false);
					ev.setInserted(true);
					storage.putInCache(ev);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ev == null) {
				close();
			}
		}
		return ev;
	}
	
	private boolean doNext = true;
	
	@SuppressWarnings("unchecked")
	public T getNextWithAttributes() {
		T ev = null;
		try {
			boolean resNext = true;
			
			if (doNext) {
				resNext = this.rset.next();
			}
			
			if (this.rset != null && resNext) {
				
				int id = this.rset.getInt("id");
				boolean attsInCache = false;
				ev = storage.getFromCache(this.getTypeClass(), id);
				if (ev == null) {
					ev = createElementFromRset();
					ev.setDirty(false);
					ev.setInserted(true);
					storage.putInCache(ev);
				} else {
					HashMap<AT, ATV> map = (HashMap<AT, ATV>) storage.getAttsFromCache(this.getTypeClass(), id);
					HashMap<String, AT> mapNames = (HashMap<String, AT>) storage.getAttNamesFromCache(this.getTypeClass(), id);
					if (map == null || mapNames == null) {
						attsInCache = false;
					} else {
						attsInCache = true;
						ev.setAttributeValues(map,mapNames);
					}
				}
				
				if (!attsInCache) {
					/**/
					HashMap<AT, ATV> attributeValues = null;
					HashMap<String, AT> attributeNames = null;
					try {
						boolean stop = false;
						attributeValues = new HashMap<>();
						attributeNames = new HashMap<>();
						while (!stop) {
							int idaux = this.rset.getInt("id");
							if (idaux == id) {
								int atId = rset.getInt("atId");
								AT at = storage.getFromCache(this.getAttributeTypeClass(), atId);
								if (at == null) {
									at = createAttributeFromRset();
									at.setDirty(false);
									at.setInserted(true);
									storage.putInCache(at);
								}

								ATV atv = createValueFromRset(atId, id);
								atv.setDirty(false);
								atv.setInserted(true);
								attributeValues.put(at, atv);
								attributeNames.put(at.getName(), at);

								if (!this.rset.next()) {
									stop = true;
									doNext = true;
								}
							} else {
								stop = true;
								doNext = false;
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					/**/
					ev.setAttributeValues(attributeValues, attributeNames);
					ev.setDirty(false);
					ev.setInserted(true);
					storage.putAttsInCache(ev, (HashMap<AbstractAttDBElement, AbstractDBElementWithValue>) attributeValues);
					storage.putAttNamesInCache(ev, (HashMap<String, AbstractAttDBElement>) attributeNames);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ev == null) {
				close();
			}
		}
		return ev;
	}
}
