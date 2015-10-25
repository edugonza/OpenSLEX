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
	public List<SLEXMMAttribute> getListAttributesForClass(SLEXMMClass cl) {
		return getListAttributesForClass(cl.getId());
	}
	

	@Override
	public List<SLEXMMAttribute> getListAttributesForClass(int clId) {
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
	public SLEXMMEventResultSet getEventsForCase(int caseId) {
		return getEventsForCases(new int[] {caseId});
	}
	
	@Override
	public SLEXMMEventResultSet getEventsForCases(int[] caseIds) {
		SLEXMMEventResultSet erset = null;
		Statement statement = null;
		
		String caseList = buildStringFromArray(caseIds, true);
		
		try {
			statement = connection.createStatement();
			String query = "SELECT EV.* FROM "
					+METAMODEL_ALIAS+".event as EV, "
					+METAMODEL_ALIAS+".activity_instance_to_case as AITC "
					+" WHERE EV.activity_instance_id = AITC.activity_instance_id "
					+" AND AITC.case_id IN ("+caseList+") "
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
	public SLEXMMEventResultSet getEventsForActivity(int activityId) {
		return getEventsForActivities(new int[] {activityId});
	}
	
	@Override
	public SLEXMMEventResultSet getEventsForActivities(int[] activityIds) {
		SLEXMMEventResultSet erset = null;
		Statement statement = null;
		
		String activityList = buildStringFromArray(activityIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT EV.* FROM "
					+METAMODEL_ALIAS+".event as EV, "
					+METAMODEL_ALIAS+".activity_instance as AI "
					+" WHERE EV.activity_instance_id = AI.id "
					+" AND AI.activity_id IN ("+activityList+") "
					+" ORDER BY EV.ordering ASC ");
			erset = new SLEXMMEventResultSet(this, rset);
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
		return getEventsForObjectVersions(new int[] {objvId});
	}
	
	@Override
	public SLEXMMEventResultSet getEventsForObjectVersions(int[] objvIds) { // TODO Check
		SLEXMMEventResultSet erset = null;
		Statement statement = null;
		
		String objectVersionList = buildStringFromArray(objvIds, true);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT EV.* FROM "
					+METAMODEL_ALIAS+".event as EV, "
					+METAMODEL_ALIAS+".event_to_object_version as ETOV "
					+" WHERE EV.id = ETOV.event_id "
					+" AND ETOV.object_version_id IN ("+objectVersionList+") ");
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
	public SLEXMMAttributeResultSet getAttributes() {
		SLEXMMAttributeResultSet arset = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT A.* FROM "
					+METAMODEL_ALIAS+".attribute_name A ");
			arset = new SLEXMMAttributeResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return arset;
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

	@Override
	public SLEXMMEventAttributeResultSet getEventAttributes() {
		SLEXMMEventAttributeResultSet arset = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT EA.* FROM "
					+METAMODEL_ALIAS+".event_attribute_name EA ");
			arset = new SLEXMMEventAttributeResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return arset;
	}

	@Override
	public SLEXMMObjectResultSet getObjectsForCase(int caseId) {
		return getObjectsForCases(new int[] {caseId});
	}
	
	@Override
	public SLEXMMObjectResultSet getObjectsForCases(int[] caseIds) {
		SLEXMMObjectResultSet erset = null;
		Statement statement = null;
		
		String caseList = buildStringFromArray(caseIds, true);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT OBJ.* FROM "
					+METAMODEL_ALIAS+".object AS OBJ, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".activity_instance_to_case AS AITC "
					+" WHERE OBJ.id = OBJV.object_id "
					+" AND OBJV.id = ETOV.object_version_id "
					+" AND ETOV.event_id = EV.id "
					+" AND EV.activity_instance_id = AITC.activity_instance_id "
					+" AND AITC.case_id IN ("+caseList+") "
					+" ORDER BY OBJ.id");
			erset = new SLEXMMObjectResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset; 
	}

	@Override
	public SLEXMMObjectResultSet getObjectsForObjectVersion(int objectVersionId) {
		return getObjectsForObjectVersions(new int[] {objectVersionId});
	}

	@Override
	public SLEXMMObjectResultSet getObjectsForObjectVersions(
			int[] objectVersionIds) { // TODO Check
		SLEXMMObjectResultSet erset = null;
		Statement statement = null;
		
		String objectVersionList = buildStringFromArray(objectVersionIds, false);
				
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT OBJ.* FROM "
					+METAMODEL_ALIAS+".object AS OBJ, "
					+METAMODEL_ALIAS+".object_version AS OBJV "
					+" WHERE OBJ.id = OBJV.object_id "
					+" AND OBJV.id IN ("+objectVersionList+") "
					+" ORDER BY OBJ.id");
			erset = new SLEXMMObjectResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset;
	}
	
	@Override
	public SLEXMMObjectResultSet getObjectsForEvent(int eventId) { // TODO Check
		return getObjectsForEvents(new int[] {eventId});
	}
	
	@Override
	public SLEXMMObjectResultSet getObjectsForEvents(int[] eventIds) { // TODO Check
		SLEXMMObjectResultSet erset = null;
		Statement statement = null;
		
		String eventList = buildStringFromArray(eventIds, true);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT OBJ.id, OBJ.class_id FROM "
					+METAMODEL_ALIAS+".object AS OBJ, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".object_version AS OBJV "
					+" WHERE OBJ.id = OBJV.object_id "
					+" AND OBJV.id = ETOV.object_version_id "
					+" AND ETOV.event_id IN ("+eventList+") "
					+" ORDER BY OBJ.id");
			erset = new SLEXMMObjectResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset; 
	}
	
	@Override
	public SLEXMMObjectResultSet getObjectsForClass(int classId) {
		return getObjectsForClasses(new int[] {classId});
	}
	
	@Override
	public SLEXMMObjectResultSet getObjectsForClasses(int[] classIds) {
		SLEXMMObjectResultSet erset = null;
		Statement statement = null;
		
		String classList = buildStringFromArray(classIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT OBJ.id, OBJ.class_id FROM "
					+METAMODEL_ALIAS+".object AS OBJ "
					+" WHERE OBJ.class_id IN ("+classList+") "
					+" ORDER BY OBJ.id");
			erset = new SLEXMMObjectResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset; 
	}
	
	@Override
	public SLEXMMObjectResultSet getObjectsForActivity(int activityId) {
		return getObjectsForActivities(new int[] {activityId});
	}
	
	@Override
	public SLEXMMObjectResultSet getObjectsForActivities(int[] activityIds) {
		SLEXMMObjectResultSet erset = null;
		Statement statement = null;
		
		String activityList = buildStringFromArray(activityIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT OBJ.* FROM "
					+METAMODEL_ALIAS+".object AS OBJ, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".activity_instance AS AI "
					+" WHERE OBJ.id = OBJV.object_id "
					+" AND OBJV.id = ETOV.object_version_id "
					+" AND ETOV.event_id = EV.id "
					+" AND EV.activity_instance_id = AI.id "
					+" AND AI.activity_id IN ("+activityList+") "
					+" ORDER BY OBJ.id");
			erset = new SLEXMMObjectResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset; 
	}

	@Override
	public SLEXMMObjectResultSet getObjectsForActivityInstance(
			int activityInstanceId) {
		return getObjectsForActivityInstances(new int[] {activityInstanceId});
	}
	
	@Override
	public SLEXMMObjectResultSet getObjectsForActivityInstances(
			int[] activityInstanceIds) {
		SLEXMMObjectResultSet erset = null;
		Statement statement = null;
		
		String activityInstanceList = buildStringFromArray(activityInstanceIds, true);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT OBJ.* FROM "
					+METAMODEL_ALIAS+".object AS OBJ, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".object_version AS OBJV "
					+" WHERE OBJ.id = OBJV.object_id "
					+" AND OBJV.id = ETOV.object_version_id "
					+" AND ETOV.event_id = EV.id "
					+" AND EV.activity_instance_id IN ("+activityInstanceList+") "
					+" ORDER BY OBJ.id");
			erset = new SLEXMMObjectResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset; 
	}

	@Override
	public SLEXMMObjectResultSet getObjectsForRelation(int relationId) {
		return getObjectsForRelations(new int[] {relationId});
	}
	
	@Override
	public SLEXMMObjectResultSet getObjectsForRelations(int[] relationIds) {
		SLEXMMObjectResultSet erset = null;
		Statement statement = null;
		
		String relationList = buildStringFromArray(relationIds, true);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT OBJ.* FROM "
					+METAMODEL_ALIAS+".object AS OBJ, "
					+METAMODEL_ALIAS+".relation AS REL, "
					+METAMODEL_ALIAS+".object_version AS OBJV "
					+" WHERE OBJ.id = OBJV.object_id "
					+" AND (OBJV.id = REL.source_object_version_id OR OBJV.id = REL.target_object_version_id) "
					+" AND REL.id IN ("+relationList+") "
					+" ORDER BY OBJ.id");
			erset = new SLEXMMObjectResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset; 
	}

	@Override
	public SLEXMMObjectResultSet getObjectsForRelationship(int relationshipId) {
		return getObjectsForRelationships(new int[] {relationshipId});
	}
	
	@Override
	public SLEXMMObjectResultSet getObjectsForRelationships(int[] relationshipIds) {
		SLEXMMObjectResultSet erset = null;
		Statement statement = null;
		
		String relationshipList = buildStringFromArray(relationshipIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT OBJ.* FROM "
					+METAMODEL_ALIAS+".object AS OBJ, "
					+METAMODEL_ALIAS+".relation AS REL, "
					+METAMODEL_ALIAS+".object_version AS OBJV "
					+" WHERE OBJ.id = OBJV.object_id "
					+" AND (OBJV.id = REL.source_object_version_id OR OBJV.id = REL.target_object_version_id) "
					+" AND REL.relationship_id IN ("+relationshipList+") "
					+" ORDER BY OBJ.id");
			erset = new SLEXMMObjectResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset;
	}
	
	@Override
	public SLEXMMObjectResultSet getObjectsForAttribute(int attributeId) {
		return getObjectsForAttributes(new int[] {attributeId});
	}
	
	@Override
	public SLEXMMObjectResultSet getObjectsForAttributes(int[] attributeIds) {
		SLEXMMObjectResultSet erset = null;
		Statement statement = null;
		
		String attributeList = buildStringFromArray(attributeIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT OBJ.* FROM "
					+METAMODEL_ALIAS+".object AS OBJ, "
					+METAMODEL_ALIAS+".attribute_value AS ATV, "
					+METAMODEL_ALIAS+".object_version AS OBJV "
					+" WHERE OBJ.id = OBJV.object_id "
					+" AND OBJV.id = ATV.object_version_id "
					+" AND ATV.attribute_name_id IN ("+attributeList+") "
					+" ORDER BY OBJ.id");
			erset = new SLEXMMObjectResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset;
	}

	@Override
	public SLEXMMCaseResultSet getCasesForObject(int objectId) {
		return getCasesForObjects(new int[] {objectId});
	}
	
	@Override
	public SLEXMMCaseResultSet getCasesForObjects(int[] objectIds) {
		SLEXMMCaseResultSet crset = null;
		Statement statement = null;
		
		String objectList = buildStringFromArray(objectIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT C.* FROM "
					+METAMODEL_ALIAS+".pcase AS C, "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".activity_instance_to_case AS AITC "
					+" WHERE C.id = AITC.case_id "
					+" AND AITC.activity_instance_id = EV.activity_instance_id "
					+" AND EV.id = ETOV.event_id "
					+" AND ETOV.object_version_id = OBJV.id "
					+" AND OBJV.object_id IN ("+objectList+") "
					+" ORDER BY C.id");
			crset = new SLEXMMCaseResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return crset;
	}

	@Override
	public SLEXMMCaseResultSet getCasesForEvent(int eventId) {
		return getCasesForEvents(new int[] {eventId});
	}
	
	@Override
	public SLEXMMCaseResultSet getCasesForEvents(int[] eventIds) {
		SLEXMMCaseResultSet crset = null;
		Statement statement = null;
		
		String eventList = buildStringFromArray(eventIds, true);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT C.* FROM "
					+METAMODEL_ALIAS+".pcase AS C, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".activity_instance_to_case AS AITC "
					+" WHERE C.id = AITC.case_id "
					+" AND AITC.activity_instance_id = EV.activity_instance_id "
					+" AND EV.id IN ("+eventList+") "
					+" ORDER BY C.id");
			crset = new SLEXMMCaseResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return crset;
	}

	@Override
	public SLEXMMCaseResultSet getCasesForActivity(int activityId) {
		return getCasesForActivities(new int[] {activityId});
	}
	
	@Override
	public SLEXMMCaseResultSet getCasesForActivities(int[] activityIds) {
		SLEXMMCaseResultSet crset = null;
		Statement statement = null;
		
		String activityList = buildStringFromArray(activityIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT C.* FROM "
					+METAMODEL_ALIAS+".pcase AS C, "
					+METAMODEL_ALIAS+".activity_instance AS AI, "
					+METAMODEL_ALIAS+".activity_instance_to_case AS AITC "
					+" WHERE C.id = AITC.case_id "
					+" AND AITC.activity_instance_id = AI.id "
					+" AND AI.activity_id IN ("+activityList+") "
					+" ORDER BY C.id");
			crset = new SLEXMMCaseResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return crset;
	}

	@Override
	public SLEXMMCaseResultSet getCasesForClass(int classId) {
		return getCasesForClasses(new int[] {classId});
	}
	
	@Override
	public SLEXMMCaseResultSet getCasesForClasses(int[] classIds) {
		SLEXMMCaseResultSet crset = null;
		Statement statement = null;
		
		String classList = buildStringFromArray(classIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT C.* FROM "
					+METAMODEL_ALIAS+".pcase AS C, "
					+METAMODEL_ALIAS+".object AS OBJ, "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".activity_instance_to_case AS AITC "
					+" WHERE C.id = AITC.case_id "
					+" AND AITC.activity_instance_id = EV.activity_instance_id "
					+" AND EV.id = ETOV.event_id "
					+" AND ETOV.object_version_id = OBJV.id "
					+" AND OBJV.object_id = OBJ.id "
					+" AND OBJ.class_id IN ("+classList+") "
					+" ORDER BY C.id");
			crset = new SLEXMMCaseResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return crset;
	}

	@Override
	public SLEXMMCaseResultSet getCasesForRelationship(int relationshipId) {
		return getCasesForRelationships(new int[] {relationshipId});
	}
	
	@Override
	public SLEXMMCaseResultSet getCasesForRelationships(int[] relationshipIds) {
		SLEXMMCaseResultSet crset = null;
		Statement statement = null;
		
		String relationshipList = buildStringFromArray(relationshipIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT C.* FROM "
					+METAMODEL_ALIAS+".pcase AS C, "
					+METAMODEL_ALIAS+".relation AS REL, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".activity_instance_to_case AS AITC "
					+" WHERE C.id = AITC.case_id "
					+" AND AITC.activity_instance_id = EV.activity_instance_id "
					+" AND EV.id = ETOV.event_id "
					+" AND OBJV.id = ETOV.object_version_id "
					+" AND (OBJV.id = REL.source_object_version_id OR OBJV.id = REL.target_object_version_id) "
					+" AND REL.relationship_id IN ("+relationshipList+") "
					+" ORDER BY C.id");
			crset = new SLEXMMCaseResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return crset;
	}

	@Override
	public SLEXMMCaseResultSet getCasesForObjectVersion(int objectVersionId) {
		return getCasesForObjectVersions(new int[] {objectVersionId});
	}
	
	@Override
	public SLEXMMCaseResultSet getCasesForObjectVersions(int[] objectVersionIds) {
		SLEXMMCaseResultSet crset = null;
		Statement statement = null;
		
		String objectVersionList = buildStringFromArray(objectVersionIds, true);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT C.* FROM "
					+METAMODEL_ALIAS+".pcase AS C, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".activity_instance_to_case AS AITC "
					+" WHERE C.id = AITC.case_id "
					+" AND AITC.activity_instance_id = EV.activity_instance_id "
					+" AND EV.id = ETOV.event_id "
					+" AND ETOV.object_version_id IN ("+objectVersionList+") "
					+" ORDER BY C.id");
			crset = new SLEXMMCaseResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return crset;
	}

	@Override
	public SLEXMMCaseResultSet getCasesForRelation(int relationId) {
		return getCasesForRelations(new int[] {relationId});
	}
	
	@Override
	public SLEXMMCaseResultSet getCasesForRelations(int[] relationIds) {
		SLEXMMCaseResultSet crset = null;
		Statement statement = null;
		
		String relationList = buildStringFromArray(relationIds, true);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT C.* FROM "
					+METAMODEL_ALIAS+".pcase AS C, "
					+METAMODEL_ALIAS+".relation AS REL, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".activity_instance_to_case AS AITC "
					+" WHERE C.id = AITC.case_id "
					+" AND AITC.activity_instance_id = EV.activity_instance_id "
					+" AND EV.id = ETOV.event_id "
					+" AND OBJV.id = ETOV.object_version_id "
					+" AND (OBJV.id = REL.source_object_version_id OR OBJV.id = REL.target_object_version_id) "
					+" AND REL.id IN ("+relationList+") "
					+" ORDER BY C.id");
			crset = new SLEXMMCaseResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return crset;
	}

	@Override
	public SLEXMMCaseResultSet getCasesForActivityInstance(
			int activityInstanceId) {
		return getCasesForActivityInstances(new int[] {activityInstanceId});
	}
	
	@Override
	public SLEXMMCaseResultSet getCasesForActivityInstances(
			int[] activityInstanceIds) {
		SLEXMMCaseResultSet crset = null;
		Statement statement = null;
		
		String activityInstanceList = buildStringFromArray(activityInstanceIds, true);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT C.* FROM "
					+METAMODEL_ALIAS+".pcase AS C, "
					+METAMODEL_ALIAS+".activity_instance_to_case AS AITC "
					+" WHERE C.id = AITC.case_id "
					+" AND AITC.activity_instance_id IN ("+activityInstanceList+") "
					+" ORDER BY C.id");
			crset = new SLEXMMCaseResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return crset;
	}

	@Override
	public SLEXMMCaseResultSet getCasesForAttribute(int attributeId) {
		return getCasesForAttributes(new int[] {attributeId});
	}
	
	@Override
	public SLEXMMCaseResultSet getCasesForAttributes(int[] attributeIds) {
		SLEXMMCaseResultSet crset = null;
		Statement statement = null;
		
		String attributeList = buildStringFromArray(attributeIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT C.* FROM "
					+METAMODEL_ALIAS+".pcase AS C, "
					+METAMODEL_ALIAS+".attribute_value AS ATV, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".activity_instance_to_case AS AITC "
					+" WHERE C.id = AITC.case_id "
					+" AND AITC.activity_instance_id = EV.activity_instance_id "
					+" AND EV.id = ETOV.event_id "
					+" AND ETOV.object_version_id = OBJV.id "
					+" AND OBJV.id = ATV.object_version_id "
					+" AND ATV.attribute_name_id IN ("+attributeList+") "
					+" ORDER BY C.id");
			crset = new SLEXMMCaseResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return crset;
	}

	@Override
	public SLEXMMEventResultSet getEventsForObject(int objectId) {
		return getEventsForObjects(new int[] {objectId});
	}
	
	@Override
	public SLEXMMEventResultSet getEventsForObjects(int[] objectIds) {
		SLEXMMEventResultSet erset = null;
		Statement statement = null;
		
		String objectList = buildStringFromArray(objectIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT EV.* FROM "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".object_version AS OBJV "
					+" WHERE EV.id = ETOV.event_id "
					+" AND ETOV.object_version_id = OBJV.id "
					+" AND OBJV.object_id IN ("+objectList+") "
					+" ORDER BY EV.ordering");
			erset = new SLEXMMEventResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset;
	}

	@Override
	public SLEXMMEventResultSet getEventsForClass(int classId) {
		return getEventsForClasses(new int[] {classId});
	}
	
	@Override
	public SLEXMMEventResultSet getEventsForClasses(int[] classIds) {
		SLEXMMEventResultSet erset = null;
		Statement statement = null;
		
		String classList = buildStringFromArray(classIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT EV.* FROM "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".object AS OBJ, "
					+METAMODEL_ALIAS+".object_version AS OBJV "
					+" WHERE EV.id = ETOV.event_id "
					+" AND ETOV.object_version_id = OBJV.id "
					+" AND OBJV.object_id = OBJ.id "
					+" AND OBJ.class_id IN ("+classList+") "
					+" ORDER BY EV.ordering");
			erset = new SLEXMMEventResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset;
	}

	@Override
	public SLEXMMEventResultSet getEventsForRelationship(int relationshipId) {
		return getEventsForRelationships(new int[] {relationshipId});
	}
	
	@Override
	public SLEXMMEventResultSet getEventsForRelationships(int[] relationshipIds) {
		SLEXMMEventResultSet erset = null;
		Statement statement = null;
		
		String relationshipList = buildStringFromArray(relationshipIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT EV.* FROM "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".relation AS REL, "
					+METAMODEL_ALIAS+".object_version AS OBJV "
					+" WHERE EV.id = ETOV.event_id "
					+" AND ETOV.object_version_id = OBJV.id "
					+" AND ( OBJV.id = REL.source_object_version_id OR OBJV.id = REL.target_object_version_id ) "
					+" AND REL.relationship_id IN ("+relationshipList+") "
					+" ORDER BY EV.ordering");
			erset = new SLEXMMEventResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset;
	}

	@Override
	public SLEXMMEventResultSet getEventsForRelation(int relationId) {
		return getEventsForRelations(new int[] {relationId});
	}
	
	@Override
	public SLEXMMEventResultSet getEventsForRelations(int[] relationIds) {
		SLEXMMEventResultSet erset = null;
		Statement statement = null;
		
		String relationList = buildStringFromArray(relationIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT EV.* FROM "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".relation AS REL, "
					+METAMODEL_ALIAS+".object_version AS OBJV "
					+" WHERE EV.id = ETOV.event_id "
					+" AND ETOV.object_version_id = OBJV.id "
					+" AND ( OBJV.id = REL.source_object_version_id OR OBJV.id = REL.target_object_version_id ) "
					+" AND REL.id IN ("+relationList+") "
					+" ORDER BY EV.ordering");
			erset = new SLEXMMEventResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset;
	}

	@Override
	public SLEXMMEventResultSet getEventsForActivityInstance(
			int activityInstanceId) {
		return getEventsForActivityInstances(new int[] {activityInstanceId});
	}
	
	@Override
	public SLEXMMEventResultSet getEventsForActivityInstances(
			int[] activityInstanceIds) {
		SLEXMMEventResultSet erset = null;
		Statement statement = null;
		
		String activityInstanceList = buildStringFromArray(activityInstanceIds, true);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT EV.* FROM "
					+METAMODEL_ALIAS+".event AS EV "
					+" WHERE EV.activity_instance_id IN ("+activityInstanceList+") "
					+" ORDER BY EV.ordering");
			erset = new SLEXMMEventResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset;
	}

	@Override
	public SLEXMMEventResultSet getEventsForAttribute(int attributeId) {
		return getEventsForAttributes(new int[] {attributeId});
	}
	
	@Override
	public SLEXMMEventResultSet getEventsForAttributes(int[] attributeIds) {
		SLEXMMEventResultSet erset = null;
		Statement statement = null;
		
		String attributeList = buildStringFromArray(attributeIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT EV.* FROM "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".attribute_value AS ATV, "
					+METAMODEL_ALIAS+".object_version AS OBJV "
					+" WHERE EV.id = ETOV.event_id "
					+" AND ETOV.object_version_id = OBJV.id "
					+" AND OBJV.id = ATV.object_version_id "
					+" AND ATV.attribute_name_id IN ("+attributeList+") "
					+" ORDER BY EV.ordering");
			erset = new SLEXMMEventResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset;
	}

	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForObject(int objId) {
		return getObjectVersionsForObjects(new int[] {objId});
	}
	
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForObjects(int[] objIds) {
		SLEXMMObjectVersionResultSet erset = null;
		Statement statement = null;
		
		String objectList = buildStringFromArray(objIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT OBJV.* FROM "
					+METAMODEL_ALIAS+".object_version AS OBJV "
					+" WHERE OBJV.object_id IN ("+objectList+") "
					+" ORDER BY OBJV.start_timestamp ");
			erset = new SLEXMMObjectVersionResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset; 
	}
	
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForEvent(int eventId) {
		return getObjectVersionsForEvents(new int[] {eventId});
	}
	
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForEvents(int[] eventIds) {
		SLEXMMObjectVersionResultSet ovrset = null;
		Statement statement = null;
		
		String eventList = buildStringFromArray(eventIds, true);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT OBJV.* FROM "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV "
					+" WHERE OBJV.id = ETOV.object_version_id "
					+" AND ETOV.event_id IN ("+eventList+") "
					+" ORDER BY OBJV.id");
			ovrset = new SLEXMMObjectVersionResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return ovrset;
	}

	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForCase(int caseId) {
		return getObjectVersionsForCases(new int[] {caseId});
	}
	
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForCases(int[] caseIds) {
		SLEXMMObjectVersionResultSet ovrset = null;
		Statement statement = null;
		
		String caseList = buildStringFromArray(caseIds, true);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT OBJV.* FROM "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".activity_instance_to_case AS AITC, "
					+METAMODEL_ALIAS+".event AS EV "
					+" WHERE OBJV.id = ETOV.object_version_id "
					+" AND ETOV.event_id = EV.id "
					+" AND EV.activity_instance_id = AITC.activity_instance_id "
					+" AND AITC.case_id IN ("+caseList+") "
					+" ORDER BY OBJV.id");
			ovrset = new SLEXMMObjectVersionResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return ovrset;
	}

	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForClass(int classId) {
		return getObjectVersionsForClasses(new int[] {classId});
	}
	
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForClasses(int[] classIds) {
		SLEXMMObjectVersionResultSet ovrset = null;
		Statement statement = null;
		
		String classList = buildStringFromArray(classIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT OBJV.* FROM "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".object AS OBJ "
					+" WHERE OBJV.object_id = OBJ.id "
					+" AND OBJ.class_id IN ("+classList+") "
					+" ORDER BY OBJV.id");
			ovrset = new SLEXMMObjectVersionResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return ovrset;
	}

	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForRelationship(
			int relationshipId) {
		return getObjectVersionsForRelationships(new int[] {relationshipId});
	}
	
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForRelationships(
			int[] relationshipIds) {
		SLEXMMObjectVersionResultSet ovrset = null;
		Statement statement = null;
		
		String relationshipList = buildStringFromArray(relationshipIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT OBJV.* FROM "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".relation AS REL "
					+" WHERE ( OBJV.id = REL.source_object_version_id OR REL.target_object_version_id ) "
					+" AND REL.relationship_id IN ("+relationshipList+") "
					+" ORDER BY OBJV.id");
			ovrset = new SLEXMMObjectVersionResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return ovrset;
	}

	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForRelation(
			int relationId) {
		return getObjectVersionsForRelations(new int[] {relationId});
	}
	
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForRelations(
			int[] relationIds) {
		SLEXMMObjectVersionResultSet ovrset = null;
		Statement statement = null;
		
		String relationList = buildStringFromArray(relationIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT OBJV.* FROM "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".relation AS REL "
					+" WHERE ( OBJV.id = REL.source_object_version_id OR REL.target_object_version_id ) "
					+" AND REL.id IN ("+relationList+") "
					+" ORDER BY OBJV.id");
			ovrset = new SLEXMMObjectVersionResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return ovrset;
	}

	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForActivityInstance(
			int activityInstanceId) {
		return getObjectVersionsForActivityInstances(new int[] {activityInstanceId});
	}
	
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForActivityInstances(
			int[] activityInstanceIds) {
		SLEXMMObjectVersionResultSet ovrset = null;
		Statement statement = null;
		
		String activityInstanceList = buildStringFromArray(activityInstanceIds, true);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT OBJV.* FROM "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".event AS EV "
					+" WHERE OBJV.id = ETOV.object_version_id "
					+" AND ETOV.event_id = EV.id "
					+" AND EV.activity_instance_id IN ("+activityInstanceList+") "
					+" ORDER BY OBJV.id");
			ovrset = new SLEXMMObjectVersionResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return ovrset;
	}

	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForActivity(
			int activityId) {
		return getObjectVersionsForActivities(new int[] {activityId});
	}
	
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForActivities( // TODO CHECK
			int[] activityIds) {
		SLEXMMObjectVersionResultSet erset = null;
		Statement statement = null;
		String query = "";
		
		String activityList = buildStringFromArray(activityIds, false);
		
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
					+" AND AI.activity_id IN ("+activityList+") ";
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
	public SLEXMMObjectVersionResultSet getObjectVersionsForAttribute(
			int attributeId) {
		return getObjectVersionsForAttributes(new int[] {attributeId});
	}
	
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForAttributes(
			int[] attributeIds) {
		SLEXMMObjectVersionResultSet ovrset = null;
		Statement statement = null;
		
		String attributeList = buildStringFromArray(attributeIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT OBJV.* FROM "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".attribute_value AS ATV "
					+" WHERE OBJV.id = ATV.object_version_id "
					+" AND ATV.attribute_name_id IN ("+attributeList+") "
					+" ORDER BY OBJV.id");
			ovrset = new SLEXMMObjectVersionResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return ovrset;
	}

	@Override
	public SLEXMMActivityResultSet getActivitiesForObject(int objectId) {
		return getActivitiesForObjects(new int[] {objectId});
	}
	
	@Override
	public SLEXMMActivityResultSet getActivitiesForObjects(int[] objectIds) {
		SLEXMMActivityResultSet arset = null;
		Statement statement = null;
		
		String objectList = buildStringFromArray(objectIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT AC.* FROM "
					+METAMODEL_ALIAS+".activity AS AC, "
					+METAMODEL_ALIAS+".activity_instance AS AI, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".object_version AS OBJV "
					+" WHERE AC.id = AI.activity_id "
					+" AND AI.id = EV.activity_instance_id "
					+" AND EV.id = ETOV.event_id "
					+" AND ETOV.object_version_id = OBJV.id "
					+" AND OBJV.object_id IN ("+objectList+") "
					+" ORDER BY AC.id");
			arset = new SLEXMMActivityResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return arset;
	}

	@Override
	public SLEXMMActivityResultSet getActivitiesForEvent(int eventId) {
		return getActivitiesForEvents(new int[] {eventId});
	}
	
	@Override
	public SLEXMMActivityResultSet getActivitiesForEvents(int[] eventIds) {
		SLEXMMActivityResultSet arset = null;
		Statement statement = null;
		
		String eventList = buildStringFromArray(eventIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT AC.* FROM "
					+METAMODEL_ALIAS+".activity AS AC, "
					+METAMODEL_ALIAS+".activity_instance AS AI, "
					+METAMODEL_ALIAS+".event AS EV "
					+" WHERE AC.id = AI.activity_id "
					+" AND AI.id = EV.activity_instance_id "
					+" AND EV.id IN ("+eventList+") "
					+" ORDER BY AC.id");
			arset = new SLEXMMActivityResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return arset;
	}

	@Override
	public SLEXMMActivityResultSet getActivitiesForCase(int caseId) {
		return getActivitiesForCases(new int[] {caseId});
	}
	
	@Override
	public SLEXMMActivityResultSet getActivitiesForCases(int[] caseIds) {
		SLEXMMActivityResultSet arset = null;
		Statement statement = null;
		
		String caseList = buildStringFromArray(caseIds, true);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT AC.* FROM "
					+METAMODEL_ALIAS+".activity AS AC, "
					+METAMODEL_ALIAS+".activity_instance AS AI, "
					+METAMODEL_ALIAS+".activity_instance_to_case AS AITC "
					+" WHERE AC.id = AI.activity_id "
					+" AND AI.id = AITC.activity_instance_id "
					+" AND AITC.case_id IN ("+caseList+") "
					+" ORDER BY AC.id");
			arset = new SLEXMMActivityResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return arset;
	}

	@Override
	public SLEXMMActivityResultSet getActivitiesForClass(int classId) {
		return getActivitiesForClasses(new int[] {classId});
	}
	
	@Override
	public SLEXMMActivityResultSet getActivitiesForClasses(int[] classIds) {
		SLEXMMActivityResultSet arset = null;
		Statement statement = null;
		
		String classList = buildStringFromArray(classIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT AC.* FROM "
					+METAMODEL_ALIAS+".activity AS AC, "
					+METAMODEL_ALIAS+".activity_instance AS AI, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".object AS OBJ "
					+" WHERE AC.id = AI.activity_id "
					+" AND AI.id = EV.activity_instance_id "
					+" AND EV.id = ETOV.event_id "
					+" AND ETOV.object_version_id = OBJV.id "
					+" AND OBJV.object_id = OBJ.id "
					+" AND OBJ.class_id IN ("+classList+") "
					+" ORDER BY AC.id");
			arset = new SLEXMMActivityResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return arset;
	}

	@Override
	public SLEXMMActivityResultSet getActivitiesForRelationship(
			int relationshipId) {
		return getActivitiesForRelationships(new int[] {relationshipId});
	}
	
	@Override
	public SLEXMMActivityResultSet getActivitiesForRelationships(
			int[] relationshipIds) {
		SLEXMMActivityResultSet arset = null;
		Statement statement = null;
		
		String relationshipList = buildStringFromArray(relationshipIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT AC.* FROM "
					+METAMODEL_ALIAS+".activity AS AC, "
					+METAMODEL_ALIAS+".activity_instance AS AI, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".relation AS REL "
					+" WHERE AC.id = AI.activity_id "
					+" AND AI.id = EV.activity_instance_id "
					+" AND EV.id = ETOV.event_id "
					+" AND ETOV.object_version_id = OBJV.id "
					+" AND ( OBJV.id = REL.source_object_version_id OR OBJV.id = REL.target_object_version_id ) "
					+" AND REL.relationship_id IN ("+relationshipList+") "
					+" ORDER BY AC.id");
			arset = new SLEXMMActivityResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return arset;
	}

	@Override
	public SLEXMMActivityResultSet getActivitiesForObjectVersion(
			int objectVersionId) {
		return getActivitiesForObjectVersions(new int[] {objectVersionId});
	}
	
	@Override
	public SLEXMMActivityResultSet getActivitiesForObjectVersions(
			int[] objectVersionIds) {
		SLEXMMActivityResultSet arset = null;
		Statement statement = null;
		
		String objectVersionList = buildStringFromArray(objectVersionIds, true);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT AC.* FROM "
					+METAMODEL_ALIAS+".activity AS AC, "
					+METAMODEL_ALIAS+".activity_instance AS AI, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV "
					+" WHERE AC.id = AI.activity_id "
					+" AND AI.id = EV.activity_instance_id "
					+" AND EV.id = ETOV.event_id "
					+" AND ETOV.object_version_id IN ("+objectVersionList+") "
					+" ORDER BY AC.id");
			arset = new SLEXMMActivityResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return arset;
	}

	@Override
	public SLEXMMActivityResultSet getActivitiesForRelation(int relationId) {
		return getActivitiesForRelations(new int[] {relationId});
	}
	
	@Override
	public SLEXMMActivityResultSet getActivitiesForRelations(int[] relationIds) {
		SLEXMMActivityResultSet arset = null;
		Statement statement = null;
		
		String relationList = buildStringFromArray(relationIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT AC.* FROM "
					+METAMODEL_ALIAS+".activity AS AC, "
					+METAMODEL_ALIAS+".activity_instance AS AI, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".relation AS REL "
					+" WHERE AC.id = AI.activity_id "
					+" AND AI.id = EV.activity_instance_id "
					+" AND EV.id = ETOV.event_id "
					+" AND ETOV.object_version_id = OBJV.id "
					+" AND ( OBJV.id = REL.source_object_version_id OR OBJV.id = REL.target_object_version_id ) "
					+" AND REL.id IN ("+relationList+") "
					+" ORDER BY AC.id");
			arset = new SLEXMMActivityResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return arset;
	}

	@Override
	public SLEXMMActivityResultSet getActivitiesForActivityInstance(
			int activityInstanceId) {
		return getActivitiesForActivityInstances(new int[] {activityInstanceId});
	}
	
	@Override
	public SLEXMMActivityResultSet getActivitiesForActivityInstances(
			int[] activityInstanceIds) {
		SLEXMMActivityResultSet arset = null;
		Statement statement = null;
		
		String activityInstanceList = buildStringFromArray(activityInstanceIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT AC.* FROM "
					+METAMODEL_ALIAS+".activity AS AC, "
					+METAMODEL_ALIAS+".activity_instance AS AI "
					+" WHERE AC.id = AI.activity_id "
					+" AND AI.id IN ("+activityInstanceList+") "
					+" ORDER BY AC.id");
			arset = new SLEXMMActivityResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return arset;
	}

	@Override
	public SLEXMMActivityResultSet getActivitiesForAttribute(int attributeId) {
		return getActivitiesForAttributes(new int[] {attributeId});
	}
	
	@Override
	public SLEXMMActivityResultSet getActivitiesForAttributes(int[] attributeIds) {
		SLEXMMActivityResultSet arset = null;
		Statement statement = null;
		
		String attributeList = buildStringFromArray(attributeIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT AC.* FROM "
					+METAMODEL_ALIAS+".activity AS AC, "
					+METAMODEL_ALIAS+".activity_instance AS AI, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".object AS OBJ, "
					+METAMODEL_ALIAS+".class AS C, "
					+METAMODEL_ALIAS+".attribute_name AS AN "
					+" WHERE AC.id = AI.activity_id "
					+" AND AI.id = EV.activity_instance_id "
					+" AND EV.id = ETOV.event_id "
					+" AND ETOV.object_version_id = OBJV.id "
					+" AND OBJV.object_id = OBJ.id "
					+" AND OBJ.class_id = C.id "
					+" AND C.id = AN.class_id "
					+" AND AN.id IN ("+attributeList+") "
					+" ORDER BY AC.id");
			arset = new SLEXMMActivityResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return arset;
	}

	@Override
	public SLEXMMClassResultSet getClassesForObject(int objectId) {
		return getClassesForObjects(new int[] {objectId});
	}
	
	@Override
	public SLEXMMClassResultSet getClassesForObjects(int[] objectIds) {
		SLEXMMClassResultSet crset = null;
		Statement statement = null;
		
		String objectList = buildStringFromArray(objectIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT C.* FROM "
					+METAMODEL_ALIAS+".class AS C, "
					+METAMODEL_ALIAS+".object AS OBJ "
					+" WHERE C.id = OBJ.class_id "
					+" AND OBJ.id IN ("+objectList+") "
					+" ORDER BY C.id");
			crset = new SLEXMMClassResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return crset;
	}

	@Override
	public SLEXMMClassResultSet getClassesForEvent(int eventId) {
		return getClassesForEvents(new int[] {eventId});
	}
	
	@Override
	public SLEXMMClassResultSet getClassesForEvents(int[] eventIds) {
		SLEXMMClassResultSet crset = null;
		Statement statement = null;
		
		String eventList = buildStringFromArray(eventIds, true);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT C.* FROM "
					+METAMODEL_ALIAS+".class AS C, "
					+METAMODEL_ALIAS+".object AS OBJ, "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV "
					+" WHERE C.id = OBJ.class_id "
					+" AND OBJ.id = OBJV.object_id "
					+" AND OBJV.id = ETOV.object_version_id "
					+" AND ETOV.event_id IN ("+eventList+") "
					+" ORDER BY C.id");
			crset = new SLEXMMClassResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return crset;
	}

	@Override
	public SLEXMMClassResultSet getClassesForCase(int caseId) {
		return getClassesForCases(new int[] {caseId});
	}
	
	@Override
	public SLEXMMClassResultSet getClassesForCases(int[] caseIds) {
		SLEXMMClassResultSet crset = null;
		Statement statement = null;
		
		String caseList = buildStringFromArray(caseIds, true);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT C.* FROM "
					+METAMODEL_ALIAS+".class AS C, "
					+METAMODEL_ALIAS+".object AS OBJ, "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".activity_instance_to_case AS AITC "
					+" WHERE C.id = OBJ.class_id "
					+" AND OBJ.id = OBJV.object_id "
					+" AND OBJV.id = ETOV.object_version_id "
					+" AND ETOV.event_id = EV.id "
					+" AND EV.activity_instance_id = AITC.activity_instance_id "
					+" AND AITC.case_id IN ("+caseList+") "
					+" ORDER BY C.id");
			crset = new SLEXMMClassResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return crset;
	}

	@Override
	public SLEXMMClassResultSet getClassesForActivity(int activityId) {
		return getClassesForActivities(new int[] {activityId});
	}
	
	@Override
	public SLEXMMClassResultSet getClassesForActivities(int[] activityIds) {
		SLEXMMClassResultSet crset = null;
		Statement statement = null;
		
		String activityList = buildStringFromArray(activityIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT C.* FROM "
					+METAMODEL_ALIAS+".class AS C, "
					+METAMODEL_ALIAS+".object AS OBJ, "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".activity_instance AS AI "
					+" WHERE C.id = OBJ.class_id "
					+" AND OBJ.id = OBJV.object_id "
					+" AND OBJV.id = ETOV.object_version_id "
					+" AND ETOV.event_id = EV.id "
					+" AND EV.activity_instance_id = AI.id "
					+" AND AI.activity_id IN ("+activityList+") "
					+" ORDER BY C.id");
			crset = new SLEXMMClassResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return crset;
	}

	@Override
	public SLEXMMClassResultSet getClassesForRelationship(int relationshipId) {
		return getClassesForRelationships(new int[] {relationshipId});
	}
	
	@Override
	public SLEXMMClassResultSet getClassesForRelationships(int[] relationshipIds) {
		SLEXMMClassResultSet crset = null;
		Statement statement = null;
		
		String relationshipList = buildStringFromArray(relationshipIds, true);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT C.* FROM "
					+METAMODEL_ALIAS+".class AS C, "
					+METAMODEL_ALIAS+".relationship AS RS "
					+" WHERE ( C.id = RS.source OR C.id = RS.target ) "
					+" AND RS.id IN ("+relationshipList+") "
					+" ORDER BY C.id");
			crset = new SLEXMMClassResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return crset;
	}

	@Override
	public SLEXMMClassResultSet getClassesForObjectVersion(
			int objectVersionId) {
		return getClassesForObjectVersions(new int[] {objectVersionId});
	}
	
	@Override
	public SLEXMMClassResultSet getClassesForObjectVersions(
			int[] objectVersionIds) {
		SLEXMMClassResultSet crset = null;
		Statement statement = null;
		
		String objectVersionList = buildStringFromArray(objectVersionIds, true);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT C.* FROM "
					+METAMODEL_ALIAS+".class AS C, "
					+METAMODEL_ALIAS+".object AS OBJ, "
					+METAMODEL_ALIAS+".object_version AS OBJV "
					+" WHERE C.id = OBJ.class_id "
					+" AND OBJ.id = OBJV.object_id "
					+" AND OBJV.id IN ("+objectVersionList+") "
					+" ORDER BY C.id");
			crset = new SLEXMMClassResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return crset;
	}

	@Override
	public SLEXMMClassResultSet getClassesForRelation(int relationId) {
		return getClassesForRelations(new int[] {relationId});
	}
	
	@Override
	public SLEXMMClassResultSet getClassesForRelations(int[] relationIds) {
		SLEXMMClassResultSet crset = null;
		Statement statement = null;
		
		String relationList = buildStringFromArray(relationIds, true);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT C.* FROM "
					+METAMODEL_ALIAS+".class AS C, "
					+METAMODEL_ALIAS+".object AS OBJ, "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".relation AS REL "
					+" WHERE C.id = OBJ.class_id "
					+" AND OBJ.id = OBJV.object_id "
					+" AND ( OBJV.id = REL.source_object_version_id OR OBJV.id = REL.target_object_version_id ) "
					+" AND REL.id IN ("+relationList+") "
					+" ORDER BY C.id");
			crset = new SLEXMMClassResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return crset;
	}

	@Override
	public SLEXMMClassResultSet getClassesForActivityInstance(
			int activityInstanceId) {
		return getClassesForActivityInstances(new int[] {activityInstanceId});
	}
	
	@Override
	public SLEXMMClassResultSet getClassesForActivityInstances(
			int[] activityInstanceIds) {
		SLEXMMClassResultSet crset = null;
		Statement statement = null;
		
		String activityInstanceList = buildStringFromArray(activityInstanceIds, true);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT C.* FROM "
					+METAMODEL_ALIAS+".class AS C, "
					+METAMODEL_ALIAS+".object AS OBJ, "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".event AS EV "
					+" WHERE C.id = OBJ.class_id "
					+" AND OBJ.id = OBJV.object_id "
					+" AND OBJV.id = ETOV.object_version_id "
					+" AND ETOV.event_id = EV.id "
					+" AND EV.activity_instance_id IN ("+activityInstanceList+") "
					+" ORDER BY C.id");
			crset = new SLEXMMClassResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return crset;
	}

	@Override
	public SLEXMMClassResultSet getClassesForAttribute(int attributeId) {
		return getClassesForAttributes(new int[] {attributeId});
	}
	
	@Override
	public SLEXMMClassResultSet getClassesForAttributes(int[] attributeIds) {
		SLEXMMClassResultSet crset = null;
		Statement statement = null;
		
		String attributeList = buildStringFromArray(attributeIds, true);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT C.* FROM "
					+METAMODEL_ALIAS+".class AS C, "
					+METAMODEL_ALIAS+".attribute_name AS AN "
					+" WHERE C.id = AN.class_id "
					+" AND AN.id IN ("+attributeList+") "
					+" ORDER BY C.id");
			crset = new SLEXMMClassResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return crset;
	}

	@Override
	public SLEXMMRelationResultSet getRelationsForObject(int objectId) {
		return getRelationsForObjects(new int[] {objectId});
	}
	
	@Override
	public SLEXMMRelationResultSet getRelationsForObjects(int[] objectIds) {
		SLEXMMRelationResultSet rrset = null;
		Statement statement = null;
		
		String objectList = buildStringFromArray(objectIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT REL.* FROM "
					+METAMODEL_ALIAS+".relation AS REL, "
					+METAMODEL_ALIAS+".object_version AS OBJV "
					+" WHERE ( REL.source_object_version_id = OBJV.id OR REL.target_object_version_id = OBJV.id ) "
					+" AND OBJV.object_id IN ("+objectList+") "
					+" ORDER BY REL.id");
			rrset = new SLEXMMRelationResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return rrset;
	}

	@Override
	public SLEXMMRelationResultSet getRelationsForEvent(int eventId) {
		return getRelationsForEvents(new int[] {eventId});
	}
	
	@Override
	public SLEXMMRelationResultSet getRelationsForEvents(int[] eventIds) {
		SLEXMMRelationResultSet rrset = null;
		Statement statement = null;
		
		String eventList = buildStringFromArray(eventIds, true);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT REL.* FROM "
					+METAMODEL_ALIAS+".relation AS REL, "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV "
					+" WHERE ( REL.source_object_version_id = OBJV.id OR REL.target_object_version_id = OBJV.id ) "
					+" AND OBJV.id = ETOV.object_version_id "
					+" AND ETOV.event_id = ("+eventList+") "
					+" ORDER BY REL.id");
			rrset = new SLEXMMRelationResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return rrset;
	}

	@Override
	public SLEXMMRelationResultSet getRelationsForCase(int caseId) {
		return getRelationsForCases(new int[] {caseId});
	}
	
	@Override
	public SLEXMMRelationResultSet getRelationsForCases(int[] caseIds) { // TODO Check
		SLEXMMRelationResultSet rrset = null;
		Statement statement = null;
		
		String caseIdList = buildStringFromArray(caseIds,true);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT REL.* FROM "
					+METAMODEL_ALIAS+".relation AS REL, "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".activity_instance_to_case AS AITC "
					+" WHERE ( REL.source_object_version_id = OBJV.id OR REL.target_object_version_id = OBJV.id ) "
					+" AND OBJV.id = ETOV.object_version_id "
					+" AND ETOV.event_id = EV.id "
					+" AND EV.activity_instance_id = AITC.activity_instance_id "
					+" AND AITC.case_id IN ("+caseIdList+") "
					+" ORDER BY REL.id");
			rrset = new SLEXMMRelationResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return rrset;
	}

	@Override
	public SLEXMMRelationResultSet getRelationsForActivity(int activityId) { // TODO Check
		return getRelationsForActivities(new int[] {activityId});
	}
	
	@Override
	public SLEXMMRelationResultSet getRelationsForActivities(int[] activityIds) { // TODO Check
		SLEXMMRelationResultSet rrset = null;
		Statement statement = null;
		
		String activityList = buildStringFromArray(activityIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT REL.* FROM "
					+METAMODEL_ALIAS+".relation AS REL, "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".activity_instance AS AI "
					+" WHERE ( REL.source_object_version_id = OBJV.id OR REL.target_object_version_id = OBJV.id ) "
					+" AND OBJV.id = ETOV.object_version_id "
					+" AND ETOV.event_id = EV.id "
					+" AND EV.activity_instance_id = AI.id "
					+" AND AI.activity_id IN ("+activityList+") "
					+" ORDER BY REL.id");
			rrset = new SLEXMMRelationResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return rrset;
	}

	private String buildStringFromArray(int[] ids, boolean quotes) {
		StringBuffer buf = new StringBuffer();
		
		for (int i = 0; i < ids.length; i++) {
			if (quotes) {
				buf.append("'");
				buf.append(String.valueOf(ids[i]));
				buf.append("'");
			} else {
				buf.append(String.valueOf(ids[i]));
			}
			if (i < ids.length - 1) {
				buf.append(",");
			}
		}
		
		return buf.toString();
	}
	
	@Override
	public SLEXMMRelationResultSet getRelationsForClass(int classId) {
		return getRelationsForClasses(new int[] {classId});
	}

	@Override
	public SLEXMMRelationResultSet getRelationsForClasses(int[] classIds) {
		SLEXMMRelationResultSet rrset = null;
		Statement statement = null;
		
		String classIdList = buildStringFromArray(classIds,false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT REL.* FROM "
					+METAMODEL_ALIAS+".relation AS REL, "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".object AS OBJ "
					+" WHERE ( REL.source_object_version_id = OBJV.id OR REL.target_object_version_id = OBJV.id ) "
					+" AND OBJV.object_id = OBJ.id "
					+" AND OBJ.class_id IN ("+classIdList+") "
					+" ORDER BY REL.id");
			rrset = new SLEXMMRelationResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return rrset;
	}
	
	@Override
	public SLEXMMRelationResultSet getRelationsForRelationship(
			int relationshipId) {
		return getRelationsForRelationships(new int[] {relationshipId});
	}
	
	@Override
	public SLEXMMRelationResultSet getRelationsForRelationships(
			int[] relationshipIds) {
		SLEXMMRelationResultSet rrset = null;
		Statement statement = null;
		
		String relationshipList = buildStringFromArray(relationshipIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT REL.* FROM "
					+METAMODEL_ALIAS+".relation AS REL "
					+" WHERE REL.relationship_id IN ("+relationshipList+") "
					+" ORDER BY REL.id");
			rrset = new SLEXMMRelationResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return rrset;
	}

	@Override
	public SLEXMMRelationResultSet getRelationsForObjectVersion(
			int objectVersionId) {
		return getRelationsForObjectVersions(new int []{objectVersionId});
	}
	
	@Override
	public SLEXMMRelationResultSet getRelationsForObjectVersions(
			int[] objectVersionIds) {
		SLEXMMRelationResultSet rrset = null;
		Statement statement = null;
		
		String objectVersionList = buildStringFromArray(objectVersionIds, false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT REL.* FROM "
					+METAMODEL_ALIAS+".relation AS REL, "
					+METAMODEL_ALIAS+".object_version AS OBJV "
					+" WHERE ( REL.source_object_version_id = OBJV.id OR REL.target_object_version_id = OBJV.id ) "
					+" AND OBJV.id IN ("+objectVersionList+") "
					+" ORDER BY REL.id");
			rrset = new SLEXMMRelationResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return rrset;
	}

	@Override
	public SLEXMMRelationResultSet getRelationsForActivityInstance(
			int activityInstanceId) { // TODO Check
		return getRelationsForActivityInstances(new int[] {activityInstanceId});
	}
	
	@Override
	public SLEXMMRelationResultSet getRelationsForActivityInstances(
			int[] activityInstanceIds) { // TODO Check
		SLEXMMRelationResultSet rrset = null;
		Statement statement = null;
		
		String activityInstancesList = buildStringFromArray(activityInstanceIds, false);
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT REL.* FROM "
					+METAMODEL_ALIAS+".relation AS REL, "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".event AS EV "
					+" WHERE ( REL.source_object_version_id = OBJV.id OR REL.target_object_version_id = OBJV.id ) "
					+" AND OBJV.id = ETOV.object_version_id "
					+" AND ETOV.event_id = EV.id "
					+" AND EV.activity_instance_id IN ("+activityInstancesList+") "
					+" ORDER BY REL.id");
			rrset = new SLEXMMRelationResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return rrset;
	}

	@Override
	public SLEXMMRelationResultSet getRelationsForAttribute(int attributeId) {
		return getRelationsForAttributes(new int[] {attributeId});
	}
	
	@Override
	public SLEXMMRelationResultSet getRelationsForAttributes(int[] attributeIds) {
		SLEXMMRelationResultSet rrset = null;
		Statement statement = null;
		
		String attributeIdList = buildStringFromArray(attributeIds,false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT REL.* FROM "
					+METAMODEL_ALIAS+".relation AS REL, "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".object AS OBJ, "
					+METAMODEL_ALIAS+".class AS C, "
					+METAMODEL_ALIAS+".attribute_name AS AN "
					+" WHERE ( REL.source_object_version_id = OBJV.id OR REL.target_object_version_id = OBJV.id ) "
					+" AND OBJV.object_id = OBJ.id "
					+" AND OBJ.class_id = C.id "
					+" AND AN.class_id = C.id "
					+" AND AN.id IN ("+attributeIdList+") "
					+" ORDER BY REL.id");
			rrset = new SLEXMMRelationResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return rrset;
	}

	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForObject(int objectId) { // TODO Check
		return getRelationshipsForObjects(new int[]{objectId});
	}
	
	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForObjects(int[] objectIds) { // TODO Check
		SLEXMMRelationshipResultSet rrset = null;
		Statement statement = null;
		
		String objectList = buildStringFromArray(objectIds,false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT RS.* FROM "
					+METAMODEL_ALIAS+".relationship AS RS, "
					+METAMODEL_ALIAS+".relation AS REL, "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".object AS OBJ "
					+" WHERE RS.id = REL.relationship_id "
					+" AND ( REL.source_object_version_id = OBJV.id OR REL.target_object_version_id = OBJV.id ) "
					+" AND OBJV.object_id = OBJ.id "
					+" AND OBJ.id IN ("+objectList+") "
					+" ORDER BY RS.id");
			rrset = new SLEXMMRelationshipResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return rrset;
	}

	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForEvent(int eventId) { // TODO Check
		return getRelationshipsForEvents(new int[] {eventId});
	}
	
	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForEvents(int[] eventIds) { // TODO Check
		SLEXMMRelationshipResultSet rrset = null;
		Statement statement = null;
		
		String eventList = buildStringFromArray(eventIds,false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT RS.* FROM "
					+METAMODEL_ALIAS+".relationship AS RS, "
					+METAMODEL_ALIAS+".relation AS REL, "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV "
					+" WHERE RS.id = REL.relationship_id "
					+" AND ( REL.source_object_version_id = OBJV.id OR REL.target_object_version_id = OBJV.id ) "
					+" AND OBJV.id = ETOV.object_version_id "
					+" AND ETOV.event_id IN ("+eventList+") "
					+" ORDER BY RS.id");
			rrset = new SLEXMMRelationshipResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return rrset;
	}

	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForCase(int caseId) { // TODO Check
		return getRelationshipsForCases(new int[] {caseId});
	}
	
	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForCases(int[] caseIds) { // TODO Check
		SLEXMMRelationshipResultSet rrset = null;
		Statement statement = null;
		
		String caseList = buildStringFromArray(caseIds,false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT RS.* FROM "
					+METAMODEL_ALIAS+".relationship AS RS, "
					+METAMODEL_ALIAS+".relation AS REL, "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".activity_instance_to_case AS AITC "
					+" WHERE RS.id = REL.relationship_id "
					+" AND ( REL.source_object_version_id = OBJV.id OR REL.target_object_version_id = OBJV.id ) "
					+" AND OBJV.id = ETOV.object_version_id "
					+" AND ETOV.event_id = EV.id "
					+" AND EV.activity_instance_id = AITC.activity_instance_id "
					+" AND AITC.case_id IN ("+caseList+") "
					+" ORDER BY RS.id");
			rrset = new SLEXMMRelationshipResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return rrset;
	}

	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForActivity(
			int activityId) { // TODO Check
		return getRelationshipsForActivities(new int[] {activityId});
	}
	
	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForActivities(
			int[] activityIds) { // TODO Check
		SLEXMMRelationshipResultSet rrset = null;
		Statement statement = null;
		
		String activityList = buildStringFromArray(activityIds,false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT RS.* FROM "
					+METAMODEL_ALIAS+".relationship AS RS, "
					+METAMODEL_ALIAS+".relation AS REL, "
					+METAMODEL_ALIAS+".object_version AS OBJV, "
					+METAMODEL_ALIAS+".event_to_object_version AS ETOV, "
					+METAMODEL_ALIAS+".event AS EV, "
					+METAMODEL_ALIAS+".activity_instance AS AI "
					+" WHERE RS.id = REL.relationship_id "
					+" AND ( REL.source_object_version_id = OBJV.id OR REL.target_object_version_id = OBJV.id ) "
					+" AND OBJV.id = ETOV.object_version_id "
					+" AND ETOV.event_id = EV.id "
					+" AND EV.activity_instance_id = AI.id "
					+" AND AI.activity_id IN ("+activityList+") "
					+" ORDER BY RS.id");
			rrset = new SLEXMMRelationshipResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return rrset;
	}

	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForClass(int classId) { // TODO Check
		return getRelationshipsForClasses(new int[] {classId});
	}
	
	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForClasses(int[] classIds) { // TODO Check
		SLEXMMRelationshipResultSet rrset = null;
		Statement statement = null;
		
		String classList = buildStringFromArray(classIds,false);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT DISTINCT RS.* FROM "
					+METAMODEL_ALIAS+".relationship AS RS, "
					+METAMODEL_ALIAS+".class AS CL "
					+" WHERE ( RS.source = CL.id OR RS.target = CL.id ) "
					+" AND CL.id IN ("+classList+") "
					+" ORDER BY RS.id");
			rrset = new SLEXMMRelationshipResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return rrset;
	}

	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForObjectVersion(
			int objectVersionId) { // TODO Check
		return getRelationshipsForObjectVersions(new int[] {objectVersionId});
	}
	
	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForObjectVersions(
			int[] objectVersionIds) { // TODO Check
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForRelation(
			int relationId) { // TODO Check
		return getRelationshipsForRelations(new int[] {relationId});
	}
	
	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForRelations(int[] relationIds) { // TODO Check
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForActivityInstance(
			int activityInstanceId) { // TODO Check
		return getRelationshipsForActivityInstances(new int[] {activityInstanceId});
	}
	
	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForActivityInstances(
			int[] activityInstanceIds) { // TODO Check
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForAttribute(
			int attributeId) {
		return getRelationshipsForAttributes(new int[]{attributeId});
	}
	
	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForAttributes(
			int[] attributeIds) { // TODO Check
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForCase(
			int caseId) {
		return getActivityInstancesForCases(new int[] {caseId});
	}
	
	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForCases(
			int[] caseIds) {
		SLEXMMActivityInstanceResultSet airset = null;
		Statement statement = null;
		
		String caseList = buildStringFromArray(caseIds, true);
		
		try {
			statement = connection.createStatement();
			ResultSet rset = statement.executeQuery("SELECT AI.* FROM "
					+METAMODEL_ALIAS+".activity_instance AI, "
					+METAMODEL_ALIAS+".activity_instance_to_case AIC "
							+ " WHERE AI.id = AIC.activity_instance_id "
							+ " AND AIC.case_id IN ("+caseList+") ");
			airset = new SLEXMMActivityInstanceResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return airset;
	}

	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForObject(
			int objectId) {
		return getActivityInstancesForObjects(new int[] {objectId});
	}

	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForObjects(
			int[] objectIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForEvent(
			int eventId) {
		return getActivityInstancesForEvents(new int[] {eventId});
	}

	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForEvents(
			int[] eventIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForActivity(
			int activityId) {
		return getActivityInstancesForActivities(new int[] {activityId});
	}

	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForActivities(
			int[] activityIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForClass(
			int classId) {
		return getActivityInstancesForClasses(new int[] {classId});
	}

	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForClasses(
			int[] classIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForRelationship(
			int relationshipId) {
		return getActivityInstancesForRelationships(new int[] {relationshipId});
	}

	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForRelationships(
			int[] relationshipIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForObjectVersion(
			int objectVersionId) {
		return getActivityInstancesForObjectVersions(new int[] {objectVersionId});
	}

	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForObjectVersions(
			int[] objectVersionIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForRelation(
			int relationId) {
		return getActivityInstancesForRelations(new int[] {relationId});
	}

	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForRelations(
			int[] relationIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForAttribute(
			int attributeId) {
		return getActivityInstancesForAttributes(new int[] {attributeId});
	}

	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForAttributes(
			int[] attributeIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SLEXMMAttributeResultSet getAttributesForObject(int objectId) {
		return getAttributesForObjects(new int[] {objectId});
	}

	@Override
	public SLEXMMAttributeResultSet getAttributesForObjects(int[] objectIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SLEXMMAttributeResultSet getAttributesForEvent(int eventId) {
		return getAttributesForEvents(new int[] {eventId});
	}

	@Override
	public SLEXMMAttributeResultSet getAttributesForEvents(int[] eventIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SLEXMMAttributeResultSet getAttributesForCase(int caseId) {
		return getAttributesForCases(new int[] {caseId});
	}

	@Override
	public SLEXMMAttributeResultSet getAttributesForCases(int[] caseIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SLEXMMAttributeResultSet getAttributesForActivity(int activityId) {
		return getAttributesForActivities(new int[] {activityId});
	}

	@Override
	public SLEXMMAttributeResultSet getAttributesForActivities(int[] activityIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SLEXMMAttributeResultSet getAttributesForClass(int classId) {
		return getAttributesForClasses(new int[] {classId});
	}

	@Override
	public SLEXMMAttributeResultSet getAttributesForClasses(int[] classIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SLEXMMAttributeResultSet getAttributesForRelationship(
			int relationshipId) {
		return getAttributesForRelationships(new int[] {relationshipId});
	}

	@Override
	public SLEXMMAttributeResultSet getAttributesForRelationships(
			int[] relationshipIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SLEXMMAttributeResultSet getAttributesForObjectVersion(
			int objectVersionId) {
		return getAttributesForObjectVersions(new int[] {objectVersionId});
	}

	@Override
	public SLEXMMAttributeResultSet getAttributesForObjectVersions(
			int[] objectVersionIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SLEXMMAttributeResultSet getAttributesForRelation(int relationId) {
		return getAttributesForRelations(new int[] {relationId});
	}

	@Override
	public SLEXMMAttributeResultSet getAttributesForRelations(int[] relationIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SLEXMMAttributeResultSet getAttributesForActivityInstance(
			int activityInstanceId) {
		return getAttributesForActivityInstances(new int[] {activityInstanceId});
	}

	@Override
	public SLEXMMAttributeResultSet getAttributesForActivityInstances(
			int[] activityInstanceIds) {
		// TODO Auto-generated method stub
		return null;
	}



	
}
