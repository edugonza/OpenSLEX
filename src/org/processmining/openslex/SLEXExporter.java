package org.processmining.openslex;

import java.util.Map.Entry;

import org.deckfour.xes.classification.XEventAttributeClassifier;
import org.deckfour.xes.extension.XExtension;
import org.deckfour.xes.extension.XExtensionManager;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeLiteralImpl;
import org.progressmining.xeslite.external.XFactoryExternalImpl;

public class SLEXExporter {
	
	public static XLog exportPerspectiveToXLog(SLEXPerspective p) {
		XLog xlog = null;
		SLEXStoragePerspective storage = p.getStorage();
		try {
			SLEXStorageCollection storageEvCol = new SLEXStorageImpl(storage.getPath(),p.getCollectionFileName(),SLEXStorage.TYPE_COLLECTION);
			//XFactory xfactory = XFactoryRegistry.instance().currentDefault();
			XFactory xfactory = new XFactoryExternalImpl.MapDBDiskImpl();
			xlog = xfactory.createLog();
		
			SLEXAttribute table_nameAttr = storageEvCol.findAttribute("COMMON", "TABLE_NAME");
			SLEXAttribute operationAttr = storageEvCol.findAttribute("COMMON", "OPERATION");
			SLEXAttribute column_changesAttr = storageEvCol.findAttribute("COMMON", "COLUMN_CHANGES");
			String[] classifier1Keys = new String[] {table_nameAttr.toString(),operationAttr.toString(),column_changesAttr.toString()};
			String[] classifier2Keys = new String[] {table_nameAttr.toString(),operationAttr.toString()};
			xlog.getClassifiers().add(new XEventAttributeClassifier("Activity with changes vector", classifier1Keys));
			xlog.getClassifiers().add(new XEventAttributeClassifier("Activity without changes vector", classifier2Keys));
		
			SLEXTraceResultSet trset = p.getTracesResultSet();
			SLEXTrace t = null;
		
			while ((t = trset.getNext()) != null) {
			
				XAttributeMap tAttrMap = xfactory.createAttributeMap();
				XAttributeLiteral caseIdAttr = xfactory.createAttributeLiteral("concept:name", t.getCaseId(), XConceptExtension.instance());
				XAttributeLiteral idAttr = xfactory.createAttributeLiteral("Id", String.valueOf(t.getId()), null);
				tAttrMap.put("caseId", caseIdAttr);
				tAttrMap.put("Id", idAttr);
				XTrace xt = xfactory.createTrace(tAttrMap);
			
				xlog.add(xt);
			
				SLEXEventResultSet erset = t.getEventsResultSet();
				SLEXEvent e = null;
			
				while ((e = erset.getNext()) != null) {
					XAttributeMap eAttrMap = xfactory.createAttributeMap();
					XEvent xe = xfactory.createEvent();
					XAttributeLiteral eidAttr = xfactory.createAttributeLiteral("Id", String.valueOf(e.getId()), null);
					eAttrMap.put("Id", eidAttr);
				
					for (Entry<SLEXAttribute,SLEXAttributeValue> entry: e.getAttributeValues().entrySet()) {
						String keyStr = entry.getKey().toString();
						String valStr = entry.getValue().getValue();
						if (keyStr == null) {
							keyStr = "";
						}
						if (valStr == null) {
							valStr = "";
						}
						eAttrMap.put(entry.getKey().toString(), xfactory.createAttributeLiteral(keyStr, valStr, null));
					}
					xe.setAttributes(eAttrMap);
					xt.add(xe);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xlog;
	}
}
