/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMEventResultSet.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de
 *         Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target=
 *      "_blank">OpenSLEX</a>
 */
public class SLEXMMEventResultSet extends
		AbstractRSetWithAtts<SLEXMMEvent, SLEXMMEventAttribute, SLEXMMEventAttributeValue> {
	
	/**
	 * Instantiates a new SLEXMM event result set.
	 *
	 * @param storage the storage
	 * @param rset the rset
	 */
	public SLEXMMEventResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset, SLEXMMEvent.class, SLEXMMEventAttribute.class);
	}
	

	@Override
	protected SLEXMMEvent createElementFromRset() throws Exception {
		SLEXMMEvent ev = new SLEXMMEvent((SLEXMMStorageMetaModel) storage);
		int id = this.rset.getInt("id");
		int activityInstanceId = this.rset.getInt("activity_instance_id");
		int order = this.rset.getInt("ordering");
		String lifecycle = this.rset.getString("lifecycle");
		String resource = this.rset.getString("resource");
		long timestamp = this.rset.getLong("timestamp");
		ev.setId(id);
		ev.setActivityInstanceId(activityInstanceId);
		ev.setOrder(order);
		ev.setTimestamp(timestamp);
		ev.setLifecycle(lifecycle);
		ev.setResource(resource);
		return ev;
	}

	@Override
	protected SLEXMMEventAttribute createAttributeFromRset() throws Exception {
		SLEXMMEventAttribute at = new SLEXMMEventAttribute((SLEXMMStorageMetaModel) storage);
		at.setId(rset.getInt("atId"));
		at.setName(rset.getString("atName"));
		return at;
	}

	@Override
	protected SLEXMMEventAttributeValue createValueFromRset(int atid, int eid) throws Exception {
		SLEXMMEventAttributeValue atv = new SLEXMMEventAttributeValue((SLEXMMStorageMetaModel) storage, atid, eid);
		atv.setId(rset.getInt("atvId"));
		atv.setValue(rset.getString("atValue"));
		atv.setType(rset.getString("atType"));
		return atv;
	}
	
}
