--
-- File generated with SQLiteStudio v3.0.6 on Tue Oct 27 20:10:26 2015
--
-- Text encoding used: UTF-8
--
PRAGMA foreign_keys = off;

-- Table: event_attribute_value
CREATE TABLE IF NOT EXISTS event_attribute_value (id INTEGER PRIMARY KEY AUTOINCREMENT, event_id INTEGER REFERENCES activity_instance (id), event_attribute_name_id INTEGER REFERENCES event_attribute_name (id), value TEXT, type TEXT);

-- Table: attribute_value
CREATE TABLE IF NOT EXISTS attribute_value (id INTEGER PRIMARY KEY AUTOINCREMENT, object_version_id INTEGER REFERENCES activity_instance (id), attribute_name_id INTEGER REFERENCES attribute_name (id), value TEXT, type TEXT);

-- Table: event_attribute_name
CREATE TABLE IF NOT EXISTS event_attribute_name (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);

-- Table: datamodel
CREATE TABLE IF NOT EXISTS datamodel (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);

-- Table: event_to_object_version
CREATE TABLE IF NOT EXISTS event_to_object_version (event_id INTEGER REFERENCES activity_instance (id) NOT NULL, object_version_id INTEGER REFERENCES object_version (id) NOT NULL, label TEXT, PRIMARY KEY (event_id, object_version_id));

-- Table: relationship
CREATE TABLE IF NOT EXISTS relationship (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, source INTEGER REFERENCES class (id), target INTEGER REFERENCES class (id));

-- Table: pcase
CREATE TABLE IF NOT EXISTS pcase (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);

-- Table: event
CREATE TABLE IF NOT EXISTS event (id INTEGER PRIMARY KEY AUTOINCREMENT, activity_instance_id INTEGER REFERENCES activity_instance (id), ordering INTEGER, timestamp INTEGER, lifecycle STRING, resource STRING);

-- Table: activity_instance
CREATE TABLE IF NOT EXISTS activity_instance (id INTEGER PRIMARY KEY AUTOINCREMENT, activity_id INTEGER REFERENCES activity (id));

-- Table: attribute_name
CREATE TABLE IF NOT EXISTS attribute_name (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, class_id INTEGER REFERENCES class (id));

-- Table: object_version
CREATE TABLE IF NOT EXISTS object_version (id INTEGER PRIMARY KEY AUTOINCREMENT, object_id INTEGER REFERENCES object (id), start_timestamp INTEGER, end_timestamp INTEGER);

-- Table: activity
CREATE TABLE IF NOT EXISTS activity (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, process_id INTEGER REFERENCES process (id));

-- Table: process
CREATE TABLE IF NOT EXISTS process (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);

-- Table: class
CREATE TABLE IF NOT EXISTS class (id INTEGER PRIMARY KEY AUTOINCREMENT, datamodel_id INTEGER REFERENCES datamodel (id), name TEXT NOT NULL);

-- Table: relation
CREATE TABLE IF NOT EXISTS relation (id INTEGER PRIMARY KEY AUTOINCREMENT, source_object_version_id INTEGER REFERENCES activity_instance (id), target_object_version_id INTEGER REFERENCES activity_instance (id), relationship_id INTEGER REFERENCES relationship (id), start_timestamp INTEGER, end_timestamp INTEGER);

-- Table: activity_instance_to_case
CREATE TABLE IF NOT EXISTS activity_instance_to_case (case_id INTEGER REFERENCES pcase (id) NOT NULL, activity_instance_id INTEGER REFERENCES activity_instance (id) NOT NULL, PRIMARY KEY (case_id, activity_instance_id));

-- Table: object
CREATE TABLE IF NOT EXISTS object (id INTEGER PRIMARY KEY AUTOINCREMENT, class_id REFERENCES class (id));

PRAGMA foreign_keys = on;
