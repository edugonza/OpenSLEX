package org.processmining.openslex.metamodel;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.processmining.openslex.utils.ScriptRunner;

public class SLEXMMStorageMetaModelImpl implements SLEXMMStorageMetaModel {
	
	private static final String STORAGE_METAMODEL = PATH+File.separator+"metamodels.db";
	
	private static final String METAMODEL_ALIAS = "metamodeldb";
	
	private InputStream METAMODEL_SCHEMA_IN = SLEXMMStorage.class.getResourceAsStream("/org/processmining/openslex/resources/metamodel.sql");
	
	private static final String JOURNAL_MODE = "OFF";
	private static final String PRAGMA_SYNCHRONOUS_MODE = "OFF";
	private boolean metamodel_attached = false;
	
	private boolean autoCommitOnCreation = true;
	
	private String filename = null;
	private String path = null;	
	private Connection connection = null;
	
	public SLEXMMStorageMetaModelImpl(String path, String filename) throws Exception {
		init();
		this.filename = filename;
		this.path = path;
		openMetaModelStorage(path,filename);
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		disconnect();
	}
	
	@Override
	public String getFilename() {
		return filename;
	}
	
	@Override
	public String getPath() {
		return path;
	}
	
	@Override
	public int getType() {
		return TYPE_METAMODEL;
	}
	
