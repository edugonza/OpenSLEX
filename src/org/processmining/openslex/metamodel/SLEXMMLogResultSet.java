/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMLogResultSet.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMLogResultSet extends AbstractRSetWithAtts<SLEXMMLog,SLEXMMLogAttribute,SLEXMMLogAttributeValue> {
	
	/**
	 * Instantiates a new SLEXMM log result set.
	 *
	 * @param storage the storage
	 * @param rset the rset
	 */
	public SLEXMMLogResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset, SLEXMMLog.class, SLEXMMLogAttribute.class);
	}
	
	@Override
	protected SLEXMMLog createElementFromRset() throws Exception {
		int id = this.rset.getInt("id");
		String name = this.rset.getString("name");
		int process_id = this.rset.getInt("process_id");
		SLEXMMLog log = new SLEXMMLog((SLEXMMStorageMetaModel) storage);
		log.setId(id);
		log.setName(name);
		log.setProcessId(process_id);
		return log;
	}

	@Override
	protected SLEXMMLogAttribute createAttributeFromRset() throws Exception {
		SLEXMMLogAttribute at = new SLEXMMLogAttribute((SLEXMMStorageMetaModel) storage);
		at.setId(rset.getInt("atId"));
		at.setName(rset.getString("atName"));
		return at;
	}

	@Override
	protected SLEXMMLogAttributeValue createValueFromRset(int atid, int eid) throws Exception {
		SLEXMMLogAttributeValue atv = new SLEXMMLogAttributeValue((SLEXMMStorageMetaModel) storage, atid, eid);
		atv.setId(rset.getInt("atvId"));
		atv.setValue(rset.getString("atValue"));
		atv.setType(rset.getString("atType"));
		return atv;
	}
	
}