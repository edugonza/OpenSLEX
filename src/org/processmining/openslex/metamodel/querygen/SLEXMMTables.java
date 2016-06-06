package org.processmining.openslex.metamodel.querygen;

public enum SLEXMMTables {
	
	T_CLASS("class"),
	T_DATAMODEL("datamodel"),
	T_OBJECT("object"),
	T_OBJECT_VERSION("object_version"),
	T_LOG("log"),
	T_CASE("case"),
	T_CASE_TO_LOG("case_to_log"),
	T_PROCESS("process"),
	T_ACTIVITY_TO_PROCESS("activity_to_process"),
	T_ACTIVITY("activity"),
	T_ACTIVITY_INSTANCE("activity_instance"),
	T_ACTIVITY_INSTANCE_TO_CASE("activity_instance_to_case"),
	T_EVENT("event"),
	T_EVENT_TO_OBJECT_VERSION("event_to_object_version"),
	T_ATTRIBUTE_NAME("attribute_name"),
	T_RELATIONSHIP("relationship"),
	T_RELATION("relation"),
	T_ATTRIBUTE_VALUE("attribute_value"),
	T_EVENT_ATTRIBUTE_VALUE("event_attribute_value"),
	T_EVENT_ATTRIBUTE_NAME("event_attribute_name"),
	T_CASE_ATTRIBUTE_NAME("case_attribute_name"),
	T_CASE_ATTRIBUTE_VALUE("case_attribute_value"),
	T_LOG_ATTRIBUTE_NAME("log_attribute_name"),
	T_LOG_ATTRIBUTE_VALUE("log_attribute_value"),
	T_CLASSIFIER("classifier"),
	T_CLASSIFIER_ATTRIBUTES("classifier_attributes");
	
	private String name = null;

	private SLEXMMTables(String s) {
	        name = s;
	    }

	public boolean equalsName(String otherName) {
		return (otherName == null) ? false : name.equals(otherName);
	}

	public String toString() {
		return this.name;
	}
}
