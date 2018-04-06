/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMAbstractDatabaseObject.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public abstract class AbstractDBElement implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6582371605116436275L;

	/** The storage. */
	transient protected SLEXMMStorageMetaModel storage = null;
	
	/** The dirty. */
	private boolean dirty = true;
	
	/** The inserted. */
	private boolean inserted = false;
	
	/** The id. */
	private int id = -1;
	
	private String code = null;
//	
//	public <T extends SLEXMMAbstractDatabaseObject> T getFromCache(String uniqueId) {
//		return this.storage.getFromCache(uniqueId);
//	}
	
	public final static int ABS = 0;
	public final static int CL = 1;
	public final static int AT = 2;
	public final static int RS = 3;
	public final static int OBJ = 4;
	public final static int OV = 5;
	public final static int REL = 6;
	public final static int EV = 7;
	public final static int ATV = 8;
	public final static int EAT = 9;
	public final static int EATV = 10;
	public final static int AI = 11;
	public final static int CS = 12;
	public final static int LG = 13;
	public final static int CSAT = 14;
	public final static int CSATV = 15;
	public final static int LGAT = 16;
	public final static int LGATV = 17;
	public final static int ACT = 18;
	public final static int PS = 19;
	public final static int CLF = 20;
	public final static int CLFAT = 21;
	public final static int PER = 22;
	public final static int DM = 23;
	
	public int clazzId = ABS;
	
	public int getClazzId() {
		return clazzId;
	}
	
	public Class<?> getClazz() {
		return this.getClass();
	}
	
	public static int getClazzIdForClass(Class<?> c) {
		int clazzId = ABS;
		
		if (c == SLEXMMClass.class) {
			clazzId = CL;
		} else if (c == SLEXMMAttribute.class) {
			clazzId = AT;
		} else if (c == SLEXMMRelationship.class) {
			clazzId = RS;
		} else if (c == SLEXMMObject.class) {
			clazzId = OBJ;
		} else if (c == SLEXMMObjectVersion.class) {
			clazzId = OV;
		} else if (c == SLEXMMRelation.class) {
			clazzId = REL;
		} else if (c == SLEXMMEvent.class) {
			clazzId = EV;
		} else if (c == SLEXMMAttributeValue.class) {
			clazzId = ATV;
		} else if (c == SLEXMMEventAttribute.class) {
			clazzId = EAT;
		} else if (c == SLEXMMEventAttributeValue.class) {
			clazzId = EATV;
		} else if (c == SLEXMMActivityInstance.class) {
			clazzId = AI;
		} else if (c == SLEXMMCase.class) {
			clazzId = CS;
		} else if (c == SLEXMMLog.class) {
			clazzId = LG;
		} else if (c == SLEXMMCaseAttribute.class) {
			clazzId = CSAT;
		} else if (c == SLEXMMCaseAttributeValue.class) {
			clazzId = CSATV;
		} else if (c == SLEXMMLogAttribute.class) {
			clazzId = LGAT;
		} else if (c == SLEXMMLogAttributeValue.class) {
			clazzId = LGATV;
		} else if (c == SLEXMMActivity.class) {
			clazzId = ACT;
		} else if (c == SLEXMMProcess.class) {
			clazzId = PS;
		} else if (c == SLEXMMClassifier.class) {
			clazzId = CLF;
		} else if (c == SLEXMMClassifierAttribute.class) {
			clazzId = CLFAT;
		} else if (c == SLEXMMPeriod.class) {
			clazzId = PER;
		} else if (c == SLEXMMDataModel.class) {
			clazzId = DM;
		}
		
		return clazzId;
	}
	
	private void setClazzId() {
		Class<?> c = getClazz();
		clazzId = getClazzIdForClass(c);
	}
	
	@Override
	public int hashCode() {
		return getUniqueId().hashCode();
	}
	
	public static String computeUniqueId(Class<?> c, int id) {
		return String.valueOf(c).concat(String.valueOf(id)).intern();
	}
	
	public String getUniqueId() {
		if (this.code == null) {
			this.code = computeUniqueId(getClazz(), getId());
		}
		return code;
	}
	
	/**
	 * Instantiates a new SLEXMM abstract database object.
	 *
	 * @param storage the storage
	 */
	public AbstractDBElement(SLEXMMStorageMetaModel storage) {
		setStorage(storage);
		setClazzId();
	}
	
	protected void setStorage(SLEXMMStorageMetaModel storage) {
		this.storage = storage;
	}
	
	public SLEXMMStorageMetaModel getStorage() {
		return storage;
	}
	
	/**
	 * Sets the inserted.
	 *
	 * @param inserted the new inserted
	 */
	protected void setInserted(boolean inserted) {
		this.inserted = inserted;
	}
	
	/**
	 * Checks if is inserted.
	 *
	 * @return true, if is inserted
	 */
	protected boolean isInserted() {
		return this.inserted;
	}
	
	/**
	 * Checks if is dirty.
	 *
	 * @return true, if is dirty
	 */
	protected boolean isDirty() {
		return this.dirty;
	}
	
	/**
	 * Sets the dirty.
	 *
	 * @param dirty the new dirty
	 */
	protected void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	protected void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Insert.
	 *
	 * @param dbob the dbob
	 * @return true, if successful
	 */
	abstract boolean insert(AbstractDBElement dbob);
	
	/**
	 * Update.
	 *
	 * @param dbob the dbob
	 * @return true, if successful
	 */
	abstract boolean update(AbstractDBElement dbob);
	
	/**
	 * Commit.
	 *
	 * @return true, if successful
	 */
	public boolean commit() {
		
		if (!isInserted()) {
			if (insert(this)) {
				setInserted(true);
				setDirty(false);
			}
		} else if (isDirty()){
			if (update(this)) {
				setDirty(false);
			}
		}
		
		return !isDirty();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			return true;
		} else {
			if (this.hashCode() == obj.hashCode()) {
				if (obj instanceof AbstractDBElement) {
					if (this.getId() == ((AbstractDBElement) obj).getId()) {
						if (this.getClazz() == ((AbstractDBElement) obj).getClazz()) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
}
