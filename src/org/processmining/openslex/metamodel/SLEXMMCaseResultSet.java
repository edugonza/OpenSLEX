/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.sql.ResultSet;

// TODO: Auto-generated Javadoc
/**
 * The Class SLEXMMCaseResultSet.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de
 *         Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target=
 *      "_blank">OpenSLEX</a>
 */
public class SLEXMMCaseResultSet
		extends AbstractRSetWithAtts<SLEXMMCase, SLEXMMCaseAttribute, SLEXMMCaseAttributeValue> {

	/**
	 * Instantiates a new SLEXMM case result set.
	 *
	 * @param storage the storage
	 * @param rset the rset
	 */
	public SLEXMMCaseResultSet(SLEXMMStorageMetaModel storage, ResultSet rset) {
		super(storage, rset, SLEXMMCase.class, SLEXMMCaseAttribute.class);
	}
	
	@Override
	protected SLEXMMCase createElementFromRset() throws Exception {
		int id = this.rset.getInt("id");
		String name = this.rset.getString("name");
		SLEXMMCase t = new SLEXMMCase((SLEXMMStorageMetaModel) storage);
		t.setId(id);
		t.setName(name);
		return t;
	}

	@Override
	protected SLEXMMCaseAttribute createAttributeFromRset() throws Exception {
		SLEXMMCaseAttribute at = new SLEXMMCaseAttribute((SLEXMMStorageMetaModel) storage);
		at.setId(rset.getInt("atId"));
		at.setName(rset.getString("atName"));
		return at;
	}

	@Override
	protected SLEXMMCaseAttributeValue createValueFromRset(int atid, int eid) throws Exception {
		SLEXMMCaseAttributeValue atv = new SLEXMMCaseAttributeValue((SLEXMMStorageMetaModel) storage, atid, eid);
		atv.setId(rset.getInt("atvId"));
		atv.setValue(rset.getString("atValue"));
		atv.setType(rset.getString("atType"));
		return atv;
	}
	
}
