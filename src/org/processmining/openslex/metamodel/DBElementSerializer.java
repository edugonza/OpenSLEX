package org.processmining.openslex.metamodel;

import java.io.IOException;
import java.io.Serializable;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;
import org.processmining.openslex.utils.MMUtils;

public class DBElementSerializer implements Serializer<AbstractDBElement>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 495273570262027504L;
	private SLEXMMStorageMetaModel storage;
	
	public DBElementSerializer(SLEXMMStorageMetaModel storage) {
		this.storage = storage;
	}
	
    @Override
    public void serialize(DataOutput2 out, AbstractDBElement value) throws IOException {
        
    	int cId = value.getClazzId();
    	out.writeInt(cId);
    	out.writeInt(value.getId());
    	
    	switch (cId) {
    	case AbstractDBElement.DM:
    		SLEXMMDataModel dm = (SLEXMMDataModel) value;
    		out.writeUTF(MMUtils.str(dm.getName()));
    		break;
		case AbstractDBElement.ACT:
			SLEXMMActivity act = (SLEXMMActivity) value;	
			out.writeUTF(MMUtils.str(act.getName()));
			break;
		case AbstractDBElement.AI:
			SLEXMMActivityInstance ai = (SLEXMMActivityInstance) value;
			out.writeInt(ai.getActivityId());
			break;
		case AbstractDBElement.AT:
			SLEXMMAttribute at = (SLEXMMAttribute) value;
			out.writeInt(at.getClassId());
			out.writeUTF(MMUtils.str(at.getName()));
			break;
		case AbstractDBElement.ATV:
			SLEXMMAttributeValue atv = (SLEXMMAttributeValue) value;
			out.writeInt(atv.getAttributeId());
			out.writeInt(atv.getObjectVersionId());
			out.writeUTF(MMUtils.str(atv.getType()));
			out.writeUTF(MMUtils.str(atv.getValue()));
			break;
		case AbstractDBElement.CL:
			SLEXMMClass cl = (SLEXMMClass) value;
			out.writeInt(cl.getDataModelId());
			out.writeUTF(MMUtils.str(cl.getName()));
			break;
		case AbstractDBElement.CLF:
			SLEXMMClassifier clf = (SLEXMMClassifier) value;
			out.writeUTF(MMUtils.str(clf.getName()));
			out.writeInt(clf.getLogId());
			break;
		case AbstractDBElement.CLFAT:
			SLEXMMClassifierAttribute clfat = (SLEXMMClassifierAttribute) value;
			out.writeInt(clfat.getClassifierId());
			out.writeInt(clfat.getEventAttributeNameId());
			break;
		case AbstractDBElement.CS:
			SLEXMMCase cs = (SLEXMMCase) value;
			out.writeUTF(MMUtils.str(cs.getName()));
			break;
		case AbstractDBElement.CSAT:
			SLEXMMCaseAttribute csat = (SLEXMMCaseAttribute) value;
			out.writeUTF(MMUtils.str(csat.getName()));
			break;
		case AbstractDBElement.CSATV:
			SLEXMMCaseAttributeValue csatv = (SLEXMMCaseAttributeValue) value;
			out.writeInt(csatv.getAttributeId());
			out.writeInt(csatv.getCaseId());
			out.writeUTF(MMUtils.str(csatv.getType()));
			out.writeUTF(MMUtils.str(csatv.getValue()));
			break;
		case AbstractDBElement.EAT:
			SLEXMMEventAttribute eat = (SLEXMMEventAttribute) value;
			out.writeUTF(MMUtils.str(eat.getName()));
			break;
		case AbstractDBElement.EATV:
			SLEXMMEventAttributeValue eatv = (SLEXMMEventAttributeValue) value;
			out.writeInt(eatv.getAttributeId());
			out.writeInt(eatv.getEventId());
			out.writeUTF(MMUtils.str(eatv.getType()));
			out.writeUTF(MMUtils.str(eatv.getValue()));
			break;
		case AbstractDBElement.EV:
			SLEXMMEvent ev = (SLEXMMEvent) value;
			out.writeInt(ev.getActivityInstanceId());
			out.writeInt(ev.getOrder());
			out.writeUTF(MMUtils.str(ev.getLifecycle()));
			out.writeUTF(MMUtils.str(ev.getResource()));
			out.writeLong(ev.getTimestamp());
			break;
		case AbstractDBElement.LG:
			SLEXMMLog log = (SLEXMMLog) value;
			out.writeInt(log.getProcessId());
			out.writeUTF(MMUtils.str(log.getName()));
			break;
		case AbstractDBElement.LGAT:
			SLEXMMLogAttribute lgat = (SLEXMMLogAttribute) value;
			out.writeUTF(MMUtils.str(lgat.getName()));
			break;
		case AbstractDBElement.LGATV:
			SLEXMMLogAttributeValue lgatv = (SLEXMMLogAttributeValue) value;
			out.writeInt(lgatv.getAttributeId());
			out.writeInt(lgatv.getLogId());
			out.writeUTF(MMUtils.str(lgatv.getType()));
			out.writeUTF(MMUtils.str(lgatv.getValue()));
			break;
		case AbstractDBElement.OBJ:
			SLEXMMObject obj = (SLEXMMObject) value;
			out.writeInt(obj.getClassId());
			break;
		case AbstractDBElement.OV:
			SLEXMMObjectVersion ov = (SLEXMMObjectVersion) value;
			out.writeInt(ov.getObjectId());
			out.writeLong(ov.getStartTimestamp());
			out.writeLong(ov.getEndTimestamp());
			break;
		case AbstractDBElement.PS:
			SLEXMMProcess ps = (SLEXMMProcess) value;
			out.writeUTF(MMUtils.str(ps.getName()));
			break;
		case AbstractDBElement.REL:
			SLEXMMRelation rel = (SLEXMMRelation) value;
			out.writeInt(rel.getRelationshipId());
			out.writeInt(rel.getSourceObjectVersionId());
			out.writeInt(rel.getTargetObjectVersionId());
			out.writeLong(rel.getStartTimestamp());
			out.writeLong(rel.getEndTimestamp());
			break;
		case AbstractDBElement.RS:
			SLEXMMRelationship rs = (SLEXMMRelationship) value;
			out.writeUTF(MMUtils.str(rs.getName()));
			out.writeInt(rs.getSourceClassId());
			out.writeInt(rs.getTargetClassId());
			break;
		case AbstractDBElement.PER:
			SLEXMMPeriod per = (SLEXMMPeriod) value;
			out.writeLong(per.getStart());
			out.writeLong(per.getEnd());
			break;
		default:
			break;
		}
    }

    @Override
    public AbstractDBElement deserialize(DataInput2 in, int available) throws IOException {
    	AbstractDBElement o = null;
    	
    	int cId = in.readInt();
    	int id = in.readInt();
    	
    	switch (cId) {
		case AbstractDBElement.DM:
			String nameDM = in.readUTF();
			SLEXMMDataModel dm = new SLEXMMDataModel(storage);
			dm.setName(nameDM);
			o = dm;
			break;
		case AbstractDBElement.ACT:
			String nameACT = in.readUTF();
			SLEXMMActivity act = new SLEXMMActivity(storage, nameACT);
			o = act;
			break;
		case AbstractDBElement.AI:
			SLEXMMActivityInstance ai = new SLEXMMActivityInstance(storage);
			ai.setActivityId(in.readInt());
			o = ai;
			break;
		case AbstractDBElement.AT:
			SLEXMMAttribute at = new SLEXMMAttribute(storage);
			at.setClassId(in.readInt());
			at.setName(in.readUTF());
			o = at;
			break;
		case AbstractDBElement.ATV:
			int attributeIdATV = in.readInt();
			int objectVersionATV = in.readInt();
			SLEXMMAttributeValue atv = new SLEXMMAttributeValue(storage,
					attributeIdATV,
					objectVersionATV);
			atv.setType(in.readUTF());
			atv.setValue(in.readUTF());
			o = atv;
			break;
		case AbstractDBElement.CL:
			int datamodelCL = in.readInt();
			String nameCL = in.readUTF();
			SLEXMMClass cl = new SLEXMMClass(storage,
					nameCL,
					datamodelCL);
			o = cl;
			break;
		case AbstractDBElement.CLF:
			String nameCLF = in.readUTF();
			int logIdCLF = in.readInt();
			SLEXMMClassifier clf = new SLEXMMClassifier(storage,
					nameCLF,
					logIdCLF);
			o = clf;
			break;
		case AbstractDBElement.CLFAT:
			SLEXMMClassifierAttribute clfat = new SLEXMMClassifierAttribute(storage);
			clfat.setClassifierId(in.readInt());
			clfat.setEventAttributeNameId(in.readInt());
			o = clfat;
			break;
		case AbstractDBElement.CS:
			SLEXMMCase cs = new SLEXMMCase(storage);
			cs.setName(in.readUTF());
			o = cs;
			break;
		case AbstractDBElement.CSAT:
			SLEXMMCaseAttribute csat = new SLEXMMCaseAttribute(storage);
			csat.setName(in.readUTF());
			o = csat;
			break;
		case AbstractDBElement.CSATV:
			int attributeIdCSATV = in.readInt();
			int caseIdCSATV = in.readInt();
			SLEXMMCaseAttributeValue csatv = new SLEXMMCaseAttributeValue(storage,
					attributeIdCSATV,
					caseIdCSATV);
			csatv.setType(in.readUTF());
			csatv.setValue(in.readUTF());
			o = csatv;
			break;
		case AbstractDBElement.EAT:
			SLEXMMEventAttribute eat = new SLEXMMEventAttribute(storage);
			eat.setName(in.readUTF());
			o = eat;
			break;
		case AbstractDBElement.EATV:
			int attributeIdEATV= in.readInt();
			int eventIdEATV= in.readInt();
			SLEXMMEventAttributeValue eatv = new SLEXMMEventAttributeValue(storage,
					attributeIdEATV,
					eventIdEATV);
			eatv.setType(in.readUTF());
			eatv.setValue(in.readUTF());
			o = eatv;
			break;
		case AbstractDBElement.EV:
			SLEXMMEvent ev = new SLEXMMEvent(storage);
			ev.setActivityInstanceId(in.readInt());
			ev.setOrder(in.readInt());
			ev.setLifecycle(in.readUTF());
			ev.setResource(in.readUTF());
			ev.setTimestamp(in.readLong());
			o = ev;
			break;
		case AbstractDBElement.LG:
			SLEXMMLog log = new SLEXMMLog(storage);
			log.setProcessId(in.readInt());
			log.setName(in.readUTF());
			o = log;
			break;
		case AbstractDBElement.LGAT:
			SLEXMMLogAttribute lgat = new SLEXMMLogAttribute(storage);
			lgat.setName(in.readUTF());
			o = lgat;
			break;
		case AbstractDBElement.LGATV:
			int attributeIdLGATV = in.readInt();
			int logIdLGATV = in.readInt();
			SLEXMMLogAttributeValue lgatv = new SLEXMMLogAttributeValue(storage,
					attributeIdLGATV,
					logIdLGATV);
			lgatv.setType(in.readUTF());
			lgatv.setValue(in.readUTF());
			o = lgatv;
			break;
		case AbstractDBElement.OBJ:
			SLEXMMObject obj = new SLEXMMObject(storage);
			obj.setClassId(in.readInt());
			o = obj;
			break;
		case AbstractDBElement.OV:
			SLEXMMObjectVersion ov = new SLEXMMObjectVersion(storage);
			ov.setObjectId(in.readInt());
			ov.setStartTimestamp(in.readLong());
			ov.setEndTimestamp(in.readLong());
			o = ov;
			break;
		case AbstractDBElement.PS:
			SLEXMMProcess ps = new SLEXMMProcess(storage);
			ps.setName(in.readUTF());
			o = ps;
			break;
		case AbstractDBElement.REL:
			SLEXMMRelation rel = new SLEXMMRelation(storage);
			rel.setRelationshipId(in.readInt());
			rel.setSourceObjectVersionId(in.readInt());
			rel.setTargetObjectVersionId(in.readInt());
			rel.setStartTimestamp(in.readLong());
			rel.setEndTimestamp(in.readLong());
			o = rel;
			break;
		case AbstractDBElement.RS:
			SLEXMMRelationship rs = new SLEXMMRelationship(storage);
			rs.setName(in.readUTF());
			rs.setSourceClassId(in.readInt());
			rs.setTargetClassId(in.readInt());
			o = rs;
			break;
		case AbstractDBElement.PER:
			long start = in.readLong();
			long end = in.readLong();
			SLEXMMPeriod per = new SLEXMMPeriod(storage, start, end);
			o = per;
			break;
		default:
			break;
		}
    	
    	o.setId(id);
    	o.setDirty(false);
    	o.setInserted(true);
    	
    	return o;
    }

}