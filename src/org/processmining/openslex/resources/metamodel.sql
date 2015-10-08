--
-- File generated with SQLiteStudio v3.0.6 on Thu Oct 8 16:46:48 2015
--
-- Text encoding used: UTF-8
--
PRAGMA foreign_keys = off;

-- Table: relationship
CREATE TABLE IF NOT EXISTS relationship (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, source REFERENCES class (id), target REFERENCES class (id));

-- Table: pcase
CREATE TABLE IF NOT EXISTS pcase (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);

-- Table: activity_instance
CREATE TABLE IF NOT EXISTS activity_instance (id INTEGER PRIMARY KEY AUTOINCREMENT, activity_id REFERENCES activity (id));

-- Table: event_attribute_name
CREATE TABLE IF NOT EXISTS event_attribute_name (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);

-- Table: relation
CREATE TABLE IF NOT EXISTS relation (id INTEGER PRIMARY KEY AUTOINCREMENT, source_object_version_id REFERENCES event (id), target_object_version_id REFERENCES event (id), relationship_id REFERENCES relationship (id), start_timestamp INTEGER, end_timestamp INTEGER);

-- Table: object
CREATE TABLE IF NOT EXISTS object (id INTEGER PRIMARY KEY AUTOINCREMENT, class_id REFERENCES class (id));

-- Table: activity
CREATE TABLE IF NOT EXISTS activity (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, process_id REFERENCES process (id));

-- Table: event
CREATE TABLE IF NOT EXISTS event (id INTEGER PRIMARY KEY AUTOINCREMENT, activity_instance_id REFERENCES activity_instance (id), ordering INTEGER, timestamp INTEGER, lifecycle STRING, resource STRING);

-- Table: attribute_value
CREATE TABLE IF NOT EXISTS attribute_value (id INTEGER PRIMARY KEY AUTOINCREMENT, object_version_id REFERENCES event (id), attribute_name_id REFERENCES attribute_name (id), value TEXT, type TEXT);

-- Table: event_attribute_value
CREATE TABLE IF NOT EXISTS event_attribute_value (id INTEGER PRIMARY KEY AUTOINCREMENT, event_id REFERENCES event (id), event_attribute_name_id REFERENCES event_attribute_name (id), value TEXT, type TEXT);

-- Table: event_to_object_version
CREATE TABLE IF NOT EXISTS event_to_object_version (event_id REFERENCES event (id) NOT NULL, object_version_id REFERENCES object_version (id) NOT NULL, label TEXT, PRIMARY KEY (event_id, object_version_id));

-- Table: class
CREATE TABLE IF NOT EXISTS class (id INTEGER PRIMARY KEY AUTOINCREMENT, datamodel_id REFERENCES datamodel (id), name TEXT NOT NULL);

-- Table: datamodel
CREATE TABLE IF NOT EXISTS datamodel (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);

-- Table: activity_instance_to_case
CREATE TABLE IF NOT EXISTS activity_instance_to_case (case_id REFERENCES pcase (id) NOT NULL, activity_instance_id REFERENCES activity_instance (id) NOT NULL, PRIMARY KEY (case_id, activity_instance_id));

-- Table: attribute_name
CREATE TABLE IF NOT EXISTS attribute_name (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, class_id REFERENCES class (id));

-- Table: process
CREATE TABLE IF NOT EXISTS process (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);

-- Table: object_version
CREATE TABLE IF NOT EXISTS object_version (id INTEGER PRIMARY KEY AUTOINCREMENT, object_id REFERENCES object (id), start_timestamp INTEGER, end_timestamp INTEGER);

PRAGMA foreign_keys = on;