	@Override
	public void setAutoCommit(boolean autoCommit) {
		try {
			this.connection.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void commit() {
		try {
			this.connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getCurrentWorkingDir() {
		return System.getProperty("user.dir");
	}
	
	public boolean openMetaModelStorage(String path, String filename) {
		if (metamodel_attached) {
			metamodel_attached = !detachDatabase(METAMODEL_ALIAS);
		}
		if (!metamodel_attached) {
			if (filename == null) {
				return false;
			}
			String fname = path+File.separator+filename;
			if (attachDatabaseFile(fname,METAMODEL_ALIAS,METAMODEL_SCHEMA_IN)) {
				metamodel_attached = true;
			}
			return metamodel_attached;
		} else {
			return false;
		}
	}
	
	private boolean checkSchema(String filename, String alias, InputStream schemaIn) {
		boolean result = false;
		Connection connAux = null;
		try {
			connAux = DriverManager.getConnection("jdbc:sqlite:"+filename);
			ScriptRunner scriptRunner = new ScriptRunner(connAux, false, false);
			scriptRunner.runScript(new InputStreamReader(schemaIn));
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			if (connAux != null) {
				try {
					connAux.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}
	
	private void closeStatement(Statement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void closeResultSet(ResultSet rset) {
		try {
			if (rset != null) {
				rset.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean attachDatabaseFile(String filename, String alias, InputStream schemaIn) {
		Statement statement = null;
		boolean result = false;
		try {
			File f = new File(filename);
			if (!f.exists()) {
				f.getParentFile().mkdirs();
				f.createNewFile();
			}
			
			if (checkSchema(filename,alias,schemaIn)) {
				statement = connection.createStatement();
				statement.setQueryTimeout(30);
				statement.execute("ATTACH DATABASE 'file:"+filename+"' AS "+alias);
				result = true;
			} else {
				result = false;
			}
			
		} catch (Exception e) {
			result = false;
		} finally {
			closeStatement(statement);
			setJournalMode(JOURNAL_MODE);
			setSynchronousMode(PRAGMA_SYNCHRONOUS_MODE);
		}
		return result;
	}
	
	private boolean detachDatabase(String alias) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.execute("DETACH DATABASE "+alias);
			result = true;
		} catch (Exception e) {
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}
	
	private boolean setJournalMode(String mode) {
		if (mode != null) {
			Statement statement = null;
			boolean result = false;
			try {
				statement = connection.createStatement();
				statement.setQueryTimeout(30);
				statement.execute("pragma journal_mode = "+mode+";");
				result = true;
			} catch (Exception e) {
				result = false;
			} finally {
				closeStatement(statement);
			}
			
			return result;
		}
		return false;
	}
	
	private boolean setSynchronousMode(String mode) {
		if (mode != null) {
			Statement statement = null;
			boolean result = false;
			try {
				statement = connection.createStatement();
				statement.setQueryTimeout(30);
				statement.execute("pragma synchronous = "+mode+";");
				result = true;
			} catch (Exception e) {
				result = false;
			} finally {
				closeStatement(statement);
			}
			
			return result;
		}
		return false;
	}
	
	private String queryPragma(String pragma) {
		String result = "";
		if (pragma != null) {
			Statement statement = null;
			try {
				statement = connection.createStatement();
				statement.setQueryTimeout(30);
				ResultSet r = statement.executeQuery("pragma "+pragma);
				if (r.next()) {
					result = r.getString(1);
				}
			} catch (Exception e) {
				result = e.getLocalizedMessage();
			} finally {
				closeStatement(statement);
			}
		}
		return result;
	}
	
	private void init() throws ClassNotFoundException {
		
		Class.forName("org.sqlite.JDBC");
		
		try {
			connection = DriverManager.getConnection("jdbc:sqlite::memory:");
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void disconnect() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void setAutoCommitOnCreation(boolean flag) {
		this.autoCommitOnCreation = flag;
	}
	
	@Override
	public boolean isAutoCommitOnCreationEnabled() {
		return this.autoCommitOnCreation;
	}
	
	@Override
	public SLEXMMEvent createEvent(int order, int activity_instance_id, String lifecycle, String resource, long timestamp) {
		SLEXMMEvent ev = new SLEXMMEvent(this);
		ev.setOrder(order);
		ev.setActivityInstanceId(activity_instance_id);
		ev.setLifecycle(lifecycle);
		ev.setResource(resource);
		ev.setTimestamp(timestamp);
		if (isAutoCommitOnCreationEnabled()) {
			ev.commit();
		}
		return ev;
	}
	
//	@Override
//	public SLEXMMClass findClass(String name, int datamodelId) {
//		SLEXMMClass cl = null;
//		Statement statement = null;
//		try {
//			statement = connection.createStatement();
//			statement.setQueryTimeout(30);
//			ResultSet rset = statement.executeQuery("SELECT id,name FROM "+METAMODEL_ALIAS
//					+".class WHERE name = '"+name+"' AND datamodel_id = '"+datamodelId+"'");
//			
//			if (rset.next()) {
//				int id = rset.getInt("id");
//				cl = new SLEXMMClass(this, name, datamodelId);
//				cl.setId(id);
//				cl.setDirty(false);
//				cl.setInserted(false);
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			cl = null;
//		} finally {
//			closeStatement(statement);
//		}
//		
//		return cl;
//	}
	
//	@Override
//	public SLEXMMAttribute findAttribute(int datamodelId, String className, String name) {
//		SLEXMMAttribute at = null;
//		Statement statement = null;
//		String classNameC = className.trim();
//		String nameC = name.trim();
//		try {
//			statement = connection.createStatement();
//			statement.setQueryTimeout(30);
//			ResultSet rset = statement.executeQuery("SELECT AT.id,AT.name,AT.class_id FROM "
//														+METAMODEL_ALIAS+".attribute_name AS AT, "
//														+METAMODEL_ALIAS+".class as CL WHERE "
//															+" AT.name = '"+nameC+"' AND "
//															+" AT.class_id = CL.id AND "
//															+" CL.name = '"+classNameC+"' AND "
//															+" CL.datamodel_id = '"+datamodelId+"'");
//			
//			if (rset.next()) {
//				int classId = rset.getInt("class_id");
//				int id = rset.getInt("id");
//				at = new SLEXMMAttribute(this);
//				at.setId(id);
//				at.setClassId(classId);
//				at.setName(name);
//				at.setDirty(false);
//				at.setInserted(false);
//			}
//			
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			at = null;
//		} finally {
//			closeStatement(statement);
//		}
//		
//		return at;
//	}
	
//	@Override
//	public SLEXMMAttribute findOrCreateAttribute(int datamodelId, String className, String name) {
//		
//		SLEXMMAttribute at = findAttribute(datamodelId, className, name);
//		
//		if (at == null) {
//			at = createAttribute(classId, name);
//		}
//		
//		return at;
//	}
	
	@Override
	public SLEXMMAttributeValue createAttributeValue(int attributeId, int objectVersionId, String value, String type) {
		SLEXMMAttributeValue av = new SLEXMMAttributeValue(this,attributeId,objectVersionId);
		av.setValue(value);
		av.setType(type);
		if (isAutoCommitOnCreationEnabled()) {
			av.commit();
		}
		return av;
	}
	
	@Override
	public SLEXMMDataModel createDataModel(String name) {
		SLEXMMDataModel dm = new SLEXMMDataModel(this);
		dm.setName(name);
		if (isAutoCommitOnCreationEnabled()) {
			dm.commit();
		}
		return dm;
	}
	
	@Override
	public SLEXMMClass createClass(int data_modelId, String name) {
		SLEXMMClass cl = new SLEXMMClass(this, name, data_modelId);
		if (isAutoCommitOnCreationEnabled()) {
			cl.commit();
		}
		return cl;
	}
	
	@Override
	public SLEXMMAttribute createAttribute(int classId, String name) {
		SLEXMMAttribute at = new SLEXMMAttribute(this);
		at.setName(name);
		at.setClassId(classId);
		if (isAutoCommitOnCreationEnabled()) {
			at.commit();
		}
		return at;
	}
	
	@Override
	public SLEXMMRelationship createRelationship(String name, int sourceClassId, int targetClassId) {
		SLEXMMRelationship k = new SLEXMMRelationship(this);
		k.setName(name);
		k.setSourceClassId(sourceClassId);
		k.setTargetClassId(targetClassId);
		if (isAutoCommitOnCreationEnabled()) {
			k.commit();
		}
		return k;
	}
	
	private int getLastInsertedRowId(Statement stmnt) {
		int id = -1;
		ResultSet res = null;
		try {
			res = stmnt.getGeneratedKeys();
			if (res.next()) {
				id = res.getInt("last_insert_rowid()");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResultSet(res);
		}
		return id;
	}
	
	@Override
	public synchronized boolean insert(SLEXMMEvent ev) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.execute("INSERT INTO "+METAMODEL_ALIAS+".event (activity_instance_id,ordering,lifecycle,timestamp,resource) VALUES ('"+
					ev.getActivityInstanceId()+"','"+ev.getOrder()+"','"+ev.getLifecycle()+"','"+ev.getTimestamp()+"','"+ev.getResource()+"')");
			ev.setId(getLastInsertedRowId(statement));
			// TODO Insert all the attributes and attribute-values in the event, if any
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}
	
	@Override
	public boolean update(SLEXMMEvent ev) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.execute("UPDATE "+METAMODEL_ALIAS+".event"+
					" SET activity_instance_id = '"+ev.getActivityInstanceId()+"' "+
					" AND ordering = '"+ev.getOrder()+"'"+
					" AND lifecycle = '"+ev.getLifecycle()+"'"+
					" AND resource = '"+ev.getResource()+"'"+
					" AND timestamp = '"+ev.getTimestamp()+"'"+
					" WHERE id = '"+ev.getId()+"'");
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}
	
	@Override
	public synchronized boolean insert(SLEXMMAttribute at) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.execute("INSERT INTO "+METAMODEL_ALIAS+".attribute_name (name,class_id) VALUES ('"+
					at.getName()+"','"+at.getClassId()+"')");
			at.setId(getLastInsertedRowId(statement));
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}
	
	@Override
	public boolean update(SLEXMMAttribute at) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.execute("UPDATE "+METAMODEL_ALIAS+".attribute_name SET class_id = '"+
					at.getClassId()+"',name = '"+at.getName()+"' WHERE id = '"+at.getId()+"'");
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}
	
	@Override
	public synchronized boolean insert(SLEXMMEventAttribute at) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.execute("INSERT INTO " + METAMODEL_ALIAS
					+ ".event_attribute_name (name) VALUES ('"
					+ at.getName() + "')");
			at.setId(getLastInsertedRowId(statement));
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}
	
	@Override
	public boolean update(SLEXMMEventAttribute at) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.execute("UPDATE "+METAMODEL_ALIAS
					+".event_attribute_name SET name = '"+at.getName()
					+"' WHERE id = '"+at.getId()+"'");
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}
	
	@Override
	public synchronized boolean insert(SLEXMMRelationship k) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.execute("INSERT INTO "+METAMODEL_ALIAS
					+".relationship (name,source,target) VALUES ('"
					+k.getName()+"','"+k.getSourceClassId()+"','"+k.getTargetClassId()+"')");
			k.setId(getLastInsertedRowId(statement));
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}
	
	@Override
	public boolean update(SLEXMMRelationship k) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.execute("UPDATE "+METAMODEL_ALIAS
					+".relationship SET name = '"+k.getName()+"',source = '"+k.getSourceClassId()
					+"',target = '"+k.getTargetClassId()+"' WHERE id = '"+k.getId()+"'");
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}
	
	@Override
	public synchronized boolean insert(SLEXMMAttributeValue av) {
		PreparedStatement statement = null;
		boolean result = false;
		try {
			statement = connection.prepareStatement("INSERT INTO "+METAMODEL_ALIAS
					+".attribute_value (object_version_id,attribute_name_id,value,type) VALUES (?,?,?,?)");
			statement.setQueryTimeout(30);
			statement.setInt(1, av.getObjectVersionId());
			statement.setInt(2, av.getAttributeId());
			statement.setString(3, av.getValue());
			statement.setString(4, av.getType());
			statement.execute();
			av.setId(getLastInsertedRowId(statement));
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}
	
	@Override
	public boolean update(SLEXMMAttributeValue av) {
		PreparedStatement statement = null;
		boolean result = false;
		try {
			statement = connection.prepareStatement("UPDATE "+METAMODEL_ALIAS
					+".attribute_value SET value = ? , type = ? , object_version_id = ? , attribute_name_id = ? WHERE id = ? ");
			statement.setQueryTimeout(30);
			statement.setString(1, av.getValue());
			statement.setString(2, av.getType());
			statement.setInt(3, av.getObjectVersionId());
			statement.setInt(4, av.getAttributeId());
			statement.setInt(5, av.getId());
			statement.execute();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}
	
	@Override
	public synchronized boolean insert(SLEXMMEventAttributeValue av) {
		PreparedStatement statement = null;
		boolean result = false;
		try {
			statement = connection.prepareStatement("INSERT INTO "+METAMODEL_ALIAS
					+".event_attribute_value (event_id,event_attribute_name_id,value,type) VALUES (?,?,?,?)");
			statement.setQueryTimeout(30);
			statement.setInt(1, av.getEventId());
			statement.setInt(2, av.getAttributeId());
			statement.setString(3, av.getValue());
			statement.setString(4, av.getType());
			statement.execute();
			av.setId(getLastInsertedRowId(statement));
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}
	
	@Override
	public boolean update(SLEXMMEventAttributeValue av) {
		PreparedStatement statement = null;
		boolean result = false;
		try {
			statement = connection.prepareStatement("UPDATE "+METAMODEL_ALIAS
					+".event_attribute_value SET value = ? , type = ? , event_id = ? , event_attribute_name_id = ? WHERE id = ? ");
			statement.setQueryTimeout(30);
			statement.setString(1, av.getValue());
			statement.setString(2, av.getType());
			statement.setInt(3, av.getEventId());
			statement.setInt(4, av.getAttributeId());
			statement.setInt(5, av.getId());
			statement.execute();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}
	
	@Override
	public synchronized boolean insert(SLEXMMClass cl) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.execute("INSERT INTO "+METAMODEL_ALIAS+".class (name,datamodel_id) VALUES ('"+cl.getName()+"','"+cl.getDataModelId()+"')");
			cl.setId(getLastInsertedRowId(statement));
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}
	
	@Override
	public boolean update(SLEXMMClass cl) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.execute("UPDATE "+METAMODEL_ALIAS+".class SET name = '"+cl.getName()+"', datamodel_id = '"+cl.getDataModelId()+"' WHERE id = '"+cl.getId()+"'");
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}
	
	@Override
	public synchronized boolean insert(SLEXMMProcess p) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.execute("INSERT INTO "+METAMODEL_ALIAS+".process (name) VALUES ('"+p.getName()+"')");
			p.setId(getLastInsertedRowId(statement));
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}
	
	@Override
	public boolean update(SLEXMMProcess p) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.execute("UPDATE "+METAMODEL_ALIAS+".process SET name = '"+p.getName()+"' WHERE id = '"+p.getId()+"'");
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}
	
	@Override
	public synchronized boolean insert(SLEXMMDataModel dm) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.execute("INSERT INTO "+METAMODEL_ALIAS+".datamodel (name) VALUES ('"+dm.getName()+"')");
			dm.setId(getLastInsertedRowId(statement));
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}
	
	@Override
	public boolean update(SLEXMMDataModel dm) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.execute("UPDATE "+METAMODEL_ALIAS+".datamodel SET name = '"+dm.getName()+"' WHERE id = '"+dm.getId()+"'");
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}
	
	@Override
	public synchronized boolean insert(SLEXMMCase t) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.execute("INSERT INTO "+METAMODEL_ALIAS+".pcase (name) VALUES ('"+t.getName()+"')");
			t.setId(getLastInsertedRowId(statement));
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}
	
	@Override
	public boolean update(SLEXMMCase t) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.execute("UPDATE "+METAMODEL_ALIAS+".pcase SET name = '"+t.getName()+"' WHERE id = '"+t.getId()+"'");
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}
	
	@Override
	public SLEXMMEventResultSet getEventsOfCase(SLEXMMCase c) {
		return getEventsOfCase(c.getId());
	}
	
	@Override
	public SLEXMMEventResultSet getEventsOfCase(int cId) {
		SLEXMMEventResultSet erset = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT EV.* FROM "
					+METAMODEL_ALIAS+".event as EV, "
					+METAMODEL_ALIAS+".activity_instance_to_case as AITC "
					+" WHERE EV.activity_instance_id = AITC.activity_instance_id "
					+" AND AITC.case_id = '"+cId+"'");
			erset = new SLEXMMEventResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset; 
	}
	
