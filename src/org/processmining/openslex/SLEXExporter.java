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
		//XFactory xfactory = XFactoryRegistry.instance().currentDefault();
		XFactory xfactory = new XFactoryExternalImpl.MapDBDiskImpl();
		XLog xlog = xfactory.createLog();
		String[] classifierKeys = new String[] {"1:5:TABLE_NAME","1:6:OPERATION","1:1:COLUMN_CHANGES"};
		xlog.getClassifiers().add(new XEventAttributeClassifier("Activity", classifierKeys));
		
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
		
		return xlog;
	}
}
