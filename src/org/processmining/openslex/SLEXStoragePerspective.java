package org.processmining.openslex;

public interface SLEXStoragePerspective extends SLEXStorage {

	public abstract SLEXPerspectiveResultSet getPerspectives();

	public abstract SLEXPerspectiveResultSet getPerspectivesOfCollection(
			SLEXEventCollection ec);

	//public abstract SLEXTrace createTrace(int perspectiveId, String caseId);
	
	public abstract SLEXTrace createTrace(int traceId, int perspectiveId, String caseId);

	public abstract SLEXPerspective createPerspective(
			SLEXEventCollection evCol, String name);

	public abstract SLEXTrace cloneTrace(SLEXTrace t, int newTraceId);

	public abstract SLEXTraceResultSet getTracesOfPerspective(
			SLEXPerspective slexPerspective);

	public abstract boolean insert(SLEXPerspective p);

	public abstract boolean update(SLEXPerspective p);

	public abstract boolean removeTraceFromPerspective(
			SLEXPerspective slexPerspective, SLEXTrace t);

	public abstract boolean addEventToTrace(int traceId, int eventId);
	public abstract boolean addEventToTrace(SLEXTrace slexTrace, SLEXEvent e);

	public abstract boolean insert(SLEXTrace t);

	public abstract boolean update(SLEXTrace t);

	public abstract SLEXEventResultSet getEventsOfTrace(SLEXTrace slexTrace);

	public abstract int getNumberEventsOfTrace(SLEXTrace slexTrace);

	public abstract int getMaxTraceId();
	
}