	@Override
	public HashMap<SLEXMMEventAttribute, SLEXMMEventAttributeValue> getAttributeValuesForEvent(
			SLEXMMEvent ev) {
		return getAttributeValuesForEvent(ev.getId());
	}
	
	@Override
	public HashMap<SLEXMMEventAttribute, SLEXMMEventAttributeValue> getAttributeValuesForEvent(
			int evId) {
		HashMap<SLEXMMEventAttribute, SLEXMMEventAttributeValue> attributeValues = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			ResultSet rset = statement.executeQuery("SELECT AT.id, AT.name, ATV.value, ATV.type FROM "+
														METAMODEL_ALIAS+".event_attribute_name AS AT, "+
														METAMODEL_ALIAS+".event_attribute_value AS ATV, "+
														METAMODEL_ALIAS+".event AS EV "+
														"WHERE EV.id = "+evId+" AND ATV.event_id = EV.id AND "
																+ " ATV.event_attribute_name_id = AT.id ");
			attributeValues = new HashMap<>();
			
			while (rset.next()) {
				SLEXMMEventAttribute at = new SLEXMMEventAttribute(this);
				at.setId(rset.getInt(1));
				at.setName(rset.getString(2));
				at.setDirty(false);
				at.setInserted(true);
				SLEXMMEventAttributeValue atv = new SLEXMMEventAttributeValue(this, at.getId(), evId);
				atv.setValue(rset.getString(3));
				atv.setType(rset.getString(4));
				atv.setDirty(false);
				atv.setInserted(true);
				attributeValues.put(at, atv);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			attributeValues = null;
		} finally {
			closeStatement(statement);
		}
		return attributeValues;
	}
	
