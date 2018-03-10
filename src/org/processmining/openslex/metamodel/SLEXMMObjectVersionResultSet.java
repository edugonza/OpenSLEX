package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

public class SLEXMMObjectVersionResultSet extends
	AbstractRSetWithAtts
	<SLEXMMObjectVersion,
	SLEXMMAttribute,
	SLEXMMAttributeValue>
{

	public SLEXMMObjectVersionResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset, SLEXMMObjectVersion.class, SLEXMMAttribute.class);
	}

	@Override
	protected SLEXMMObjectVersion createElementFromRset() throws Exception {
		SLEXMMObjectVersion ov = new SLEXMMObjectVersion((SLEXMMStorageMetaModel) storage);
		int id = this.rset.getInt("id");
		int objectId = this.rset.getInt("object_id");
		long startTimestamp = this.rset.getLong("start_timestamp");
		long endTimestamp = this.rset.getLong("end_timestamp");
		ov.setId(id);
		ov.setObjectId(objectId);
		ov.setStartTimestamp(startTimestamp);
		ov.setEndTimestamp(endTimestamp);
		ov.setId(id);
		return ov;
	}

	@Override
	protected SLEXMMAttribute createAttributeFromRset() throws Exception {
		SLEXMMAttribute at = new SLEXMMAttribute((SLEXMMStorageMetaModel) storage);
		at.setId(rset.getInt("atId"));
		at.setName(rset.getString("atName"));
		return at;
	}

	@Override
	protected SLEXMMAttributeValue createValueFromRset(int atid, int eid) throws Exception {
		SLEXMMAttributeValue atv = new SLEXMMAttributeValue((SLEXMMStorageMetaModel) storage, atid, eid);
		atv.setId(rset.getInt("atvId"));
		atv.setValue(rset.getString("atValue"));
		atv.setType(rset.getString("atType"));
		return atv;
	}
	
}