	@Override
	public SLEXMMClassResultSet getClassesForDataModel(SLEXMMDataModel dm) {
		SLEXMMClassResultSet crset = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT * FROM "+METAMODEL_ALIAS+".class WHERE datamodel_id = '"+dm.getId()+"'");
			crset = new SLEXMMClassResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return crset;
	}
	
	@Override
	public SLEXMMDataModelResultSet getDataModels() {
		SLEXMMDataModelResultSet dmrset = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT * FROM "+METAMODEL_ALIAS+".datamodel");
			dmrset = new SLEXMMDataModelResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return dmrset;
	}
	
	@Override
	public List<SLEXMMAttribute> getAttributesForClass(SLEXMMClass cl) {
		return getAttributesForClass(cl.getId());
	}
	

	@Override
	public List<SLEXMMAttribute> getAttributesForClass(int clId) {
		List<SLEXMMAttribute> atList = new Vector<>();
		Statement statement = null;
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT * FROM "+METAMODEL_ALIAS+".attribute_name WHERE class_id = '"+clId+"'");
			while (rset.next()) {
				int id = rset.getInt("id");
				String name = rset.getString("name");
				int classId = rset.getInt("class_id");
				SLEXMMAttribute at = new SLEXMMAttribute(this);
				at.setId(id);
				at.setName(name);
				at.setClassId(classId);
				at.setDirty(false);
				at.setInserted(true);
				atList.add(at);
			}
		} catch (Exception e) {
			e.printStackTrace();
			atList = null;
		} finally {
			closeStatement(statement);
		}
		return atList;
	}
	
	@Override
	public List<SLEXMMRelationship> getRelationshipsForClass(SLEXMMClass cl) {
		List<SLEXMMRelationship> kList = new Vector<>();
		Statement statement = null;
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT * FROM "+METAMODEL_ALIAS+".relationship WHERE source = '"+cl.getId()+"'");
			while (rset.next()) {
				int id = rset.getInt("id");
				String name = rset.getString("name");
				int sourceId = rset.getInt("source");
				int targetId = rset.getInt("target");
				SLEXMMRelationship k = new SLEXMMRelationship(this);
				k.setId(id);
				k.setName(name);
				k.setSourceClassId(sourceId);
				k.setTargetClassId(targetId);
				k.setDirty(false);
				k.setInserted(true);
				kList.add(k);
			}
		} catch (Exception e) {
			e.printStackTrace();
			kList = null;
		} finally {
			closeStatement(statement);
		}
		return kList;
	}

	public int getNumberEventsOfCase(SLEXMMCase t) {
		// TEST
		int events = 0;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT count(TE.event_id) FROM "+METAMODEL_ALIAS+".event_to_case AS TE WHERE TE.case_id='"+t.getId()+"'");
			if (rset.next()) {
				events = rset.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return events;
	}
	
	@Override
	public SLEXMMCase createCase(String name) {
		SLEXMMCase t = new SLEXMMCase(this);
		t.setName(name);
		if (isAutoCommitOnCreationEnabled()) {
			t.commit();
		}
		return t;
	}

	@Override
	public SLEXMMCase cloneCase(SLEXMMCase t) {
		SLEXMMCase ct = this.createCase(t.getName());
		ct.commit();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			//statement.setQueryTimeout(30);
			statement.execute("INSERT INTO "+METAMODEL_ALIAS+".activity_instance_to_case (case_id,activity_instance_id) "+
							" SELECT '"+ct.getId()+"', activity_instance_id FROM "+METAMODEL_ALIAS+".activity_instance_to_case "+
							" WHERE case_id = '"+t.getId()+"' ");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeStatement(statement);
		}
		return ct;
	}

	@Override
	public boolean addActivityInstanceToCase(int traceId, int activityInstanceId) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.execute("INSERT INTO "+METAMODEL_ALIAS+".activity_instance_to_case (case_id,activity_instance_id) VALUES ('"+traceId+"','"+activityInstanceId+"')");
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		return result;
	}
	
	@Override
	public boolean addActivityInstanceToCase(SLEXMMCase t, SLEXMMActivityInstance e) {
		return addActivityInstanceToCase(t.getId(),e.getId());
	}

	@Override
	public int getMaxCaseId() {
		int maxId = 0;
		
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT IFNULL(MAX(id),0) + 1 FROM "+METAMODEL_ALIAS+".pcase");
			rset.next();
			maxId = rset.getInt(1);
			rset.close();
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
				
		return maxId;
	}

	@Override
	public SLEXMMEventAttribute createEventAttribute(String name) {
		SLEXMMEventAttribute at = new SLEXMMEventAttribute(this);
		at.setName(name);
		if (isAutoCommitOnCreationEnabled()) {
			at.commit();
		}
		return at;
	}

	@Override
	public SLEXMMEventAttributeValue createEventAttributeValue(int attributeId,
			int eventId, String value, String type) {
		SLEXMMEventAttributeValue av = new SLEXMMEventAttributeValue(this,attributeId,eventId);
		av.setValue(value);
		av.setType(type);
		if (isAutoCommitOnCreationEnabled()) {
			av.commit();
		}
		return av;
	}

	@Override
	public SLEXMMRelation createRelation(int sourceObjectVersionId,
			int targetObjectVersionId, int relationshipId,
			long startTimestamp, long endTimestamp) {
		SLEXMMRelation rt = new SLEXMMRelation(this);
		rt.setSourceObjectVersionId(sourceObjectVersionId);
		rt.setTargetObjectVersionId(targetObjectVersionId);
		rt.setStartTimestamp(startTimestamp);
		rt.setEndTimestamp(endTimestamp);
		rt.setRelationshipId(relationshipId);
		if (isAutoCommitOnCreationEnabled()) {
			rt.commit();
		}
		return rt;
	}
	
	@Override
	public synchronized boolean insert(SLEXMMRelation rt) {
		PreparedStatement statement = null;
		boolean result = false;
		try {
			statement = connection.prepareStatement("INSERT INTO "+METAMODEL_ALIAS
					+".relation (source_object_version_id,"
					+"target_object_version_id,relationship_id,"
					+"start_timestamp,end_timestamp)"
					+" VALUES (?,?,?,?,?)");
			statement.setQueryTimeout(30);
			statement.setInt(1, rt.getSourceObjectVersionId());
			statement.setInt(2, rt.getTargetObjectVersionId());
			statement.setInt(3, rt.getRelationshipId());
			statement.setLong(4, rt.getStartTimestamp());
			statement.setLong(5, rt.getEndTimestamp());
			rt.setId(getLastInsertedRowId(statement));
			statement.execute();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}

	@Override
	public boolean update(SLEXMMRelation rt) {
		PreparedStatement statement = null;
		boolean result = false;
		try {
			statement = connection.prepareStatement("UPDATE "+METAMODEL_ALIAS
					+".relation SET source_object_version_id = ? , "
					+"target_object_version_id = ? , "
					+"relationship_id = ? , "
					+"start_timestamp = ? , "
					+"end_timestamp = ? "
					+" WHERE id = ? ");
			statement.setQueryTimeout(30);
			statement.setInt(1, rt.getSourceObjectVersionId());
			statement.setInt(2, rt.getTargetObjectVersionId());
			statement.setInt(3, rt.getRelationshipId());
			statement.setLong(4, rt.getStartTimestamp());
			statement.setLong(5, rt.getEndTimestamp());
			statement.setInt(6, rt.getId());
			statement.execute();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}

	@Override
	public SLEXMMObject createObject(int classId) {
		SLEXMMObject obj = new SLEXMMObject(this);
		obj.setClassId(classId);
		if (isAutoCommitOnCreationEnabled()) {
			obj.commit();
		}
		return obj;
	}
	
	@Override
	public synchronized boolean insert(SLEXMMObject obj) {
		PreparedStatement statement = null;
		boolean result = false;
		try {
			statement = connection.prepareStatement("INSERT INTO "+METAMODEL_ALIAS
					+".object (class_id) VALUES (?)");
			statement.setQueryTimeout(30);
			statement.setInt(1, obj.getClassId());
			statement.execute();
			obj.setId(getLastInsertedRowId(statement));
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}

	@Override
	public boolean update(SLEXMMObject obj) {
		PreparedStatement statement = null;
		boolean result = false;
		try {
			statement = connection.prepareStatement("UPDATE "+METAMODEL_ALIAS
					+".object SET class_id = ? WHERE id = ? ");
			statement.setQueryTimeout(30);
			statement.setInt(1, obj.getClassId());
			statement.setInt(2, obj.getId());
			statement.execute();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}

	@Override
	public SLEXMMObjectVersion createObjectVersion(int objectId, long startTimestamp, long endTimestamp) {
		SLEXMMObjectVersion objv = new SLEXMMObjectVersion(this);
		objv.setObjectId(objectId);
		objv.setStartTimestamp(startTimestamp);
		objv.setEndTimestamp(endTimestamp);
		if (isAutoCommitOnCreationEnabled()) {
			objv.commit();
		}
		return objv;
	}
	
	@Override
	public synchronized boolean insert(SLEXMMObjectVersion objv) {
		PreparedStatement statement = null;
		boolean result = false;
		try {
			statement = connection.prepareStatement("INSERT INTO "+METAMODEL_ALIAS
					+".object_version (object_id,start_timestamp,end_timestamp) VALUES (?,?,?)");
			statement.setQueryTimeout(30);
			statement.setInt(1, objv.getObjectId());
			statement.setLong(2, objv.getStartTimestamp());
			statement.setLong(3, objv.getEndTimestamp());
			statement.execute();
			objv.setId(getLastInsertedRowId(statement));
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}

	@Override
	public boolean update(SLEXMMObjectVersion objv) {
		PreparedStatement statement = null;
		boolean result = false;
		try {
			statement = connection.prepareStatement("UPDATE "+METAMODEL_ALIAS
					+".object_version SET object_id = ?, "
					+" start_timestamp = ?, end_timestamp = ? WHERE id = ? ");
			statement.setQueryTimeout(30);
			statement.setInt(1, objv.getObjectId());
			statement.setLong(2, objv.getStartTimestamp());
			statement.setLong(3, objv.getEndTimestamp());
			statement.setInt(4, objv.getId());
			statement.execute();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}

	@Override
	public HashMap<SLEXMMAttribute, SLEXMMAttributeValue> getAttributeValuesForObjectVersion(
			SLEXMMObjectVersion objv) {
		return getAttributeValuesForObjectVersion(objv.getId());
	}
	
	@Override
	public HashMap<SLEXMMAttribute, SLEXMMAttributeValue> getAttributeValuesForObjectVersion(
			int objvId) {
		HashMap<SLEXMMAttribute, SLEXMMAttributeValue> attributeValues = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			ResultSet rset = statement.executeQuery("SELECT AT.id, AT.name, AT.class_id, ATV.id, ATV.value, ATV.type FROM "+
														METAMODEL_ALIAS+".attribute_name AS AT, "+
														METAMODEL_ALIAS+".attribute_value AS ATV, "+
														METAMODEL_ALIAS+".object_version AS OBJV "+
														"WHERE OBJV.id = "+objvId+" AND "+
														"ATV.object_version_id = OBJV.id AND "+
														"ATV.attribute_name_id = AT.id ");
			attributeValues = new HashMap<>();
			
			while (rset.next()) {
				SLEXMMAttribute at = new SLEXMMAttribute(this);
				at.setId(rset.getInt(1));
				at.setName(rset.getString(2));
				at.setClassId(rset.getInt(3));
				at.setDirty(false);
				at.setInserted(true);
				SLEXMMAttributeValue atv = new SLEXMMAttributeValue(this, at.getId(), objvId);
				atv.setId(rset.getInt(4));
				atv.setValue(rset.getString(5));
				atv.setType(rset.getString(6));
				atv.setDirty(false);
				atv.setInserted(true);
				attributeValues.put(at, atv);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			attributeValues = null;
		} finally {
			closeStatement(statement);
		}
		return attributeValues;
	}

	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForObjectOrdered(SLEXMMObject obj) {
		return getObjectVersionsForObjectOrdered(obj.getId()); 
	}
	
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForObjectOrdered(int objId) {
		SLEXMMObjectVersionResultSet erset = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT OBJV.* FROM "
					+METAMODEL_ALIAS+".object_version AS OBJV "
					+" WHERE OBJV.object_id = "+objId+" "
					+" ORDER BY OBJV.start_timestamp ");
			erset = new SLEXMMObjectVersionResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset; 
	}
	
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersions() {
		SLEXMMObjectVersionResultSet erset = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT OBJV.* FROM "
					+METAMODEL_ALIAS+".object_version AS OBJV "
					+" ORDER BY OBJV.start_timestamp ");
			erset = new SLEXMMObjectVersionResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset; 
	}
	
	@Override
	public SLEXMMRelationResultSet getRelationsForSourceObject(SLEXMMObject obj) {
		return getRelationsForSourceObject(obj.getId());
	}
	
	@Override
	public SLEXMMRelationResultSet getRelationsForSourceObject(int objId) {
		SLEXMMRelationResultSet erset = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT RL.* FROM "
					+METAMODEL_ALIAS+".relation RL, "
					+METAMODEL_ALIAS+".object_version OBJV "
					+" WHERE RL.source_object_version_id = OBJV.id "
					+" AND OBJV.object_id = "+objId+" ");
			erset = new SLEXMMRelationResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset; 
	}
	
	@Override
	public SLEXMMRelationResultSet getRelationsForTargetObject(SLEXMMObject obj) {
		return getRelationsForTargetObject(obj.getId());
	}
	
	@Override
	public SLEXMMRelationResultSet getRelationsForTargetObject(int objId) {
		SLEXMMRelationResultSet erset = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT RL.* FROM "
					+METAMODEL_ALIAS+".relation RL, "
					+METAMODEL_ALIAS+".object_version OBJV "
					+" WHERE RL.target_object_version_id = OBJV.id "
					+" AND OBJV.object_id = "+objId+" ");
			erset = new SLEXMMRelationResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset; 
	}
	
	@Override
	public SLEXMMRelationResultSet getRelationsForSourceObjectOrdered(SLEXMMObject obj) {
		return getRelationsForSourceObjectOrdered(obj.getId());
	}
	
	@Override
	public SLEXMMRelationResultSet getRelationsForSourceObjectOrdered(int objId) { // TODO Check
		SLEXMMRelationResultSet erset = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT REL.* FROM "
					+METAMODEL_ALIAS+".relation as REL, "
					+METAMODEL_ALIAS+".object_version OBJV "
					+" WHERE REL.source_object_version_id = OBJV.id "
					+" AND OBJV.object_id = "+objId+" "
					+" ORDER BY OBJV.start_timestamp ");
			erset = new SLEXMMRelationResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset; 
	}
	
	@Override
	public SLEXMMRelationResultSet getRelationsForTargetObjectOrdered(SLEXMMObject obj) {
		return getRelationsForTargetObject(obj.getId());
	}
	
	@Override
	public SLEXMMRelationResultSet getRelationsForTargetObjectOrdered(int objId) { // TODO Check
		SLEXMMRelationResultSet erset = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT REL.* FROM "
					+METAMODEL_ALIAS+".relation as REL, "
					+METAMODEL_ALIAS+".object_version OBJV "
					+" WHERE REL.target_object_version_id = OBJV.id "
					+" AND OBJV.object_id = "+objId+" "
					+" ORDER BY OBJV.start_timestamp ASC ");
			erset = new SLEXMMRelationResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset; 
	}

	@Override
	public SLEXMMObjectResultSet getObjects() {
		SLEXMMObjectResultSet erset = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT OBJ.id, OBJ.class_id FROM "
					+METAMODEL_ALIAS+".object AS OBJ "
					+" ORDER BY OBJ.id");
			erset = new SLEXMMObjectResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset; 
	}
	
	@Override
	public SLEXMMObjectResultSet getObjectsPerClass(int classId) {
		SLEXMMObjectResultSet erset = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT OBJ.id, OBJ.class_id FROM "
					+METAMODEL_ALIAS+".object AS OBJ "
					+" WHERE OBJ.class_id = "+classId
					+" ORDER BY OBJ.id");
			erset = new SLEXMMObjectResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset; 
	}

	@Override
	public SLEXMMEventResultSet getEvents() {
		SLEXMMEventResultSet erset = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT * FROM "
					+METAMODEL_ALIAS+".event ");
			erset = new SLEXMMEventResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset; 
	}

	@Override
	public SLEXMMEventResultSet getEventsOrdered() {
		SLEXMMEventResultSet erset = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT * FROM "
					+METAMODEL_ALIAS+".event ORDER BY ordering ASC ");
			erset = new SLEXMMEventResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset; 
	}

	@Override
	public SLEXMMCaseResultSet getCases() {
		SLEXMMCaseResultSet erset = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT * FROM "
					+METAMODEL_ALIAS+".pcase ");
			erset = new SLEXMMCaseResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset;
	}

	@Override
	public boolean insert(SLEXMMActivity cl) {
		PreparedStatement statement = null;
		boolean result = false;
		try {
			statement = connection.prepareStatement("INSERT INTO "+METAMODEL_ALIAS
					+".activity (name,process_id) VALUES (?,?)");
			statement.setQueryTimeout(30);
			statement.setString(1, cl.getName());
			statement.setInt(2, cl.getProcessId());
			statement.execute();
			cl.setId(getLastInsertedRowId(statement));
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}

	@Override
	public boolean update(SLEXMMActivity cl) {
		PreparedStatement statement = null;
		boolean result = false;
		try {
			statement = connection.prepareStatement("UPDATE "+METAMODEL_ALIAS
					+".activity SET name = ?, process_id = ? WHERE id = ? ");
			statement.setQueryTimeout(30);
			statement.setString(1, cl.getName());
			statement.setInt(2, cl.getProcessId());
			statement.setInt(3, cl.getId());
			statement.execute();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}	
	
	@Override
	public boolean insert(SLEXMMActivityInstance ai) {
		PreparedStatement statement = null;
		boolean result = false;
		try {
			statement = connection.prepareStatement("INSERT INTO "+METAMODEL_ALIAS
					+".activity_instance (activity_id) VALUES (?)");
			statement.setQueryTimeout(30);
			statement.setInt(1, ai.getActivityId());
			statement.execute();
			ai.setId(getLastInsertedRowId(statement));
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}

	@Override
	public boolean update(SLEXMMActivityInstance ai) {
		PreparedStatement statement = null;
		boolean result = false;
		try {
			statement = connection.prepareStatement("UPDATE "+METAMODEL_ALIAS
					+".activity_instance SET activity_id = ? WHERE id = ? ");
			statement.setQueryTimeout(30);
			statement.setInt(1, ai.getActivityId());
			statement.setInt(2, ai.getId());
			statement.execute();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}

	@Override
	public SLEXMMEventResultSet getEventsForActivityInstanceOrdered(
			SLEXMMActivityInstance slexmmActivityInstance) {
		return getEventsForActivityInstanceOrdered(slexmmActivityInstance.getId());
	}
	
	@Override
	public SLEXMMEventResultSet getEventsForActivityInstanceOrdered(int aiId) {
		SLEXMMEventResultSet erset = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT EV.* FROM "
					+METAMODEL_ALIAS+".event as EV, "
					+METAMODEL_ALIAS+".activity_instance_to_case as AITC, "
					+" WHERE EV.activity_instance_id = '"+aiId+"' "
					+" ORDER BY EV.ordering ASC ");
			erset = new SLEXMMEventResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset;
	}

	@Override
	public SLEXMMEventResultSet getEventsForCaseOrdered(SLEXMMCase c) {
		return getEventsForCaseOrdered(c.getId());
	}
	
	@Override
	public SLEXMMEventResultSet getEventsForCaseOrdered(int caseId) {
		SLEXMMEventResultSet erset = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String query = "SELECT EV.* FROM "
					+METAMODEL_ALIAS+".event as EV, "
					+METAMODEL_ALIAS+".activity_instance_to_case as AITC "
					+" WHERE EV.activity_instance_id = AITC.activity_instance_id "
					+" AND AITC.case_id = '"+caseId+"' "
					+" ORDER BY EV.ordering ASC ";
			ResultSet rset = statement.executeQuery(query);
			erset = new SLEXMMEventResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset; 
	}

	@Override
	public SLEXMMProcess createProcess(String name) {
		SLEXMMProcess p = new SLEXMMProcess(this);
		p.setName(name);
		if (isAutoCommitOnCreationEnabled()) {
			p.commit();
		}
		return p;
	}

	@Override
	public SLEXMMActivity createActivity(int processId, String name) {
		SLEXMMActivity act = new SLEXMMActivity(this,name,processId);
		if (isAutoCommitOnCreationEnabled()) {
			act.commit();
		}
		return act;
	}

	@Override
	public SLEXMMActivityInstance createActivityInstance(SLEXMMActivity act) {
		SLEXMMActivityInstance actIn = new SLEXMMActivityInstance(this);
		actIn.setActivityId(act.getId());
		if (isAutoCommitOnCreationEnabled()) {
			actIn.commit();
		}
		return actIn;
	}
	
	@Override
	public SLEXMMEvent getEventForId(int evId) {
		SLEXMMEvent e = null;
		
		SLEXMMEventResultSet erset = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String query = "SELECT EV.* FROM "
					+METAMODEL_ALIAS+".event as EV "
					+" WHERE EV.id = '"+evId+"' ";
			ResultSet rset = statement.executeQuery(query);
			erset = new SLEXMMEventResultSet(this, rset);
			e = erset.getNext();
			closeStatement(statement);
		} catch (Exception ex) {
			ex.printStackTrace();
			closeStatement(statement);
		}
		
		return e;
	}

	@Override
	public List<SLEXMMActivity> getActivities() {
		List<SLEXMMActivity> actList = new Vector<>();
		Statement statement = null;
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT * FROM "+METAMODEL_ALIAS+".activity");
			while (rset.next()) {
				int id = rset.getInt("id");
				String name = rset.getString("name");
				int processId = rset.getInt("process_id");
				SLEXMMActivity act = new SLEXMMActivity(this,name,processId);
				act.setId(id);
				act.setDirty(false);
				act.setInserted(true);
				actList.add(act);
			}
		} catch (Exception e) {
			e.printStackTrace();
			actList = null;
		} finally {
			closeStatement(statement);
		}
		return actList;
	}

	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForActivity( // TODO CHECK
			SLEXMMActivity act) {
		SLEXMMObjectVersionResultSet erset = null;
		Statement statement = null;
		String query = "";
		try {
			statement = connection.createStatement();
			query = "SELECT DISTINCT OBJV.* FROM "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".activity_instance AS AI "
					+" WHERE OBJV.id = ETOV.object_version_id "
					+" AND ETOV.event_id = EV.id "
					+" AND EV.activity_instance_id = AI.id "
					+" AND AI.activity_id = "+ act.getId()+" ";
			ResultSet rset = statement.executeQuery(query);
			erset = new SLEXMMObjectVersionResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(query);
			closeStatement(statement);
		}
		
		return erset;
	}

	@Override
	public SLEXMMObjectVersionResultSet getVersionsRelatedToObjectVersion(
			SLEXMMObjectVersion ob) {
		
		SLEXMMObjectVersionResultSet erset = null;
		Statement statement = null;
		String query = "";
		try {
			statement = connection.createStatement();
			query = "SELECT DISTINCT OBJV.* FROM "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".relation AS RL "
					+" WHERE ( RL.source_object_version_id = "+ob.getId()+" AND OBJV.id = RL.target_object_version_id ) "
					+" OR ( RL.target_object_version_id = "+ob.getId()+" AND OBJV.id = RL.source_object_version_id ) ";
			ResultSet rset = statement.executeQuery(query);
			erset = new SLEXMMObjectVersionResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(query);
			closeStatement(statement);
		}
		
		return erset; 
		 
	}

	@Override
	public SLEXMMEventResultSet getEventsForActivity(SLEXMMActivity act) {
		SLEXMMEventResultSet erset = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT EV.* FROM "
					+METAMODEL_ALIAS+".event as EV, "
					+METAMODEL_ALIAS+".activity_instance as AI "
					+" WHERE EV.activity_instance_id = AI.id "
					+" AND AI.activity_id = "+act.getId()+" "
					+" ORDER BY EV.ordering ASC ");
			erset = new SLEXMMEventResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset;
	}

	@Override
	public SLEXMMObjectResultSet getObjectsForEvent(SLEXMMEvent ev) { // TODO Check
		SLEXMMObjectResultSet erset = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT OBJ.id, OBJ.class_id FROM "
					+METAMODEL_ALIAS+".object AS OBJ, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".object_version AS OBJV "
					+" WHERE OBJ.id = OBJV.object_id "
					+" AND OBJV.id = ETOV.object_version_id "
					+" AND ETOV.event_id = '"+ev.getId()+"' "
					+" ORDER BY OBJ.id");
			erset = new SLEXMMObjectResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset; 
	}

	@Override
	public SLEXMMObject getObjectPerId(int objectId) {
		SLEXMMObject ob = null;
			
		SLEXMMObjectResultSet orset = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String query = "SELECT OBJ.* FROM "
					+METAMODEL_ALIAS+".object as OBJ "
					+" WHERE OBJ.id = '"+objectId+"' ";
			ResultSet rset = statement.executeQuery(query);
			orset = new SLEXMMObjectResultSet(this, rset);
			ob = orset.getNext();
			closeStatement(statement);
		} catch (Exception ex) {
			ex.printStackTrace();
			closeStatement(statement);
		}
		
		return ob;
	}

	@Override
	public SLEXMMEventResultSet getEventsForObjectVersion(int objvId) { // TODO Check
		SLEXMMEventResultSet erset = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT EV.* FROM "
					+METAMODEL_ALIAS+".event as EV, "
					+METAMODEL_ALIAS+".event_to_object_version as ETOV "
					+" WHERE EV.id = ETOV.event_id "
					+" AND ETOV.object_version_id = '"+objvId+"' ");
			erset = new SLEXMMEventResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset; 
	}

	@Override
	public boolean addEventToObjectVersion(SLEXMMObjectVersion ov, // TODO Check
			SLEXMMEvent ev, String label) {
		return addEventToObjectVersion(ov.getId(),ev.getId(),label);
	}

	@Override
	public boolean addEventToObjectVersion(int ovId, int evId, String label) { // TODO Check
		Statement statement = null;
		boolean result = false;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.execute("INSERT INTO "+METAMODEL_ALIAS+".event_to_object_version "
					+" (event_id,object_version_id,label) VALUES ('"
					+evId+"','"
					+ovId+"','"
					+label+"')");
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		return result;
	}

	@Override
	public SLEXMMRelationResultSet getRelations() {
		SLEXMMRelationResultSet erset = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT RL.* FROM "
					+METAMODEL_ALIAS+".relation RL ");
			erset = new SLEXMMRelationResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset; 
	}

	@Override
	public Collection<? extends Object> getRelationships() {
		List<SLEXMMRelationship> kList = new Vector<>();
		Statement statement = null;
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT * FROM "+METAMODEL_ALIAS+".relationship ");
			while (rset.next()) {
				int id = rset.getInt("id");
				String name = rset.getString("name");
				int sourceId = rset.getInt("source");
				int targetId = rset.getInt("target");
				SLEXMMRelationship k = new SLEXMMRelationship(this);
				k.setId(id);
				k.setName(name);
				k.setSourceClassId(sourceId);
				k.setTargetClassId(targetId);
				k.setDirty(false);
				k.setInserted(true);
				kList.add(k);
			}
		} catch (Exception e) {
			e.printStackTrace();
			kList = null;
		} finally {
			closeStatement(statement);
		}
		return kList;
	}

	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstances() {
		SLEXMMActivityInstanceResultSet airset = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT AI.* FROM "
					+METAMODEL_ALIAS+".activity_instance AI ");
			airset = new SLEXMMActivityInstanceResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return airset;
	}

	@Override
	public SLEXMMSQLResultSet executeSQL(String query) throws Exception {
		SLEXMMSQLResultSet sqlrset = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery(query);
			sqlrset = new SLEXMMSQLResultSet(this, rset);
		} catch (Exception e) {
			closeStatement(statement);
			throw e;
		}
		
		return sqlrset;
	}	
	
}
