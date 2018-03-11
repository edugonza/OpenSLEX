/*
 * 
 */
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.processmining.openslex.metamodel.querygen.SLEXMMEdge;
import org.processmining.openslex.metamodel.querygen.SLEXMMStorageQueryGenerator;
import org.processmining.openslex.metamodel.querygen.SLEXMMTables;
import org.processmining.openslex.utils.ScriptRunner;

/**
 * The Class SLEXMMStorageMetaModelImpl.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class SLEXMMStorageMetaModelImpl implements SLEXMMStorageMetaModel {
	
	private SLEXMMStorageQueryGenerator slxmmstrqgen;
	//private static final String STORAGE_METAMODEL = PATH+File.separator+"metamodels.db";
	
	/** The Constant METAMODEL_ALIAS. */
	public static final String METAMODEL_ALIAS = "metamodeldb";
	
	private HTreeMap<String, AbstractDBElement> objectRepo;
	
	private HTreeMap<String, HashMap<AbstractAttDBElement, AbstractDBElementWithValue>> attsRepo;
	
	private HTreeMap<String, HashMap<String, AbstractAttDBElement>> attNamesRepo;
	
	/** The metamodel schema in. */
	private InputStream METAMODEL_SCHEMA_IN = SLEXMMStorage.class.getResourceAsStream("/org/processmining/openslex/resources/metamodel.sql");
	
	/** The Constant JOURNAL_MODE. */
	private static final String JOURNAL_MODE = "OFF";
	
	/** The Constant PRAGMA_SYNCHRONOUS_MODE. */
	private static final String PRAGMA_SYNCHRONOUS_MODE = "OFF";
	
	/** The metamodel_attached. */
	private boolean metamodel_attached;
	
	/** The auto commit on creation. */
	private boolean autoCommitOnCreation;
	
	/** The filename. */
	private String filename;
	
	/** The path. */
	private String path;
	
	private boolean diskCache;
	
	/** The connection. */
	private Connection connection;
	
	/** The statements. */
	private HashSet<Statement> statements;
	
	/**
	 * Instantiates a new SLEXMM storage meta model impl.
	 *
	 * @param path the path
	 * @param filename the filename
	 * @throws Exception the exception
	 */
	public SLEXMMStorageMetaModelImpl(String path, String filename) throws Exception {
		startConnection(path, filename, false);
	}
	
	public SLEXMMStorageMetaModelImpl(String path, String filename, boolean diskCache) throws Exception {
		startConnection(path, filename, diskCache);
	}
	
	private void startConnection(String path, String filename, boolean diskCache) throws Exception {
		this.slxmmstrqgen = null;
		this.metamodel_attached = false;
		this.autoCommitOnCreation = true;
		this.filename = null;
		this.path = null;
		this.diskCache = diskCache;
		this.connection = null;
		this.statements = new HashSet<>();
		
		AttsMapSerializer atMapSerializer =
				new AttsMapSerializer(this);
		AttNamesMapSerializer atNamesMapSerializer =
				new AttNamesMapSerializer(this);
		DBElementSerializer absDBobjectSerializer =
				new DBElementSerializer(this);
		DB db = null;
		
		if (diskCache) {
			db = DBMaker
					.tempFileDB()
					.concurrencyDisable()
					.allocateStartSize( 15 * 1024*1024*1024)  // 10GB
				    .allocateIncrement(512 * 1024*1024)       // 512MB
			        .fileMmapEnable()
			        .fileMmapPreclearDisable()
			        .make();
		} else {
			db = DBMaker
					.heapDB()
					.concurrencyDisable()
					.allocateStartSize( 15 * 1024*1024*1024)  // 10GB
				    .allocateIncrement(512 * 1024*1024)       // 512MB
			        .make();
		}
		HTreeMap<String, AbstractDBElement> map = db
		        .hashMap("objectRepo")
		        .keySerializer(Serializer.STRING)
		        .valueSerializer(absDBobjectSerializer)
		        .create();
		HTreeMap<String, HashMap<AbstractAttDBElement, AbstractDBElementWithValue>> mapAtts = db
				.hashMap("attsRepo")
				.keySerializer(Serializer.STRING)
				.valueSerializer(atMapSerializer)
				.create();
		HTreeMap<String, HashMap<String, AbstractAttDBElement>> mapAttNames = db
				.hashMap("attNamesRepo")
				.keySerializer(Serializer.STRING)
				.valueSerializer(atNamesMapSerializer)
				.create();
		this.objectRepo = map;
		this.attsRepo = mapAtts;
		this.attNamesRepo = mapAttNames;
		init();
		this.filename = filename;
		this.path = path;
		openMetaModelStorage(path,filename);
		
		slxmmstrqgen = new SLEXMMStorageQueryGenerator();
	}
	
	@Override
	public void reconnect() throws Exception {
		disconnect();
		startConnection(this.path, this.filename, this.diskCache);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		disconnect();
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorage#getFilename()
	 */
	@Override
	public String getFilename() {
		return filename;
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorage#getPath()
	 */
	@Override
	public String getPath() {
		return path;
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorage#getType()
	 */
	@Override
	public int getType() {
		return TYPE_METAMODEL;
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorage#setAutoCommit(boolean)
	 */
	@Override
	public void setAutoCommit(boolean autoCommit) {
		try {
			this.connection.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorage#commit()
	 */
	@Override
	public void commit() {
		try {
			this.connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the current working dir.
	 *
	 * @return the current working dir
	 */
	public static String getCurrentWorkingDir() {
		return System.getProperty("user.dir");
	}
	
	/**
	 * Open meta model storage.
	 *
	 * @param path the path
	 * @param filename the filename
	 * @return true, if successful
	 */
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
	
	/**
	 * Check schema.
	 *
	 * @param filename the filename
	 * @param alias the alias
	 * @param schemaIn the schema in
	 * @return true, if successful
	 */
	private boolean checkSchema(String filename, String alias, InputStream schemaIn) {
		boolean result = false;
		Connection connAux = null;
		try {
			connAux = DriverManager.getConnection("jdbc:sqlite:"+filename);
			ScriptRunner scriptRunner = new ScriptRunner(connAux, false, false);
			scriptRunner.setLogWriter(null);
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
	
	/**
	 * Creates the statement.
	 *
	 * @return the statement
	 */
	private Statement createStatement() {
		Statement stm = null;
		try {
			stm = connection.createStatement();
			statements.add(stm);
		} catch (SQLException e) {
			//e.printStackTrace();
		}
		return stm;
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorage#closeStatement(java.sql.Statement)
	 */
	@Override
	public void closeStatement(Statement statement) {
		try {
			if (statement != null) {
				statement.close();
				statements.remove(statement);
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorage#closeResultSet(java.sql.ResultSet)
	 */
	@Override
	public void closeResultSet(ResultSet rset) {
		try {
			if (rset != null) {
				rset.close();
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	
	/**
	 * Attach database file.
	 *
	 * @param filename the filename
	 * @param alias the alias
	 * @param schemaIn the schema in
	 * @return true, if successful
	 */
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
				statement = createStatement();
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
	
	/**
	 * Detach database.
	 *
	 * @param alias the alias
	 * @return true, if successful
	 */
	private boolean detachDatabase(String alias) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
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
	
	/**
	 * Sets the journal mode.
	 *
	 * @param mode the mode
	 * @return true, if successful
	 */
	private boolean setJournalMode(String mode) {
		if (mode != null) {
			Statement statement = null;
			boolean result = false;
			try {
				statement = createStatement();
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
	
	/**
	 * Sets the synchronous mode.
	 *
	 * @param mode the mode
	 * @return true, if successful
	 */
	private boolean setSynchronousMode(String mode) {
		if (mode != null) {
			Statement statement = null;
			boolean result = false;
			try {
				statement = createStatement();
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
	
	/**
	 * Query pragma.
	 *
	 * @param pragma the pragma
	 * @return the string
	 */
	public String queryPragma(String pragma) {
		String result = "";
		if (pragma != null) {
			Statement statement = null;
			try {
				statement = createStatement();
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
	
	/**
	 * Inits the.
	 *
	 * @throws ClassNotFoundException the class not found exception
	 */
	private void init() throws ClassNotFoundException {
		
		Class.forName("org.sqlite.JDBC");
		
		try {
			connection = DriverManager.getConnection("jdbc:sqlite::memory:");
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorage#disconnect()
	 */
	@Override
	public void disconnect() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			if (objectRepo != null) {
				objectRepo.close();
				objectRepo = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if (attsRepo != null) {
				attsRepo.close();
				attsRepo = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorage#abort()
	 */
	@Override
	public void abort() {
		if (connection != null) {
			Iterator<Statement> it = statements.iterator();
			while (it.hasNext()) {
				Statement stm = it.next();
				try {
					stm.cancel();
				} catch (Exception e) {
					e.printStackTrace();
				}
				it.remove();
			}
			disconnect();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorage#setAutoCommitOnCreation(boolean)
	 */
	@Override
	public void setAutoCommitOnCreation(boolean flag) {
		this.autoCommitOnCreation = flag;
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorage#isAutoCommitOnCreationEnabled()
	 */
	@Override
	public boolean isAutoCommitOnCreationEnabled() {
		return this.autoCommitOnCreation;
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#createEvent(int, int, java.lang.String, java.lang.String, long)
	 */
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
	
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#createAttributeValue(int, int, java.lang.String, java.lang.String)
 	*/
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#createDataModel(java.lang.String)
	 */
	@Override
	public SLEXMMDataModel createDataModel(String name) {
		SLEXMMDataModel dm = new SLEXMMDataModel(this);
		dm.setName(name);
		if (isAutoCommitOnCreationEnabled()) {
			dm.commit();
		}
		return dm;
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#createClass(int, java.lang.String)
	 */
	@Override
	public SLEXMMClass createClass(int data_modelId, String name) {
		SLEXMMClass cl = new SLEXMMClass(this, name, data_modelId);
		if (isAutoCommitOnCreationEnabled()) {
			cl.commit();
		}
		return cl;
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#createAttribute(int, java.lang.String)
	 */
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#createRelationship(java.lang.String, int, int)
	 */
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
	
	/**
	 * Gets the last inserted row id.
	 *
	 * @param stmnt the stmnt
	 * @return the last inserted row id
	 */
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#insert(org.processmining.openslex.metamodel.SLEXMMEvent)
	 */
	@Override
	public synchronized boolean insert(SLEXMMEvent ev) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#update(org.processmining.openslex.metamodel.SLEXMMEvent)
	 */
	@Override
	public boolean update(SLEXMMEvent ev) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#insert(org.processmining.openslex.metamodel.SLEXMMAttribute)
	 */
	@Override
	public synchronized boolean insert(SLEXMMAttribute at) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#update(org.processmining.openslex.metamodel.SLEXMMAttribute)
	 */
	@Override
	public boolean update(SLEXMMAttribute at) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#insert(org.processmining.openslex.metamodel.SLEXMMEventAttribute)
	 */
	@Override
	public synchronized boolean insert(SLEXMMEventAttribute at) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#update(org.processmining.openslex.metamodel.SLEXMMEventAttribute)
	 */
	@Override
	public boolean update(SLEXMMEventAttribute at) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#insert(org.processmining.openslex.metamodel.SLEXMMRelationship)
	 */
	@Override
	public synchronized boolean insert(SLEXMMRelationship k) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#update(org.processmining.openslex.metamodel.SLEXMMRelationship)
	 */
	@Override
	public boolean update(SLEXMMRelationship k) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#insert(org.processmining.openslex.metamodel.SLEXMMAttributeValue)
	 */
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#update(org.processmining.openslex.metamodel.SLEXMMAttributeValue)
	 */
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#insert(org.processmining.openslex.metamodel.SLEXMMEventAttributeValue)
	 */
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#update(org.processmining.openslex.metamodel.SLEXMMEventAttributeValue)
	 */
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#insert(org.processmining.openslex.metamodel.SLEXMMClass)
	 */
	@Override
	public synchronized boolean insert(SLEXMMClass cl) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#update(org.processmining.openslex.metamodel.SLEXMMClass)
	 */
	@Override
	public boolean update(SLEXMMClass cl) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#insert(org.processmining.openslex.metamodel.SLEXMMProcess)
	 */
	@Override
	public synchronized boolean insert(SLEXMMProcess p) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#update(org.processmining.openslex.metamodel.SLEXMMProcess)
	 */
	@Override
	public boolean update(SLEXMMProcess p) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#insert(org.processmining.openslex.metamodel.SLEXMMDataModel)
	 */
	@Override
	public synchronized boolean insert(SLEXMMDataModel dm) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#update(org.processmining.openslex.metamodel.SLEXMMDataModel)
	 */
	@Override
	public boolean update(SLEXMMDataModel dm) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#insert(org.processmining.openslex.metamodel.SLEXMMCase)
	 */
	@Override
	public synchronized boolean insert(SLEXMMCase t) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
			statement.setQueryTimeout(30);
			statement.execute("INSERT INTO "+METAMODEL_ALIAS+".'case' (name) VALUES ('"+t.getName()+"')");
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#update(org.processmining.openslex.metamodel.SLEXMMCase)
	 */
	@Override
	public boolean update(SLEXMMCase t) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
			statement.setQueryTimeout(30);
			statement.execute("UPDATE "+METAMODEL_ALIAS+".'case' SET name = '"+t.getName()+"' WHERE id = '"+t.getId()+"'");
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getEventsOfCase(org.processmining.openslex.metamodel.SLEXMMCase)
	 */
	@Override
	public SLEXMMEventResultSet getEventsOfCase(SLEXMMCase c) {
		return getEventsOfCase(new int[] {c.getId()});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getEventsOfCase(int)
	 */
	@Override
	public SLEXMMEventResultSet getEventsOfCase(int[] is) {
		return (SLEXMMEventResultSet) getResultSetFor(SLEXMMEventResultSet.class,
				SLEXMMTables.T_EVENT, SLEXMMTables.T_CASE, is);
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getAttributeValuesForEvent(org.processmining.openslex.metamodel.SLEXMMEvent)
	 */
	@Override
	public HashMap<SLEXMMEventAttribute, SLEXMMEventAttributeValue> getAttributeValuesForEvent(
			SLEXMMEvent ev) {
		return getAttributeValuesForEvent(ev.getId());
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getAttributeValuesForEvent(int)
	 */
	@Override
	public HashMap<SLEXMMEventAttribute, SLEXMMEventAttributeValue> getAttributeValuesForEvent(
			int evId) {
		HashMap<SLEXMMEventAttribute, SLEXMMEventAttributeValue> attributeValues = null;
		Statement statement = null;
		try {
			statement = createStatement();
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getDataModels()
	 */
	@Override
	public SLEXMMDataModelResultSet getDataModels() {
		SLEXMMDataModelResultSet dmrset = null;
		Statement statement = null;
		try {
			statement = createStatement();
			ResultSet rset = statement.executeQuery("SELECT * FROM "+METAMODEL_ALIAS+".datamodel");
			dmrset = new SLEXMMDataModelResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return dmrset;
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getListAttributesForClass(org.processmining.openslex.metamodel.SLEXMMClass)
	 */
	@Override
	public List<SLEXMMAttribute> getListAttributesForClass(SLEXMMClass cl) {
		return getListAttributesForClass(cl.getId());
	}
	

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getListAttributesForClass(int)
	 */
	@Override
	public List<SLEXMMAttribute> getListAttributesForClass(int clId) {
		SLEXMMAttributeResultSet arset = (SLEXMMAttributeResultSet) getResultSetFor(SLEXMMAttributeResultSet.class,
				SLEXMMTables.T_ATTRIBUTE_NAME, SLEXMMTables.T_CLASS, new int[] { clId });
		
		List<SLEXMMAttribute> atList = new Vector<>();
		SLEXMMAttribute at = null;
		
		while ((at = arset.getNext()) != null) {
			atList.add(at);
		}
		
		return atList;
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationshipsForClass(org.processmining.openslex.metamodel.SLEXMMClass)
	 */
	@Override
	public List<SLEXMMRelationship> getRelationshipsForClass(SLEXMMClass cl) {
		
		SLEXMMRelationshipResultSet rsset = (SLEXMMRelationshipResultSet) getResultSetFor(SLEXMMRelationshipResultSet.class,
				SLEXMMTables.T_RELATIONSHIP, SLEXMMTables.T_CLASS, new int[] { cl.getId() });
		
		List<SLEXMMRelationship> kList = new Vector<>();
		SLEXMMRelationship rsh = null;
		
		while ((rsh = rsset.getNext()) != null) {
			kList.add(rsh);
		}
		
		return kList;
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getNumberEventsOfCase(org.processmining.openslex.metamodel.SLEXMMCase)
	 */
	public int getNumberEventsOfCase(SLEXMMCase t) {
		// TEST
		int events = 0;
		Statement statement = null;
		try {
			statement = createStatement();
			ResultSet rset = statement.executeQuery("SELECT count(E.id) FROM "
					+METAMODEL_ALIAS+".event AS E, "
					+METAMODEL_ALIAS+".activity_instance AS AI, "
					+METAMODEL_ALIAS+".activity_instance_to_case AS AITC "
					+" WHERE E.activity_instance_id = AI.id "
					+" AND AI.id = AITC.activity_instance_id "
					+" AND AITC.case_id='"+t.getId()+"'");
			if (rset.next()) {
				events = rset.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return events;
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#createCase(java.lang.String)
	 */
	@Override
	public SLEXMMCase createCase(String name) {
		SLEXMMCase t = new SLEXMMCase(this);
		t.setName(name);
		if (isAutoCommitOnCreationEnabled()) {
			t.commit();
		}
		return t;
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#cloneCase(org.processmining.openslex.metamodel.SLEXMMCase)
	 */
	@Override
	public SLEXMMCase cloneCase(SLEXMMCase t) {
		SLEXMMCase ct = this.createCase(t.getName());
		ct.commit();
		Statement statement = null;
		try {
			statement = createStatement();
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

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#addActivityInstanceToCase(int, int)
	 */
	@Override
	public boolean addActivityInstanceToCase(int traceId, int activityInstanceId) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#addActivityInstanceToCase(org.processmining.openslex.metamodel.SLEXMMCase, org.processmining.openslex.metamodel.SLEXMMActivityInstance)
	 */
	@Override
	public boolean addActivityInstanceToCase(SLEXMMCase t, SLEXMMActivityInstance e) {
		return addActivityInstanceToCase(t.getId(),e.getId());
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getMaxCaseId()
	 */
	@Override
	public int getMaxCaseId() {
		int maxId = 0;
		
		Statement statement = null;
		try {
			statement = createStatement();
			ResultSet rset = statement.executeQuery("SELECT IFNULL(MAX(id),0) + 1 FROM "+METAMODEL_ALIAS+".case");
			rset.next();
			maxId = rset.getInt(1);
			rset.close();
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
				
		return maxId;
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#createEventAttribute(java.lang.String)
	 */
	@Override
	public SLEXMMEventAttribute createEventAttribute(String name) {
		SLEXMMEventAttribute at = new SLEXMMEventAttribute(this);
		at.setName(name);
		if (isAutoCommitOnCreationEnabled()) {
			at.commit();
		}
		return at;
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#createEventAttributeValue(int, int, java.lang.String, java.lang.String)
	 */
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

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#createRelation(int, int, int, long, long)
	 */
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#insert(org.processmining.openslex.metamodel.SLEXMMRelation)
	 */
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

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#update(org.processmining.openslex.metamodel.SLEXMMRelation)
	 */
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

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#createObject(int)
	 */
	@Override
	public SLEXMMObject createObject(int classId) {
		SLEXMMObject obj = new SLEXMMObject(this);
		obj.setClassId(classId);
		if (isAutoCommitOnCreationEnabled()) {
			obj.commit();
		}
		return obj;
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#insert(org.processmining.openslex.metamodel.SLEXMMObject)
	 */
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

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#update(org.processmining.openslex.metamodel.SLEXMMObject)
	 */
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

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#createObjectVersion(int, long, long)
	 */
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#insert(org.processmining.openslex.metamodel.SLEXMMObjectVersion)
	 */
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

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#update(org.processmining.openslex.metamodel.SLEXMMObjectVersion)
	 */
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

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getAttributeValuesForObjectVersion(org.processmining.openslex.metamodel.SLEXMMObjectVersion)
	 */
	@Override
	public HashMap<SLEXMMAttribute, SLEXMMAttributeValue> getAttributeValuesForObjectVersion(
			SLEXMMObjectVersion objv) {
		return getAttributeValuesForObjectVersion(objv.getId());
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getAttributeValuesForObjectVersion(int)
	 */
	@Override
	public HashMap<SLEXMMAttribute, SLEXMMAttributeValue> getAttributeValuesForObjectVersion(
			int objvId) {
		HashMap<SLEXMMAttribute, SLEXMMAttributeValue> attributeValues = null;
		Statement statement = null;
		try {
			statement = createStatement();
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
	

	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectVersions()
	 */
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersions() {
		SLEXMMObjectVersionResultSet erset = null;
		Statement statement = null;
		try {
			statement = createStatement();
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationsForSourceObject(org.processmining.openslex.metamodel.SLEXMMObject)
	 */
	@Override
	public SLEXMMRelationResultSet getRelationsForSourceObject(SLEXMMObject obj) {
		return getRelationsForSourceObject(obj.getId());
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationsForSourceObject(int)
	 */
	@Override
	public SLEXMMRelationResultSet getRelationsForSourceObject(int objId) {
		SLEXMMRelationResultSet erset = null;
		Statement statement = null;
		try {
			statement = createStatement();
			ResultSet rset = statement.executeQuery("SELECT OBJV.object_id as originIdQuery, RL.* FROM "
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationsForTargetObject(org.processmining.openslex.metamodel.SLEXMMObject)
	 */
	@Override
	public SLEXMMRelationResultSet getRelationsForTargetObject(SLEXMMObject obj) {
		return getRelationsForTargetObject(obj.getId());
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationsForTargetObject(int)
	 */
	@Override
	public SLEXMMRelationResultSet getRelationsForTargetObject(int objId) {
		SLEXMMRelationResultSet erset = null;
		Statement statement = null;
		try {
			statement = createStatement();
			ResultSet rset = statement.executeQuery("SELECT OBJV.object_id as originIdQuery, RL.* FROM "
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationsForSourceObjectOrdered(org.processmining.openslex.metamodel.SLEXMMObject)
	 */
	@Override
	public SLEXMMRelationResultSet getRelationsForSourceObjectOrdered(SLEXMMObject obj) {
		return getRelationsForSourceObjectOrdered(obj.getId());
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationsForSourceObjectOrdered(int)
	 */
	@Override
	public SLEXMMRelationResultSet getRelationsForSourceObjectOrdered(int objId) { 
		SLEXMMRelationResultSet erset = null;
		Statement statement = null;
		try {
			statement = createStatement();
			ResultSet rset = statement.executeQuery("SELECT OBJV.object_id as originIdQuery, REL.* FROM "
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationsForTargetObjectOrdered(org.processmining.openslex.metamodel.SLEXMMObject)
	 */
	@Override
	public SLEXMMRelationResultSet getRelationsForTargetObjectOrdered(SLEXMMObject obj) {
		return getRelationsForTargetObject(obj.getId());
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationsForTargetObjectOrdered(int)
	 */
	@Override
	public SLEXMMRelationResultSet getRelationsForTargetObjectOrdered(int objId) { 
		SLEXMMRelationResultSet erset = null;
		Statement statement = null;
		try {
			statement = createStatement();
			ResultSet rset = statement.executeQuery("SELECT OBJV.object_id as originIdQuery, REL.* FROM "
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

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjects()
	 */
	@Override
	public SLEXMMObjectResultSet getObjects() {
		SLEXMMObjectResultSet erset = null;
		Statement statement = null;
		try {
			statement = createStatement();
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
	


	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getEvents()
	 */
	@Override
	public SLEXMMEventResultSet getEvents() {
		SLEXMMEventResultSet erset = null;
		Statement statement = null;
		try {
			statement = createStatement();
			ResultSet rset = statement.executeQuery("SELECT * FROM "
					+METAMODEL_ALIAS+".event ");
			erset = new SLEXMMEventResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset; 
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getEventsOrdered()
	 */
	@Override
	public SLEXMMEventResultSet getEventsOrdered() {
		SLEXMMEventResultSet erset = null;
		Statement statement = null;
		try {
			statement = createStatement();
			ResultSet rset = statement.executeQuery("SELECT * FROM "
					+METAMODEL_ALIAS+".event ORDER BY ordering ASC ");
			erset = new SLEXMMEventResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset; 
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getCases()
	 */
	@Override
	public SLEXMMCaseResultSet getCases() {
		SLEXMMCaseResultSet erset = null;
		Statement statement = null;
		try {
			statement = createStatement();
			ResultSet rset = statement.executeQuery("SELECT * FROM "
					+METAMODEL_ALIAS+".'case' ");
			erset = new SLEXMMCaseResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset;
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#insert(org.processmining.openslex.metamodel.SLEXMMActivity)
	 */
	@Override
	public boolean insert(SLEXMMActivity cl) {
		PreparedStatement statement = null;
		boolean result = false;
		try {
			statement = connection.prepareStatement("INSERT INTO "+METAMODEL_ALIAS
					+".activity (name) VALUES (?)");
			statement.setQueryTimeout(30);
			statement.setString(1, cl.getName());
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

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#update(org.processmining.openslex.metamodel.SLEXMMActivity)
	 */
	@Override
	public boolean update(SLEXMMActivity cl) {
		PreparedStatement statement = null;
		boolean result = false;
		try {
			statement = connection.prepareStatement("UPDATE "+METAMODEL_ALIAS
					+".activity SET name = ? WHERE id = ? ");
			statement.setQueryTimeout(30);
			statement.setString(1, cl.getName());
			statement.setInt(2, cl.getId());
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
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#insert(org.processmining.openslex.metamodel.SLEXMMActivityInstance)
	 */
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

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#update(org.processmining.openslex.metamodel.SLEXMMActivityInstance)
	 */
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

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#createProcess(java.lang.String)
	 */
	@Override
	public SLEXMMProcess createProcess(String name) {
		SLEXMMProcess p = new SLEXMMProcess(this);
		p.setName(name);
		if (isAutoCommitOnCreationEnabled()) {
			p.commit();
		}
		return p;
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#createActivity(int, java.lang.String)
	 */
	@Override
	public SLEXMMActivity createActivity(String name) {
		SLEXMMActivity act = new SLEXMMActivity(this,name);
		if (isAutoCommitOnCreationEnabled()) {
			act.commit();
		}
		return act;
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#createActivityInstance(org.processmining.openslex.metamodel.SLEXMMActivity)
	 */
	@Override
	public SLEXMMActivityInstance createActivityInstance(SLEXMMActivity act) {
		SLEXMMActivityInstance actIn = new SLEXMMActivityInstance(this);
		actIn.setActivityId(act.getId());
		if (isAutoCommitOnCreationEnabled()) {
			actIn.commit();
		}
		return actIn;
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getEventForId(int)
	 */
	@Override
	public SLEXMMEvent getEventForId(int evId) {
		SLEXMMEvent e = null;
		
		SLEXMMEventResultSet erset = null;
		Statement statement = null;
		try {
			statement = createStatement();
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

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivities()
	 */
	@Override
	public SLEXMMActivityResultSet getActivities() {
		SLEXMMActivityResultSet actrset = null;
		Statement statement = null;
		
		try {
			statement = createStatement();
			ResultSet rset = statement.executeQuery("SELECT * FROM "+METAMODEL_ALIAS+".activity");
			actrset = new SLEXMMActivityResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return actrset;
	}
	
	@Override
	public SLEXMMClassResultSet getClasses() {
		SLEXMMClassResultSet actrset = null;
		Statement statement = null;
		
		try {
			statement = createStatement();
			ResultSet rset = statement.executeQuery("SELECT * FROM "+METAMODEL_ALIAS+".class");
			actrset = new SLEXMMClassResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return actrset;
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getVersionsRelatedToObjectVersion(org.processmining.openslex.metamodel.SLEXMMObjectVersion)
	 */
	@Override
	public SLEXMMObjectVersionResultSet getVersionsRelatedToObjectVersion(
			SLEXMMObjectVersion ob) {
		
		SLEXMMObjectVersionResultSet erset = null;
		Statement statement = null;
		String query = "";
		try {
			statement = createStatement();
			query = "SELECT "+ob.getId()+" as originIdQuery, OBJV.* FROM "
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
	public SLEXMMObjectVersionResultSet getVersionsRelatedToObjectVersions(int[] verIds) {
		
		SLEXMMObjectVersionResultSet erset = null;
		Statement statement = null;
		String idsStr = buildStringFromArray(verIds);
		String query = "";
		try {
			statement = createStatement();
			query = "SELECT OBJV1.id as originIdQuery, OBJV2.* FROM "
					+METAMODEL_ALIAS+".object_version AS OBJV1, "
					+METAMODEL_ALIAS+".object_version AS OBJV2, "
					+METAMODEL_ALIAS+".relation AS RL "
					+" WHERE (( RL.source_object_version_id = OBJV1.id AND OBJV2.id = RL.target_object_version_id ) "
					+" OR ( RL.target_object_version_id = OBJV1.id AND OBJV2.id = RL.source_object_version_id )) "
					+" AND OBJV1.id IN ("+idsStr+")";
			ResultSet rset = statement.executeQuery(query);
			erset = new SLEXMMObjectVersionResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(query);
			closeStatement(statement);
		}
		
		return erset; 
		 
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getEventsForCase(int)
	 */
	@Override
	public SLEXMMEventResultSet getEventsForCase(int caseId) {
		return getEventsForCases(new int[] {caseId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getEventsForCases(int[])
	 */
	@Override
	public SLEXMMEventResultSet getEventsForCases(int[] ids) {
		return (SLEXMMEventResultSet) getResultSetFor(SLEXMMEventResultSet.class, SLEXMMTables.T_EVENT,
				SLEXMMTables.T_CASE, ids);
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getEventsForActivity(int)
	 */
	@Override
	public SLEXMMEventResultSet getEventsForActivity(int activityId) {
		return getEventsForActivities(new int[] {activityId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getEventsForActivities(int[])
	 */
	@Override
	public SLEXMMEventResultSet getEventsForActivities(int[] ids) {
		return (SLEXMMEventResultSet) getResultSetFor(SLEXMMEventResultSet.class, SLEXMMTables.T_EVENT,
				SLEXMMTables.T_ACTIVITY, ids);
	}



	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectPerId(int)
	 */
	@Override
	public SLEXMMObject getObjectPerId(int objectId) {
		SLEXMMObject ob = null;
			
		SLEXMMObjectResultSet orset = null;
		Statement statement = null;
		try {
			statement = createStatement();
			String query = "SELECT OBJ.id as originIdQuery, OBJ.* FROM "
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

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getEventsForObjectVersion(int)
	 */
	@Override
	public SLEXMMEventResultSet getEventsForObjectVersion(int objvId) { 
		return getEventsForObjectVersions(new int[] {objvId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getEventsForObjectVersions(int[])
	 */
	@Override
	public SLEXMMEventResultSet getEventsForObjectVersions(int[] is) {
		return (SLEXMMEventResultSet) getResultSetFor(SLEXMMEventResultSet.class,
				SLEXMMTables.T_EVENT,
				SLEXMMTables.T_OBJECT_VERSION, is); 
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#addEventToObjectVersion(org.processmining.openslex.metamodel.SLEXMMObjectVersion, org.processmining.openslex.metamodel.SLEXMMEvent, java.lang.String)
	 */
	@Override
	public boolean addEventToObjectVersion(SLEXMMObjectVersion ov, 
			SLEXMMEvent ev, String label) {
		return addEventToObjectVersion(ov.getId(),ev.getId(),label);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#addEventToObjectVersion(int, int, java.lang.String)
	 */
	@Override
	public boolean addEventToObjectVersion(int ovId, int evId, String label) { 
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
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

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelations()
	 */
	@Override
	public SLEXMMRelationResultSet getRelations() {
		SLEXMMRelationResultSet erset = null;
		Statement statement = null;
		try {
			statement = createStatement();
			ResultSet rset = statement.executeQuery("SELECT RL.* FROM "
					+METAMODEL_ALIAS+".relation RL ");
			erset = new SLEXMMRelationResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return erset; 
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationships()
	 */
	@Override
	public List<SLEXMMRelationship> getRelationships() {
		List<SLEXMMRelationship> kList = new Vector<>();
		Statement statement = null;
		
		try {
			statement = createStatement();
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
	public SLEXMMRelationshipResultSet getRelationshipsRS() {
		SLEXMMRelationshipResultSet rsrset = null;
		Statement statement = null;
		try {
			statement = createStatement();
			ResultSet rset = statement.executeQuery("SELECT * FROM "+METAMODEL_ALIAS+".relationship ");
			rsrset = new SLEXMMRelationshipResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return rsrset;
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivityInstances()
	 */
	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstances() {
		SLEXMMActivityInstanceResultSet airset = null;
		Statement statement = null;
		try {
			statement = createStatement();
			ResultSet rset = statement.executeQuery("SELECT AI.* FROM "
					+METAMODEL_ALIAS+".activity_instance AI ");
			airset = new SLEXMMActivityInstanceResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return airset;
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getAttributes()
	 */
	@Override
	public SLEXMMAttributeResultSet getAttributes() {
		SLEXMMAttributeResultSet arset = null;
		Statement statement = null;
		try {
			statement = createStatement();
			ResultSet rset = statement.executeQuery("SELECT A.* FROM "
					+METAMODEL_ALIAS+".attribute_name A ");
			arset = new SLEXMMAttributeResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return arset;
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#executeSQL(java.lang.String)
	 */
	@Override
	public SLEXMMSQLResultSet executeSQL(String query) throws Exception {
		SLEXMMSQLResultSet sqlrset = null;
		Statement statement = null;
		try {
			statement = createStatement();
			ResultSet rset = statement.executeQuery(query);
			sqlrset = new SLEXMMSQLResultSet(this, rset);
		} catch (Exception e) {
			closeStatement(statement);
			throw e;
		}
		
		return sqlrset;
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getEventAttributes()
	 */
	@Override
	public SLEXMMEventAttributeResultSet getEventAttributes() {
		SLEXMMEventAttributeResultSet arset = null;
		Statement statement = null;
		try {
			statement = createStatement();
			ResultSet rset = statement.executeQuery("SELECT EA.* FROM "
					+METAMODEL_ALIAS+".event_attribute_name EA ");
			arset = new SLEXMMEventAttributeResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return arset;
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectsForCase(int)
	 */
	@Override
	public SLEXMMObjectResultSet getObjectsForCase(int caseId) {
		return getObjectsForCases(new int[] {caseId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectsForCases(int[])
	 */
	@Override
	public SLEXMMObjectResultSet getObjectsForCases(int[] is) {
		return (SLEXMMObjectResultSet) getResultSetFor(SLEXMMObjectResultSet.class,
				SLEXMMTables.T_OBJECT,
				SLEXMMTables.T_CASE, is); 
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectsForObjectVersion(int)
	 */
	@Override
	public SLEXMMObjectResultSet getObjectsForObjectVersion(int objectVersionId) {
		return getObjectsForObjectVersions(new int[] {objectVersionId});
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectsForObjectVersions(int[])
	 */
	@Override
	public SLEXMMObjectResultSet getObjectsForObjectVersions(
			int[] is) {
		return (SLEXMMObjectResultSet) getResultSetFor(SLEXMMObjectResultSet.class,
				SLEXMMTables.T_OBJECT,
				SLEXMMTables.T_OBJECT_VERSION, is);
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectsForEvent(int)
	 */
	@Override
	public SLEXMMObjectResultSet getObjectsForEvent(int eventId) { 
		return getObjectsForEvents(new int[] {eventId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectsForEvents(int[])
	 */
	@Override
	public SLEXMMObjectResultSet getObjectsForEvents(int[] is) {
		return (SLEXMMObjectResultSet) getResultSetFor(SLEXMMObjectResultSet.class,
				SLEXMMTables.T_OBJECT,
				SLEXMMTables.T_EVENT, is); 
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectsForClass(int)
	 */
	@Override
	public SLEXMMObjectResultSet getObjectsForClass(int classId) {
		return getObjectsForClasses(new int[] {classId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectsForClasses(int[])
	 */
	@Override
	public SLEXMMObjectResultSet getObjectsForClasses(int[] is) {
		return (SLEXMMObjectResultSet) getResultSetFor(SLEXMMObjectResultSet.class,
				SLEXMMTables.T_OBJECT,
				SLEXMMTables.T_CLASS, is); 
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectsForActivity(int)
	 */
	@Override
	public SLEXMMObjectResultSet getObjectsForActivity(int activityId) {
		return getObjectsForActivities(new int[] {activityId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectsForActivities(int[])
	 */
	@Override
	public SLEXMMObjectResultSet getObjectsForActivities(int[] is) {
		return (SLEXMMObjectResultSet) getResultSetFor(SLEXMMObjectResultSet.class,
				SLEXMMTables.T_OBJECT,
				SLEXMMTables.T_ACTIVITY, is); 
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectsForActivityInstance(int)
	 */
	@Override
	public SLEXMMObjectResultSet getObjectsForActivityInstance(
			int activityInstanceId) {
		return getObjectsForActivityInstances(new int[] {activityInstanceId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectsForActivityInstances(int[])
	 */
	@Override
	public SLEXMMObjectResultSet getObjectsForActivityInstances(
			int[] is) {
		return (SLEXMMObjectResultSet) getResultSetFor(SLEXMMObjectResultSet.class,
				SLEXMMTables.T_OBJECT,
				SLEXMMTables.T_ACTIVITY_INSTANCE, is); 
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectsForRelation(int)
	 */
	@Override
	public SLEXMMObjectResultSet getObjectsForRelation(int relationId) {
		return getObjectsForRelations(new int[] {relationId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectsForRelations(int[])
	 */
	@Override
	public SLEXMMObjectResultSet getObjectsForRelations(int[] is) {
		return (SLEXMMObjectResultSet) getResultSetFor(SLEXMMObjectResultSet.class,
				SLEXMMTables.T_OBJECT,
				SLEXMMTables.T_RELATION, is); 
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectsForRelationship(int)
	 */
	@Override
	public SLEXMMObjectResultSet getObjectsForRelationship(int relationshipId) {
		return getObjectsForRelationships(new int[] {relationshipId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectsForRelationships(int[])
	 */
	@Override
	public SLEXMMObjectResultSet getObjectsForRelationships(int[] is) {
		return (SLEXMMObjectResultSet) getResultSetFor(SLEXMMObjectResultSet.class,
				SLEXMMTables.T_OBJECT,
				SLEXMMTables.T_RELATIONSHIP, is);
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectsForAttribute(int)
	 */
	@Override
	public SLEXMMObjectResultSet getObjectsForAttribute(int attributeId) {
		return getObjectsForAttributes(new int[] {attributeId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectsForAttributes(int[])
	 */
	@Override
	public SLEXMMObjectResultSet getObjectsForAttributes(int[] is) {
		return (SLEXMMObjectResultSet) getResultSetFor(SLEXMMObjectResultSet.class,
				SLEXMMTables.T_OBJECT,
				SLEXMMTables.T_ATTRIBUTE_NAME, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getCasesForObject(int)
	 */
	@Override
	public SLEXMMCaseResultSet getCasesForObject(int objectId) {
		return getCasesForObjects(new int[] {objectId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getCasesForObjects(int[])
	 */
	@Override
	public SLEXMMCaseResultSet getCasesForObjects(int[] is) {
		return (SLEXMMCaseResultSet) getResultSetFor(SLEXMMCaseResultSet.class,
				SLEXMMTables.T_CASE,
				SLEXMMTables.T_OBJECT, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getCasesForEvent(int)
	 */
	@Override
	public SLEXMMCaseResultSet getCasesForEvent(int eventId) {
		return getCasesForEvents(new int[] {eventId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getCasesForEvents(int[])
	 */
	@Override
	public SLEXMMCaseResultSet getCasesForEvents(int[] is) {
		return (SLEXMMCaseResultSet) getResultSetFor(SLEXMMCaseResultSet.class,
				SLEXMMTables.T_CASE,
				SLEXMMTables.T_EVENT, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getCasesForActivity(int)
	 */
	@Override
	public SLEXMMCaseResultSet getCasesForActivity(int activityId) {
		return getCasesForActivities(new int[] {activityId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getCasesForActivities(int[])
	 */
	@Override
	public SLEXMMCaseResultSet getCasesForActivities(int[] is) {
		return (SLEXMMCaseResultSet) getResultSetFor(SLEXMMCaseResultSet.class,
				SLEXMMTables.T_CASE,
				SLEXMMTables.T_ACTIVITY, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getCasesForClass(int)
	 */
	@Override
	public SLEXMMCaseResultSet getCasesForClass(int classId) {
		return getCasesForClasses(new int[] {classId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getCasesForClasses(int[])
	 */
	@Override
	public SLEXMMCaseResultSet getCasesForClasses(int[] is) {
		return (SLEXMMCaseResultSet) getResultSetFor(SLEXMMCaseResultSet.class,
				SLEXMMTables.T_CASE,
				SLEXMMTables.T_CLASS, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getCasesForRelationship(int)
	 */
	@Override
	public SLEXMMCaseResultSet getCasesForRelationship(int relationshipId) {
		return getCasesForRelationships(new int[] {relationshipId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getCasesForRelationships(int[])
	 */
	@Override
	public SLEXMMCaseResultSet getCasesForRelationships(int[] is) {
		return (SLEXMMCaseResultSet) getResultSetFor(SLEXMMCaseResultSet.class,
				SLEXMMTables.T_CASE,
				SLEXMMTables.T_RELATIONSHIP, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getCasesForObjectVersion(int)
	 */
	@Override
	public SLEXMMCaseResultSet getCasesForObjectVersion(int objectVersionId) {
		return getCasesForObjectVersions(new int[] {objectVersionId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getCasesForObjectVersions(int[])
	 */
	@Override
	public SLEXMMCaseResultSet getCasesForObjectVersions(int[] is) {
		return (SLEXMMCaseResultSet) getResultSetFor(SLEXMMCaseResultSet.class,
				SLEXMMTables.T_CASE,
				SLEXMMTables.T_OBJECT_VERSION, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getCasesForRelation(int)
	 */
	@Override
	public SLEXMMCaseResultSet getCasesForRelation(int relationId) {
		return getCasesForRelations(new int[] {relationId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getCasesForRelations(int[])
	 */
	@Override
	public SLEXMMCaseResultSet getCasesForRelations(int[] is) {
		return (SLEXMMCaseResultSet) getResultSetFor(SLEXMMCaseResultSet.class,
				SLEXMMTables.T_CASE,
				SLEXMMTables.T_RELATION, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getCasesForActivityInstance(int)
	 */
	@Override
	public SLEXMMCaseResultSet getCasesForActivityInstance(
			int activityInstanceId) {
		return getCasesForActivityInstances(new int[] {activityInstanceId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getCasesForActivityInstances(int[])
	 */
	@Override
	public SLEXMMCaseResultSet getCasesForActivityInstances(
			int[] is) {
		return (SLEXMMCaseResultSet) getResultSetFor(SLEXMMCaseResultSet.class,
				SLEXMMTables.T_CASE,
				SLEXMMTables.T_ACTIVITY_INSTANCE, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getCasesForAttribute(int)
	 */
	@Override
	public SLEXMMCaseResultSet getCasesForAttribute(int attributeId) {
		return getCasesForAttributes(new int[] {attributeId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getCasesForAttributes(int[])
	 */
	@Override
	public SLEXMMCaseResultSet getCasesForAttributes(int[] is) {
		return (SLEXMMCaseResultSet) getResultSetFor(SLEXMMCaseResultSet.class,
				SLEXMMTables.T_CASE,
				SLEXMMTables.T_ATTRIBUTE_NAME, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getEventsForObject(int)
	 */
	@Override
	public SLEXMMEventResultSet getEventsForObject(int objectId) {
		return getEventsForObjects(new int[] {objectId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getEventsForObjects(int[])
	 */
	@Override
	public SLEXMMEventResultSet getEventsForObjects(int[] is) {
		return (SLEXMMEventResultSet) getResultSetFor(SLEXMMEventResultSet.class,
				SLEXMMTables.T_EVENT,
				SLEXMMTables.T_OBJECT, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getEventsForClass(int)
	 */
	@Override
	public SLEXMMEventResultSet getEventsForClass(int classId) {
		return getEventsForClasses(new int[] {classId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getEventsForClasses(int[])
	 */
	@Override
	public SLEXMMEventResultSet getEventsForClasses(int[] is) {
		return (SLEXMMEventResultSet) getResultSetFor(SLEXMMEventResultSet.class,
				SLEXMMTables.T_EVENT,
				SLEXMMTables.T_CLASS, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getEventsForRelationship(int)
	 */
	@Override
	public SLEXMMEventResultSet getEventsForRelationship(int relationshipId) {
		return getEventsForRelationships(new int[] {relationshipId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getEventsForRelationships(int[])
	 */
	@Override
	public SLEXMMEventResultSet getEventsForRelationships(int[] is) {
		return (SLEXMMEventResultSet) getResultSetFor(SLEXMMEventResultSet.class,
				SLEXMMTables.T_EVENT,
				SLEXMMTables.T_RELATIONSHIP, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getEventsForRelation(int)
	 */
	@Override
	public SLEXMMEventResultSet getEventsForRelation(int relationId) {
		return getEventsForRelations(new int[] {relationId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getEventsForRelations(int[])
	 */
	@Override
	public SLEXMMEventResultSet getEventsForRelations(int[] is) {
		return (SLEXMMEventResultSet) getResultSetFor(SLEXMMEventResultSet.class,
				SLEXMMTables.T_EVENT,
				SLEXMMTables.T_RELATION, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getEventsForActivityInstance(int)
	 */
	@Override
	public SLEXMMEventResultSet getEventsForActivityInstance(
			int activityInstanceId) {
		return getEventsForActivityInstances(new int[] {activityInstanceId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getEventsForActivityInstances(int[])
	 */
	@Override
	public SLEXMMEventResultSet getEventsForActivityInstances(
			int[] is) {
		return (SLEXMMEventResultSet) getResultSetFor(SLEXMMEventResultSet.class,
				SLEXMMTables.T_EVENT,
				SLEXMMTables.T_ACTIVITY_INSTANCE, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getEventsForAttribute(int)
	 */
	@Override
	public SLEXMMEventResultSet getEventsForAttribute(int attributeId) {
		return getEventsForAttributes(new int[] {attributeId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getEventsForAttributes(int[])
	 */
	@Override
	public SLEXMMEventResultSet getEventsForAttributes(int[] is) {
		return (SLEXMMEventResultSet) getResultSetFor(SLEXMMEventResultSet.class,
				SLEXMMTables.T_EVENT,
				SLEXMMTables.T_ATTRIBUTE_NAME, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectVersionsForObject(int)
	 */
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForObject(int objId) {
		return getObjectVersionsForObjects(new int[] {objId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectVersionsForObjects(int[])
	 */
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForObjects(int[] is) {
		return (SLEXMMObjectVersionResultSet) getResultSetFor(SLEXMMObjectVersionResultSet.class,
				SLEXMMTables.T_OBJECT_VERSION,
				SLEXMMTables.T_OBJECT, is); 
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectVersionsForEvent(int)
	 */
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForEvent(int eventId) {
		return getObjectVersionsForEvents(new int[] {eventId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectVersionsForEvents(int[])
	 */
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForEvents(int[] is) {
		return (SLEXMMObjectVersionResultSet) getResultSetFor(SLEXMMObjectVersionResultSet.class,
				SLEXMMTables.T_OBJECT_VERSION,
				SLEXMMTables.T_EVENT, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectVersionsForCase(int)
	 */
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForCase(int caseId) {
		return getObjectVersionsForCases(new int[] {caseId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectVersionsForCases(int[])
	 */
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForCases(int[] is) {
		return (SLEXMMObjectVersionResultSet) getResultSetFor(SLEXMMObjectVersionResultSet.class,
				SLEXMMTables.T_OBJECT_VERSION,
				SLEXMMTables.T_CASE, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectVersionsForClass(int)
	 */
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForClass(int classId) {
		return getObjectVersionsForClasses(new int[] {classId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectVersionsForClasses(int[])
	 */
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForClasses(int[] is) {
		return (SLEXMMObjectVersionResultSet) getResultSetFor(SLEXMMObjectVersionResultSet.class,
				SLEXMMTables.T_OBJECT_VERSION,
				SLEXMMTables.T_CLASS, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectVersionsForRelationship(int)
	 */
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForRelationship(
			int relationshipId) {
		return getObjectVersionsForRelationships(new int[] {relationshipId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectVersionsForRelationships(int[])
	 */
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForRelationships(
			int[] relationshipIds) {
		return (SLEXMMObjectVersionResultSet) getResultSetFor(SLEXMMObjectVersionResultSet.class,
				SLEXMMTables.T_OBJECT_VERSION,
				SLEXMMTables.T_RELATIONSHIP,relationshipIds);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectVersionsForRelation(int)
	 */
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForRelation(
			int relationId) {
		return getObjectVersionsForRelations(new int[] {relationId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectVersionsForRelations(int[])
	 */
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForRelations(
			int[] is) {
		return (SLEXMMObjectVersionResultSet) getResultSetFor(SLEXMMObjectVersionResultSet.class,
				SLEXMMTables.T_OBJECT_VERSION,
				SLEXMMTables.T_RELATION, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectVersionsForActivityInstance(int)
	 */
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForActivityInstance(
			int activityInstanceId) {
		return getObjectVersionsForActivityInstances(new int[] {activityInstanceId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectVersionsForActivityInstances(int[])
	 */
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForActivityInstances(
			int[] is) {
		return (SLEXMMObjectVersionResultSet) getResultSetFor(SLEXMMObjectVersionResultSet.class,
				SLEXMMTables.T_OBJECT_VERSION,
				SLEXMMTables.T_ACTIVITY_INSTANCE, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectVersionsForActivity(int)
	 */
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForActivity(
			int activityId) {
		return getObjectVersionsForActivities(new int[] {activityId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectVersionsForActivities(int[])
	 */
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForActivities( 
			int[] is) {
		return (SLEXMMObjectVersionResultSet) getResultSetFor(SLEXMMObjectVersionResultSet.class,
				SLEXMMTables.T_OBJECT_VERSION,
				SLEXMMTables.T_ACTIVITY, is);
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectVersionsForAttribute(int)
	 */
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForAttribute(
			int attributeId) {
		return getObjectVersionsForAttributes(new int[] {attributeId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getObjectVersionsForAttributes(int[])
	 */
	@Override
	public SLEXMMObjectVersionResultSet getObjectVersionsForAttributes(
			int[] is) {
		return (SLEXMMObjectVersionResultSet) getResultSetFor(SLEXMMObjectVersionResultSet.class,
				SLEXMMTables.T_OBJECT_VERSION,
				SLEXMMTables.T_ATTRIBUTE_NAME, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivitiesForObject(int)
	 */
	@Override
	public SLEXMMActivityResultSet getActivitiesForObject(int objectId) {
		return getActivitiesForObjects(new int[] {objectId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivitiesForObjects(int[])
	 */
	@Override
	public SLEXMMActivityResultSet getActivitiesForObjects(int[] is) {
		return (SLEXMMActivityResultSet) getResultSetFor(SLEXMMActivityResultSet.class,
				SLEXMMTables.T_ACTIVITY,
				SLEXMMTables.T_OBJECT, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivitiesForEvent(int)
	 */
	@Override
	public SLEXMMActivityResultSet getActivitiesForEvent(int eventId) {
		return getActivitiesForEvents(new int[] {eventId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivitiesForEvents(int[])
	 */
	@Override
	public SLEXMMActivityResultSet getActivitiesForEvents(int[] is) {
		return (SLEXMMActivityResultSet) getResultSetFor(SLEXMMActivityResultSet.class,
				SLEXMMTables.T_ACTIVITY,
				SLEXMMTables.T_EVENT, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivitiesForCase(int)
	 */
	@Override
	public SLEXMMActivityResultSet getActivitiesForCase(int caseId) {
		return getActivitiesForCases(new int[] {caseId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivitiesForCases(int[])
	 */
	@Override
	public SLEXMMActivityResultSet getActivitiesForCases(int[] is) {
		return (SLEXMMActivityResultSet) getResultSetFor(SLEXMMActivityResultSet.class,
				SLEXMMTables.T_ACTIVITY,
				SLEXMMTables.T_CASE, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivitiesForClass(int)
	 */
	@Override
	public SLEXMMActivityResultSet getActivitiesForClass(int classId) {
		return getActivitiesForClasses(new int[] {classId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivitiesForClasses(int[])
	 */
	@Override
	public SLEXMMActivityResultSet getActivitiesForClasses(int[] is) {
		return (SLEXMMActivityResultSet) getResultSetFor(SLEXMMActivityResultSet.class,
				SLEXMMTables.T_ACTIVITY,
				SLEXMMTables.T_CLASS, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivitiesForRelationship(int)
	 */
	@Override
	public SLEXMMActivityResultSet getActivitiesForRelationship(
			int relationshipId) {
		return getActivitiesForRelationships(new int[] {relationshipId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivitiesForRelationships(int[])
	 */
	@Override
	public SLEXMMActivityResultSet getActivitiesForRelationships(
			int[] is) {
		return (SLEXMMActivityResultSet) getResultSetFor(SLEXMMActivityResultSet.class,
				SLEXMMTables.T_ACTIVITY,
				SLEXMMTables.T_RELATIONSHIP, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivitiesForObjectVersion(int)
	 */
	@Override
	public SLEXMMActivityResultSet getActivitiesForObjectVersion(
			int objectVersionId) {
		return getActivitiesForObjectVersions(new int[] {objectVersionId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivitiesForObjectVersions(int[])
	 */
	@Override
	public SLEXMMActivityResultSet getActivitiesForObjectVersions(
			int[] is) {
		return (SLEXMMActivityResultSet) getResultSetFor(SLEXMMActivityResultSet.class,
				SLEXMMTables.T_ACTIVITY,
				SLEXMMTables.T_OBJECT_VERSION, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivitiesForRelation(int)
	 */
	@Override
	public SLEXMMActivityResultSet getActivitiesForRelation(int relationId) {
		return getActivitiesForRelations(new int[] {relationId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivitiesForRelations(int[])
	 */
	@Override
	public SLEXMMActivityResultSet getActivitiesForRelations(int[] is) {
		return (SLEXMMActivityResultSet) getResultSetFor(SLEXMMActivityResultSet.class,
				SLEXMMTables.T_ACTIVITY,
				SLEXMMTables.T_RELATION, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivitiesForActivityInstance(int)
	 */
	@Override
	public SLEXMMActivityResultSet getActivitiesForActivityInstance(
			int activityInstanceId) {
		return getActivitiesForActivityInstances(new int[] {activityInstanceId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivitiesForActivityInstances(int[])
	 */
	@Override
	public SLEXMMActivityResultSet getActivitiesForActivityInstances(
			int[] is) {
		return (SLEXMMActivityResultSet) getResultSetFor(SLEXMMActivityResultSet.class,
				SLEXMMTables.T_ACTIVITY,
				SLEXMMTables.T_ACTIVITY_INSTANCE, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivitiesForAttribute(int)
	 */
	@Override
	public SLEXMMActivityResultSet getActivitiesForAttribute(int attributeId) {
		return getActivitiesForAttributes(new int[] {attributeId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivitiesForAttributes(int[])
	 */
	@Override
	public SLEXMMActivityResultSet getActivitiesForAttributes(int[] is) {
		return (SLEXMMActivityResultSet) getResultSetFor(SLEXMMActivityResultSet.class,
				SLEXMMTables.T_ACTIVITY,
				SLEXMMTables.T_ATTRIBUTE_NAME, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getClassesForObject(int)
	 */
	@Override
	public SLEXMMClassResultSet getClassesForObject(int objectId) {
		return getClassesForObjects(new int[] {objectId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getClassesForObjects(int[])
	 */
	@Override
	public SLEXMMClassResultSet getClassesForObjects(int[] is) {
		return (SLEXMMClassResultSet) getResultSetFor(SLEXMMClassResultSet.class,
				SLEXMMTables.T_CLASS,
				SLEXMMTables.T_OBJECT, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getClassesForEvent(int)
	 */
	@Override
	public SLEXMMClassResultSet getClassesForEvent(int eventId) {
		return getClassesForEvents(new int[] {eventId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getClassesForEvents(int[])
	 */
	@Override
	public SLEXMMClassResultSet getClassesForEvents(int[] is) {
		return (SLEXMMClassResultSet) getResultSetFor(SLEXMMClassResultSet.class,
				SLEXMMTables.T_CLASS,
				SLEXMMTables.T_EVENT, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getClassesForCase(int)
	 */
	@Override
	public SLEXMMClassResultSet getClassesForCase(int caseId) {
		return getClassesForCases(new int[] {caseId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getClassesForCases(int[])
	 */
	@Override
	public SLEXMMClassResultSet getClassesForCases(int[] is) {
		return (SLEXMMClassResultSet) getResultSetFor(SLEXMMClassResultSet.class,
				SLEXMMTables.T_CLASS,
				SLEXMMTables.T_CASE, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getClassesForActivity(int)
	 */
	@Override
	public SLEXMMClassResultSet getClassesForActivity(int activityId) {
		return getClassesForActivities(new int[] {activityId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getClassesForActivities(int[])
	 */
	@Override
	public SLEXMMClassResultSet getClassesForActivities(int[] is) {
		return (SLEXMMClassResultSet) getResultSetFor(SLEXMMClassResultSet.class,
				SLEXMMTables.T_CLASS,
				SLEXMMTables.T_ACTIVITY, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getClassesForRelationship(int)
	 */
	@Override
	public SLEXMMClassResultSet getClassesForRelationship(int relationshipId) {
		return getClassesForRelationships(new int[] {relationshipId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getClassesForRelationships(int[])
	 */
	@Override
	public SLEXMMClassResultSet getClassesForRelationships(int[] relationshipIds) {
		return (SLEXMMClassResultSet) getResultSetFor(SLEXMMClassResultSet.class,
				SLEXMMTables.T_CLASS,
				SLEXMMTables.T_RELATIONSHIP, relationshipIds);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getClassesForObjectVersion(int)
	 */
	@Override
	public SLEXMMClassResultSet getClassesForObjectVersion(
			int objectVersionId) {
		return getClassesForObjectVersions(new int[] {objectVersionId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getClassesForObjectVersions(int[])
	 */
	@Override
	public SLEXMMClassResultSet getClassesForObjectVersions(
			int[] is) {
		return (SLEXMMClassResultSet) getResultSetFor(SLEXMMClassResultSet.class,
				SLEXMMTables.T_CLASS,
				SLEXMMTables.T_OBJECT_VERSION, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getClassesForRelation(int)
	 */
	@Override
	public SLEXMMClassResultSet getClassesForRelation(int relationId) {
		return getClassesForRelations(new int[] {relationId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getClassesForRelations(int[])
	 */
	@Override
	public SLEXMMClassResultSet getClassesForRelations(int[] is) {
		return (SLEXMMClassResultSet) getResultSetFor(SLEXMMClassResultSet.class,
				SLEXMMTables.T_CLASS,
				SLEXMMTables.T_RELATION, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getClassesForActivityInstance(int)
	 */
	@Override
	public SLEXMMClassResultSet getClassesForActivityInstance(
			int activityInstanceId) {
		return getClassesForActivityInstances(new int[] {activityInstanceId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getClassesForActivityInstances(int[])
	 */
	@Override
	public SLEXMMClassResultSet getClassesForActivityInstances(
			int[] is) {
		return (SLEXMMClassResultSet) getResultSetFor(SLEXMMClassResultSet.class,
				SLEXMMTables.T_CLASS,
				SLEXMMTables.T_ACTIVITY_INSTANCE, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getClassesForAttribute(int)
	 */
	@Override
	public SLEXMMClassResultSet getClassesForAttribute(int attributeId) {
		return getClassesForAttributes(new int[] {attributeId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getClassesForAttributes(int[])
	 */
	@Override
	public SLEXMMClassResultSet getClassesForAttributes(int[] is) {
		return (SLEXMMClassResultSet) getResultSetFor(SLEXMMClassResultSet.class,
				SLEXMMTables.T_CLASS,
				SLEXMMTables.T_ATTRIBUTE_NAME, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationsForObject(int)
	 */
	@Override
	public SLEXMMRelationResultSet getRelationsForObject(int objectId) {
		return getRelationsForObjects(new int[] {objectId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationsForObjects(int[])
	 */
	@Override
	public SLEXMMRelationResultSet getRelationsForObjects(int[] is) {
		return (SLEXMMRelationResultSet) getResultSetFor(SLEXMMRelationResultSet.class,
				SLEXMMTables.T_RELATION,
				SLEXMMTables.T_OBJECT, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationsForEvent(int)
	 */
	@Override
	public SLEXMMRelationResultSet getRelationsForEvent(int eventId) {
		return getRelationsForEvents(new int[] {eventId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationsForEvents(int[])
	 */
	@Override
	public SLEXMMRelationResultSet getRelationsForEvents(int[] is) {
		return (SLEXMMRelationResultSet) getResultSetFor(SLEXMMRelationResultSet.class,
				SLEXMMTables.T_RELATION,
				SLEXMMTables.T_EVENT, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationsForCase(int)
	 */
	@Override
	public SLEXMMRelationResultSet getRelationsForCase(int caseId) {
		return getRelationsForCases(new int[] {caseId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationsForCases(int[])
	 */
	@Override
	public SLEXMMRelationResultSet getRelationsForCases(int[] is) {
		return (SLEXMMRelationResultSet) getResultSetFor(SLEXMMRelationResultSet.class,
				SLEXMMTables.T_RELATION,
				SLEXMMTables.T_CASE, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationsForActivity(int)
	 */
	@Override
	public SLEXMMRelationResultSet getRelationsForActivity(int activityId) { 
		return getRelationsForActivities(new int[] {activityId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationsForActivities(int[])
	 */
	@Override
	public SLEXMMRelationResultSet getRelationsForActivities(int[] is) {
		return (SLEXMMRelationResultSet) getResultSetFor(SLEXMMRelationResultSet.class,
				SLEXMMTables.T_RELATION,
				SLEXMMTables.T_ACTIVITY, is);
	}

	/**
	 * Builds the string from array.
	 *
	 * @param ids the ids
	 * @return the string
	 */
	public static String buildStringFromArray(int[] ids) {
		StringBuffer buf = new StringBuffer();
		
		for (int i = 0; i < ids.length; i++) {
			buf.append(String.valueOf(ids[i]));
			if (i < ids.length - 1) {
				buf.append(",");
			}
		}
		
		return buf.toString();
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationsForClass(int)
	 */
	@Override
	public SLEXMMRelationResultSet getRelationsForClass(int classId) {
		return getRelationsForClasses(new int[] {classId});
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationsForClasses(int[])
	 */
	@Override
	public SLEXMMRelationResultSet getRelationsForClasses(int[] is) {
		return (SLEXMMRelationResultSet) getResultSetFor(SLEXMMRelationResultSet.class,
				SLEXMMTables.T_RELATION,
				SLEXMMTables.T_CLASS, is);
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationsForRelationship(int)
	 */
	@Override
	public SLEXMMRelationResultSet getRelationsForRelationship(
			int relationshipId) {
		return getRelationsForRelationships(new int[] {relationshipId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationsForRelationships(int[])
	 */
	@Override
	public SLEXMMRelationResultSet getRelationsForRelationships(
			int[] is) {
		return (SLEXMMRelationResultSet) getResultSetFor(SLEXMMRelationResultSet.class,
				SLEXMMTables.T_RELATION,
				SLEXMMTables.T_RELATIONSHIP, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationsForObjectVersion(int)
	 */
	@Override
	public SLEXMMRelationResultSet getRelationsForObjectVersion(
			int objectVersionId) {
		return getRelationsForObjectVersions(new int []{objectVersionId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationsForObjectVersions(int[])
	 */
	@Override
	public SLEXMMRelationResultSet getRelationsForObjectVersions(
			int[] is) {
		return (SLEXMMRelationResultSet) getResultSetFor(SLEXMMRelationResultSet.class,
				SLEXMMTables.T_RELATION,
				SLEXMMTables.T_OBJECT_VERSION, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationsForActivityInstance(int)
	 */
	@Override
	public SLEXMMRelationResultSet getRelationsForActivityInstance(
			int activityInstanceId) { 
		return getRelationsForActivityInstances(new int[] {activityInstanceId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationsForActivityInstances(int[])
	 */
	@Override
	public SLEXMMRelationResultSet getRelationsForActivityInstances(
			int[] is) {
		return (SLEXMMRelationResultSet) getResultSetFor(SLEXMMRelationResultSet.class,
				SLEXMMTables.T_RELATION,
				SLEXMMTables.T_ACTIVITY_INSTANCE, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationsForAttribute(int)
	 */
	@Override
	public SLEXMMRelationResultSet getRelationsForAttribute(int attributeId) {
		return getRelationsForAttributes(new int[] {attributeId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationsForAttributes(int[])
	 */
	@Override
	public SLEXMMRelationResultSet getRelationsForAttributes(int[] is) {
		return (SLEXMMRelationResultSet) getResultSetFor(SLEXMMRelationResultSet.class,
				SLEXMMTables.T_RELATION,
				SLEXMMTables.T_ATTRIBUTE_NAME, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationshipsForObject(int)
	 */
	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForObject(int objectId) { 
		return getRelationshipsForObjects(new int[]{objectId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationshipsForObjects(int[])
	 */
	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForObjects(int[] is) {
		return (SLEXMMRelationshipResultSet) getResultSetFor(SLEXMMRelationshipResultSet.class,
				SLEXMMTables.T_RELATIONSHIP,
				SLEXMMTables.T_OBJECT, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationshipsForEvent(int)
	 */
	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForEvent(int eventId) { 
		return getRelationshipsForEvents(new int[] {eventId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationshipsForEvents(int[])
	 */
	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForEvents(int[] is) {
		return (SLEXMMRelationshipResultSet) getResultSetFor(SLEXMMRelationshipResultSet.class,
				SLEXMMTables.T_RELATIONSHIP,
				SLEXMMTables.T_EVENT, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationshipsForCase(int)
	 */
	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForCase(int caseId) { 
		return getRelationshipsForCases(new int[] {caseId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationshipsForCases(int[])
	 */
	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForCases(int[] is) {
		return (SLEXMMRelationshipResultSet) getResultSetFor(SLEXMMRelationshipResultSet.class,
				SLEXMMTables.T_RELATIONSHIP,
				SLEXMMTables.T_CASE, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationshipsForActivity(int)
	 */
	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForActivity(
			int activityId) { 
		return getRelationshipsForActivities(new int[] {activityId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationshipsForActivities(int[])
	 */
	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForActivities(
			int[] is) {
		return (SLEXMMRelationshipResultSet) getResultSetFor(SLEXMMRelationshipResultSet.class,
				SLEXMMTables.T_RELATIONSHIP,
				SLEXMMTables.T_ACTIVITY, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationshipsForClass(int)
	 */
	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForClass(int classId) { 
		return getRelationshipsForClasses(new int[] {classId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationshipsForClasses(int[])
	 */
	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForClasses(int[] is) {
		return (SLEXMMRelationshipResultSet) getResultSetFor(SLEXMMRelationshipResultSet.class,
				SLEXMMTables.T_RELATIONSHIP,
				SLEXMMTables.T_CLASS, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationshipsForObjectVersion(int)
	 */
	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForObjectVersion(
			int objectVersionId) { 
		return getRelationshipsForObjectVersions(new int[] {objectVersionId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationshipsForObjectVersions(int[])
	 */
	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForObjectVersions(
			int[] objectVersionIds) { 
		
		return (SLEXMMRelationshipResultSet) getResultSetFor(SLEXMMRelationshipResultSet.class,
				SLEXMMTables.T_RELATIONSHIP,SLEXMMTables.T_OBJECT_VERSION,objectVersionIds);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationshipsForRelation(int)
	 */
	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForRelation(
			int relationId) { 
		return getRelationshipsForRelations(new int[] {relationId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationshipsForRelations(int[])
	 */
	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForRelations(int[] is) {
		return (SLEXMMRelationshipResultSet) getResultSetFor(SLEXMMRelationshipResultSet.class,
				SLEXMMTables.T_RELATIONSHIP,
				SLEXMMTables.T_RELATION, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationshipsForActivityInstance(int)
	 */
	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForActivityInstance(
			int activityInstanceId) {
		return getRelationshipsForActivityInstances(new int[] {activityInstanceId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationshipsForActivityInstances(int[])
	 */
	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForActivityInstances(
			int[] is) {
		return (SLEXMMRelationshipResultSet) getResultSetFor(SLEXMMRelationshipResultSet.class,
				SLEXMMTables.T_RELATIONSHIP,
				SLEXMMTables.T_ACTIVITY_INSTANCE, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationshipsForAttribute(int)
	 */
	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForAttribute(
			int attributeId) {
		return getRelationshipsForAttributes(new int[]{attributeId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getRelationshipsForAttributes(int[])
	 */
	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForAttributes(
			int[] is) { 
		return (SLEXMMRelationshipResultSet) getResultSetFor(SLEXMMRelationshipResultSet.class,
				SLEXMMTables.T_RELATIONSHIP,
				SLEXMMTables.T_ATTRIBUTE_NAME, is);
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivityInstancesForCase(int)
	 */
	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForCase(
			int caseId) {
		return getActivityInstancesForCases(new int[] {caseId});
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivityInstancesForCases(int[])
	 */
	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForCases(
			int[] is) {
		return (SLEXMMActivityInstanceResultSet) getResultSetFor(SLEXMMActivityInstanceResultSet.class,
				SLEXMMTables.T_ACTIVITY_INSTANCE,
				SLEXMMTables.T_CASE, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivityInstancesForObject(int)
	 */
	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForObject(
			int objectId) {
		return getActivityInstancesForObjects(new int[] {objectId});
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivityInstancesForObjects(int[])
	 */
	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForObjects(
			int[] is) {
		return (SLEXMMActivityInstanceResultSet) getResultSetFor(SLEXMMActivityInstanceResultSet.class,
				SLEXMMTables.T_ACTIVITY_INSTANCE,
				SLEXMMTables.T_OBJECT, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivityInstancesForEvent(int)
	 */
	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForEvent(
			int eventId) {
		return getActivityInstancesForEvents(new int[] {eventId});
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivityInstancesForEvents(int[])
	 */
	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForEvents(
			int[] is) {
		return (SLEXMMActivityInstanceResultSet) getResultSetFor(SLEXMMActivityInstanceResultSet.class,
				SLEXMMTables.T_ACTIVITY_INSTANCE,
				SLEXMMTables.T_EVENT, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivityInstancesForActivity(int)
	 */
	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForActivity(
			int activityId) {
		return getActivityInstancesForActivities(new int[] {activityId});
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivityInstancesForActivities(int[])
	 */
	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForActivities(
			int[] is) {
		return (SLEXMMActivityInstanceResultSet) getResultSetFor(SLEXMMActivityInstanceResultSet.class,
				SLEXMMTables.T_ACTIVITY_INSTANCE,
				SLEXMMTables.T_ACTIVITY, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivityInstancesForClass(int)
	 */
	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForClass(
			int classId) {
		return getActivityInstancesForClasses(new int[] {classId});
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivityInstancesForClasses(int[])
	 */
	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForClasses(
			int[] is) {
		return (SLEXMMActivityInstanceResultSet) getResultSetFor(SLEXMMActivityInstanceResultSet.class,
				SLEXMMTables.T_ACTIVITY_INSTANCE,
				SLEXMMTables.T_CLASS, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivityInstancesForRelationship(int)
	 */
	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForRelationship(
			int relationshipId) {
		return getActivityInstancesForRelationships(new int[] {relationshipId});
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivityInstancesForRelationships(int[])
	 */
	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForRelationships(
			int[] is) {
		return (SLEXMMActivityInstanceResultSet) getResultSetFor(SLEXMMActivityInstanceResultSet.class,
				SLEXMMTables.T_ACTIVITY_INSTANCE,
				SLEXMMTables.T_RELATIONSHIP, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivityInstancesForObjectVersion(int)
	 */
	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForObjectVersion(
			int objectVersionId) {
		return getActivityInstancesForObjectVersions(new int[] {objectVersionId});
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivityInstancesForObjectVersions(int[])
	 */
	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForObjectVersions(
			int[] is) {
		return (SLEXMMActivityInstanceResultSet) getResultSetFor(SLEXMMActivityInstanceResultSet.class,
				SLEXMMTables.T_ACTIVITY_INSTANCE,
				SLEXMMTables.T_OBJECT_VERSION, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivityInstancesForRelation(int)
	 */
	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForRelation(
			int relationId) {
		return getActivityInstancesForRelations(new int[] {relationId});
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivityInstancesForRelations(int[])
	 */
	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForRelations(
			int[] is) {
		
		return (SLEXMMActivityInstanceResultSet) getResultSetFor(SLEXMMActivityInstanceResultSet.class,
				SLEXMMTables.T_ACTIVITY_INSTANCE,
				SLEXMMTables.T_RELATION, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivityInstancesForAttribute(int)
	 */
	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForAttribute(
			int attributeId) {
		return getActivityInstancesForAttributes(new int[] {attributeId});
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getActivityInstancesForAttributes(int[])
	 */
	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForAttributes(
			int[] is) {
		return (SLEXMMActivityInstanceResultSet) getResultSetFor(SLEXMMActivityInstanceResultSet.class,
				SLEXMMTables.T_ACTIVITY_INSTANCE,
				SLEXMMTables.T_ATTRIBUTE_NAME, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getAttributesForObject(int)
	 */
	@Override
	public SLEXMMAttributeResultSet getAttributesForObject(int objectId) {
		return getAttributesForObjects(new int[] {objectId});
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getAttributesForObjects(int[])
	 */
	@Override
	public SLEXMMAttributeResultSet getAttributesForObjects(int[] is) {
		return (SLEXMMAttributeResultSet) getResultSetFor(SLEXMMAttributeResultSet.class,
				SLEXMMTables.T_ATTRIBUTE_NAME,
				SLEXMMTables.T_OBJECT, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getAttributesForEvent(int)
	 */
	@Override
	public SLEXMMAttributeResultSet getAttributesForEvent(int eventId) {
		return getAttributesForEvents(new int[] {eventId});
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getAttributesForEvents(int[])
	 */
	@Override
	public SLEXMMAttributeResultSet getAttributesForEvents(int[] is) {
		return (SLEXMMAttributeResultSet) getResultSetFor(SLEXMMAttributeResultSet.class,
				SLEXMMTables.T_ATTRIBUTE_NAME,
				SLEXMMTables.T_EVENT, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getAttributesForCase(int)
	 */
	@Override
	public SLEXMMAttributeResultSet getAttributesForCase(int caseId) {
		return getAttributesForCases(new int[] {caseId});
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getAttributesForCases(int[])
	 */
	@Override
	public SLEXMMAttributeResultSet getAttributesForCases(int[] is) {
		return (SLEXMMAttributeResultSet) getResultSetFor(SLEXMMAttributeResultSet.class,
				SLEXMMTables.T_ATTRIBUTE_NAME,
				SLEXMMTables.T_CASE, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getAttributesForActivity(int)
	 */
	@Override
	public SLEXMMAttributeResultSet getAttributesForActivity(int activityId) {
		return getAttributesForActivities(new int[] {activityId});
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getAttributesForActivities(int[])
	 */
	@Override
	public SLEXMMAttributeResultSet getAttributesForActivities(int[] is) {
		return (SLEXMMAttributeResultSet) getResultSetFor(SLEXMMAttributeResultSet.class,
				SLEXMMTables.T_ATTRIBUTE_NAME,
				SLEXMMTables.T_ACTIVITY, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getAttributesForClass(int)
	 */
	@Override
	public SLEXMMAttributeResultSet getAttributesForClass(int classId) {
		return getAttributesForClasses(new int[] {classId});
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getAttributesForClasses(int[])
	 */
	@Override
	public SLEXMMAttributeResultSet getAttributesForClasses(int[] is) {
		return (SLEXMMAttributeResultSet) getResultSetFor(SLEXMMAttributeResultSet.class,
				SLEXMMTables.T_ATTRIBUTE_NAME,
				SLEXMMTables.T_CLASS, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getAttributesForRelationship(int)
	 */
	@Override
	public SLEXMMAttributeResultSet getAttributesForRelationship(
			int relationshipId) {
		return getAttributesForRelationships(new int[] {relationshipId});
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getAttributesForRelationships(int[])
	 */
	@Override
	public SLEXMMAttributeResultSet getAttributesForRelationships(
			int[] is) {
		return (SLEXMMAttributeResultSet) getResultSetFor(SLEXMMAttributeResultSet.class,
				SLEXMMTables.T_ATTRIBUTE_NAME,
				SLEXMMTables.T_RELATIONSHIP, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getAttributesForObjectVersion(int)
	 */
	@Override
	public SLEXMMAttributeResultSet getAttributesForObjectVersion(
			int objectVersionId) {
		return getAttributesForObjectVersions(new int[] {objectVersionId});
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getAttributesForObjectVersions(int[])
	 */
	@Override
	public SLEXMMAttributeResultSet getAttributesForObjectVersions(
			int[] is) {
		return (SLEXMMAttributeResultSet) getResultSetFor(SLEXMMAttributeResultSet.class,
				SLEXMMTables.T_ATTRIBUTE_NAME,
				SLEXMMTables.T_OBJECT_VERSION, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getAttributesForRelation(int)
	 */
	@Override
	public SLEXMMAttributeResultSet getAttributesForRelation(int relationId) {
		return getAttributesForRelations(new int[] {relationId});
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getAttributesForRelations(int[])
	 */
	@Override
	public SLEXMMAttributeResultSet getAttributesForRelations(int[] is) {
		return (SLEXMMAttributeResultSet) getResultSetFor(SLEXMMAttributeResultSet.class,
				SLEXMMTables.T_ATTRIBUTE_NAME,
				SLEXMMTables.T_RELATION, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getAttributesForActivityInstance(int)
	 */
	@Override
	public SLEXMMAttributeResultSet getAttributesForActivityInstance(
			int activityInstanceId) {
		return getAttributesForActivityInstances(new int[] {activityInstanceId});
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#getAttributesForActivityInstances(int[])
	 */
	@Override
	public SLEXMMAttributeResultSet getAttributesForActivityInstances(
			int[] is) {
		return (SLEXMMAttributeResultSet) getResultSetFor(SLEXMMAttributeResultSet.class,
				SLEXMMTables.T_ATTRIBUTE_NAME,
				SLEXMMTables.T_ACTIVITY_INSTANCE, is);
	}

	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#insert(org.processmining.openslex.metamodel.SLEXMMLog)
	 */
	@Override
	public synchronized boolean insert(SLEXMMLog log) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
			statement.setQueryTimeout(30);
			statement.execute("INSERT INTO "+METAMODEL_ALIAS+".log (process_id,name) VALUES ('"+
					log.getProcessId()+"','"+log.getName()+"')");
			log.setId(getLastInsertedRowId(statement));
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeStatement(statement);
		}
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.processmining.openslex.metamodel.SLEXMMStorageMetaModel#update(org.processmining.openslex.metamodel.SLEXMMLog)
	 */
	@Override
	public boolean update(SLEXMMLog log) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
			statement.setQueryTimeout(30);
			statement.execute("UPDATE "+METAMODEL_ALIAS+".log"+
					" SET process_id = '"+log.getProcessId()+"' "+
					" AND name = '"+log.getName()+"'"+
					" WHERE id = '"+log.getId()+"'");
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
	public SLEXMMCaseResultSet getCasesForLog(int logId) {
		return getCasesForLogs(new int[] {logId});
	}

	@Override
	public SLEXMMCaseResultSet getCasesForLogs(int[] is) {
		return (SLEXMMCaseResultSet) getResultSetFor(SLEXMMCaseResultSet.class,
				SLEXMMTables.T_CASE,
				SLEXMMTables.T_LOG, is);
	}

	@Override
	public SLEXMMCaseResultSet getCasesForProcess(int processId) {
		return getCasesForProcess(new int[] {processId});
	}

	@Override
	public SLEXMMCaseResultSet getCasesForProcess(int[] is) {
		return (SLEXMMCaseResultSet) getResultSetFor(SLEXMMCaseResultSet.class,
				SLEXMMTables.T_CASE,
				SLEXMMTables.T_PROCESS, is);
	}

	@Override
	public boolean addCaseToLog(int logId, int caseId) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
			statement.setQueryTimeout(30);
			statement.execute("INSERT INTO "+METAMODEL_ALIAS+".case_to_log (case_id,log_id) VALUES ('"
					+caseId+"','"+logId+"')");
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
	public boolean addActivityToProcess(int processId, int actId) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
			statement.setQueryTimeout(30);
			statement.execute("INSERT INTO "+METAMODEL_ALIAS+".activity_to_process (process_id,activity_id) VALUES ('"
					+processId+"','"+actId+"')");
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
	public boolean insert(SLEXMMCaseAttribute at) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
			statement.setQueryTimeout(30);
			statement.execute("INSERT INTO "+METAMODEL_ALIAS+".case_attribute_name (name) VALUES ('"+
					"'"+at.getName()+"')");
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
	public boolean update(SLEXMMCaseAttribute at) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
			statement.setQueryTimeout(30);
			statement.execute("UPDATE "+METAMODEL_ALIAS+".case_attribute_name "+
					" SET name = '"+at.getName()+"'"+
					" WHERE id = '"+at.getId()+"'");
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
	public boolean update(SLEXMMCaseAttributeValue at) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
			statement.setQueryTimeout(30);
			statement.execute("UPDATE "+METAMODEL_ALIAS+".case_attribute_value "+
					" SET case_id = '"+at.getCaseId()+"'"+
					" AND case_attribute_name_id = '"+at.getAttributeId()+"'"+
					" AND value = '"+at.getValue()+"'"+
					" AND type = '"+at.getType()+"'"+
					" WHERE id = '"+at.getId()+"'");
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
	public boolean insert(SLEXMMCaseAttributeValue at) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
			statement.setQueryTimeout(30);
			statement.execute("INSERT INTO "+METAMODEL_ALIAS+".case_attribute_value (case_id,case_attribute_name_id,value,type) VALUES ('"+
					"'"+at.getCaseId()+"','"+at.getAttributeId()+"','"+at.getValue()+"','"+at.getType()+"')");
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
	public HashMap<SLEXMMCaseAttribute, SLEXMMCaseAttributeValue> getAttributeValuesForCase(SLEXMMCase slexmmCase) {
		return getAttributeValuesForCase(slexmmCase.getId());
	}
	
	@Override
	public HashMap<SLEXMMCaseAttribute, SLEXMMCaseAttributeValue> getAttributeValuesForCase(int caseId) {
		HashMap<SLEXMMCaseAttribute, SLEXMMCaseAttributeValue> attributeValues = null;
		Statement statement = null;
		try {
			statement = createStatement();
			statement.setQueryTimeout(30);
			ResultSet rset = statement.executeQuery("SELECT CAT.id, CAT.name, CATV.value, CATV.type FROM "+
														METAMODEL_ALIAS+".case_attribute_name AS CAT, "+
														METAMODEL_ALIAS+".case_attribute_value AS CATV "+
														"WHERE CATV.case_id = "+caseId+" AND "
																+ " CATV.case_attribute_name_id = CAT.id ");
			attributeValues = new HashMap<>();
			
			while (rset.next()) {
				SLEXMMCaseAttribute at = new SLEXMMCaseAttribute(this);
				at.setId(rset.getInt(1));
				at.setName(rset.getString(2));
				at.setDirty(false);
				at.setInserted(true);
				SLEXMMCaseAttributeValue atv = new SLEXMMCaseAttributeValue(this, at.getId(), caseId);
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
	public boolean update(SLEXMMLogAttributeValue at) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
			statement.setQueryTimeout(30);
			statement.execute("UPDATE "+METAMODEL_ALIAS+".log_attribute_value "+
					" SET log_id = '"+at.getLogId()+"'"+
					" AND log_attribute_name_id = '"+at.getAttributeId()+"'"+
					" AND value = '"+at.getValue()+"'"+
					" AND type = '"+at.getType()+"'"+
					" WHERE id = '"+at.getId()+"'");
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
	public boolean insert(SLEXMMLogAttributeValue at) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
			statement.setQueryTimeout(30);
			statement.execute("INSERT INTO "+METAMODEL_ALIAS+".log_attribute_value (log_id,log_attribute_name_id,value,type) VALUES ('"+
					"'"+at.getLogId()+"','"+at.getAttributeId()+"','"+at.getValue()+"','"+at.getType()+"')");
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
	public boolean update(SLEXMMLogAttribute at) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
			statement.setQueryTimeout(30);
			statement.execute("UPDATE "+METAMODEL_ALIAS+".log_attribute_name "+
					" SET name = '"+at.getName()+"'"+
					" WHERE id = '"+at.getId()+"'");
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
	public boolean insert(SLEXMMLogAttribute at) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
			statement.setQueryTimeout(30);
			statement.execute("INSERT INTO "+METAMODEL_ALIAS+".log_attribute_name (name) VALUES ('"+
					"'"+at.getName()+"')");
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
	public HashMap<SLEXMMLogAttribute, SLEXMMLogAttributeValue> getAttributeValuesForLog(SLEXMMLog slexmmLog) {
		return getAttributeValuesForLog(slexmmLog.getId());
	}

	@Override
	public HashMap<SLEXMMLogAttribute, SLEXMMLogAttributeValue> getAttributeValuesForLog(int logId) {
		HashMap<SLEXMMLogAttribute, SLEXMMLogAttributeValue> attributeValues = null;
		Statement statement = null;
		try {
			statement = createStatement();
			statement.setQueryTimeout(30);
			ResultSet rset = statement.executeQuery("SELECT LAT.id, LAT.name, LATV.value, LATV.type FROM "+
														METAMODEL_ALIAS+".log_attribute_name AS LAT, "+
														METAMODEL_ALIAS+".log_attribute_value AS LATV "+
														"WHERE LATV.log_id = "+logId+" AND "
																+ " LATV.log_attribute_name_id = LAT.id ");
			attributeValues = new HashMap<>();
			
			while (rset.next()) {
				SLEXMMLogAttribute at = new SLEXMMLogAttribute(this);
				at.setId(rset.getInt(1));
				at.setName(rset.getString(2));
				at.setDirty(false);
				at.setInserted(true);
				SLEXMMLogAttributeValue atv = new SLEXMMLogAttributeValue(this, at.getId(), logId);
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
	public List<SLEXMMClassifierAttribute> getListAttributesForClassifier(SLEXMMClassifier slexmmClassifier) {
		return getListAttributesForClassifier(slexmmClassifier.getId());
	}
	
	@Override
	public List<SLEXMMClassifierAttribute> getListAttributesForClassifier(int classifierId) {
		ArrayList<SLEXMMClassifierAttribute> attributes = null;
		Statement statement = null;
		try {
			statement = createStatement();
			statement.setQueryTimeout(30);
			ResultSet rset = statement.executeQuery("SELECT CLAT.id, CLAT.classifier_id, CLAT.event_attribute_name FROM "+
														METAMODEL_ALIAS+".classifier_attributes AS CLAT "+
														"WHERE CLAT.classifier_id = "+classifierId);
			attributes = new ArrayList<>();
			
			while (rset.next()) {
				SLEXMMClassifierAttribute at = new SLEXMMClassifierAttribute(this);
				at.setId(rset.getInt(1));
				at.setClassifierId(rset.getInt(2));
				at.setEventAttributeNameId(rset.getInt(3));
				at.setDirty(false);
				at.setInserted(true);
				attributes.add(at);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			attributes = null;
		} finally {
			closeStatement(statement);
		}
		return attributes;
	}

	@Override
	public boolean insert(SLEXMMClassifier cl) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
			statement.setQueryTimeout(30);
			statement.execute("INSERT INTO "+METAMODEL_ALIAS+".classifier (log_id,name) VALUES ('"+
					"'"+cl.getLogId()+"','"+cl.getName()+"')");
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
	public boolean update(SLEXMMClassifier cl) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
			statement.setQueryTimeout(30);
			statement.execute("UPDATE "+METAMODEL_ALIAS+".classifier "+
					" SET name = '"+cl.getName()+"'"+
					" AND log_id = '"+cl.getLogId()+"'"+
					" WHERE id = '"+cl.getId()+"'");
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
	public boolean insert(SLEXMMClassifierAttribute at) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
			statement.setQueryTimeout(30);
			statement.execute("INSERT INTO "+METAMODEL_ALIAS+".classifier_attributes (classifier_id,event_attribute_name_id) VALUES ('"+
					"'"+at.getClassifierId()+"','"+at.getEventAttributeNameId()+"')");
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
	public boolean update(SLEXMMClassifierAttribute at) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = createStatement();
			statement.setQueryTimeout(30);
			statement.execute("UPDATE "+METAMODEL_ALIAS+".classifier_attributes "+
					" SET classifier_id = '"+at.getClassifierId()+"'"+
					" AND event_attribute_name_id = '"+at.getEventAttributeNameId()+"'"+
					" WHERE id = '"+at.getId()+"'");
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
	public SLEXMMLogResultSet getLogsForProcess(int id) {
		return (SLEXMMLogResultSet) getResultSetFor(SLEXMMLogResultSet.class,
				SLEXMMTables.T_LOG,
				SLEXMMTables.T_PROCESS, new int[] {id}); 
	}

	@Override
	public SLEXMMActivityResultSet getActivitiesForProcess(int id) {
		return (SLEXMMActivityResultSet) getResultSetFor(SLEXMMActivityResultSet.class,
				SLEXMMTables.T_ACTIVITY,
				SLEXMMTables.T_PROCESS, new int[] {id});
	}

	@Override
	public SLEXMMLogResultSet getLogs() {
		SLEXMMLogResultSet lrset = null;
		Statement statement = null;
		try {
			statement = createStatement();
			ResultSet rset = statement.executeQuery("SELECT * FROM "
					+METAMODEL_ALIAS+".log ");
			lrset = new SLEXMMLogResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return lrset; 
	}

	@Override
	public SLEXMMProcessResultSet getProcesses() {
		SLEXMMProcessResultSet prset = null;
		Statement statement = null;
		try {
			statement = createStatement();
			ResultSet rset = statement.executeQuery("SELECT * FROM "
					+METAMODEL_ALIAS+".process ");
			prset = new SLEXMMProcessResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return prset; 
	}

	@Override
	public SLEXMMObjectResultSet getObjectsForDatamodels(int[] is) {
		return (SLEXMMObjectResultSet) getResultSetFor(SLEXMMObjectResultSet.class,
				SLEXMMTables.T_OBJECT,
				SLEXMMTables.T_DATAMODEL, is);
	}

	@Override
	public SLEXMMObjectResultSet getObjectsForLogs(int[] is) {
		return (SLEXMMObjectResultSet) getResultSetFor(SLEXMMObjectResultSet.class,
				SLEXMMTables.T_OBJECT,
				SLEXMMTables.T_LOG, is);
	}

	@Override
	public SLEXMMObjectResultSet getObjectsForProcesses(int[] is) {
		return (SLEXMMObjectResultSet) getResultSetFor(SLEXMMObjectResultSet.class,
				SLEXMMTables.T_OBJECT,
				SLEXMMTables.T_PROCESS, is);
	}

	@Override
	public SLEXMMCaseResultSet getCasesForDatamodels(int[] is) {
		return (SLEXMMCaseResultSet) getResultSetFor(SLEXMMCaseResultSet.class,
				SLEXMMTables.T_CASE,
				SLEXMMTables.T_DATAMODEL, is);
	}

	@Override
	public SLEXMMCaseResultSet getCasesForProcesses(int[] is) {
		return (SLEXMMCaseResultSet) getResultSetFor(SLEXMMCaseResultSet.class,
				SLEXMMTables.T_CASE,
				SLEXMMTables.T_PROCESS, is);
	}

	@Override
	public SLEXMMEventResultSet getEventsForDatamodels(int[] is) {
		return (SLEXMMEventResultSet) getResultSetFor(SLEXMMEventResultSet.class,
				SLEXMMTables.T_EVENT,
				SLEXMMTables.T_DATAMODEL, is);
	}

	@Override
	public SLEXMMEventResultSet getEventsForLogs(int[] is) {
		return (SLEXMMEventResultSet) getResultSetFor(SLEXMMEventResultSet.class,
				SLEXMMTables.T_EVENT,
				SLEXMMTables.T_LOG, is);
	}

	@Override
	public SLEXMMEventResultSet getEventsForProcesses(int[] is) {
		return (SLEXMMEventResultSet) getResultSetFor(SLEXMMEventResultSet.class,
				SLEXMMTables.T_EVENT,
				SLEXMMTables.T_PROCESS, is);
	}

	@Override
	public SLEXMMObjectVersionResultSet getVersionsForDatamodels(int[] is) {
		return (SLEXMMObjectVersionResultSet) getResultSetFor(SLEXMMObjectVersionResultSet.class,
				SLEXMMTables.T_OBJECT_VERSION,
				SLEXMMTables.T_DATAMODEL, is);
	}

	@Override
	public SLEXMMObjectVersionResultSet getVersionsForLogs(int[] is) {
		return (SLEXMMObjectVersionResultSet) getResultSetFor(SLEXMMObjectVersionResultSet.class,
				SLEXMMTables.T_OBJECT_VERSION,
				SLEXMMTables.T_LOG, is);
	}

	@Override
	public SLEXMMObjectVersionResultSet getVersionsForProcesses(int[] is) {
		return (SLEXMMObjectVersionResultSet) getResultSetFor(SLEXMMObjectVersionResultSet.class,
				SLEXMMTables.T_OBJECT_VERSION,
				SLEXMMTables.T_PROCESS, is);
	}

	@Override
	public SLEXMMActivityResultSet getActivitiesForDatamodels(int[] is) {
		return (SLEXMMActivityResultSet) getResultSetFor(SLEXMMActivityResultSet.class,
				SLEXMMTables.T_ACTIVITY,
				SLEXMMTables.T_DATAMODEL, is);
	}

	@Override
	public SLEXMMActivityResultSet getActivitiesForLogs(int[] is) {
		return (SLEXMMActivityResultSet) getResultSetFor(SLEXMMActivityResultSet.class,
				SLEXMMTables.T_ACTIVITY,
				SLEXMMTables.T_LOG, is);
	}

	@Override
	public SLEXMMActivityResultSet getActivitiesForProcesses(int[] is) {
		return (SLEXMMActivityResultSet) getResultSetFor(SLEXMMActivityResultSet.class,
				SLEXMMTables.T_ACTIVITY,
				SLEXMMTables.T_PROCESS, is);
	}

	@Override
	public SLEXMMClassResultSet getClassesForDatamodels(int[] is) {
		return (SLEXMMClassResultSet) getResultSetFor(SLEXMMClassResultSet.class,
				SLEXMMTables.T_CLASS,
				SLEXMMTables.T_DATAMODEL, is);
	}

	@Override
	public SLEXMMClassResultSet getClassesForLogs(int[] is) {
		return (SLEXMMClassResultSet) getResultSetFor(SLEXMMClassResultSet.class,
				SLEXMMTables.T_CLASS,
				SLEXMMTables.T_LOG, is);
	}

	@Override
	public SLEXMMClassResultSet getClassesForProcesses(int[] is) {
		return (SLEXMMClassResultSet) getResultSetFor(SLEXMMClassResultSet.class,
				SLEXMMTables.T_CLASS,
				SLEXMMTables.T_PROCESS, is);
	}

	@Override
	public SLEXMMRelationResultSet getRelationsForDatamodels(int[] is) {
		return (SLEXMMRelationResultSet) getResultSetFor(SLEXMMRelationResultSet.class,
				SLEXMMTables.T_RELATION,
				SLEXMMTables.T_DATAMODEL, is);
	}

	@Override
	public SLEXMMRelationResultSet getRelationsForLogs(int[] is) {
		return (SLEXMMRelationResultSet) getResultSetFor(SLEXMMRelationResultSet.class,
				SLEXMMTables.T_RELATION,
				SLEXMMTables.T_LOG, is);
	}

	@Override
	public SLEXMMRelationResultSet getRelationsForProcesses(int[] is) {
		return (SLEXMMRelationResultSet) getResultSetFor(SLEXMMRelationResultSet.class,
				SLEXMMTables.T_RELATION,
				SLEXMMTables.T_PROCESS, is);
	}

	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForDatamodels(int[] is) {
		return (SLEXMMRelationshipResultSet) getResultSetFor(SLEXMMRelationshipResultSet.class,
				SLEXMMTables.T_RELATIONSHIP,
				SLEXMMTables.T_DATAMODEL, is);
	}

	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForLogs(int[] is) {
		return (SLEXMMRelationshipResultSet) getResultSetFor(SLEXMMRelationshipResultSet.class,
				SLEXMMTables.T_RELATIONSHIP,
				SLEXMMTables.T_LOG, is);
	}

	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForProcesses(int[] is) {
		return (SLEXMMRelationshipResultSet) getResultSetFor(SLEXMMRelationshipResultSet.class,
				SLEXMMTables.T_RELATIONSHIP,
				SLEXMMTables.T_PROCESS, is);
	}

	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForDatamodels(int[] is) {
		return (SLEXMMActivityInstanceResultSet) getResultSetFor(SLEXMMActivityInstanceResultSet.class,
				SLEXMMTables.T_ACTIVITY_INSTANCE,
				SLEXMMTables.T_DATAMODEL, is);
	}

	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForLogs(int[] is) {
		return (SLEXMMActivityInstanceResultSet) getResultSetFor(SLEXMMActivityInstanceResultSet.class,
				SLEXMMTables.T_ACTIVITY_INSTANCE,
				SLEXMMTables.T_LOG, is);
	}

	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForProcesses(int[] is) {
		return (SLEXMMActivityInstanceResultSet) getResultSetFor(SLEXMMActivityInstanceResultSet.class,
				SLEXMMTables.T_ACTIVITY_INSTANCE,
				SLEXMMTables.T_PROCESS, is);
	}

	@Override
	public SLEXMMAttributeResultSet getAttributesForDatamodels(int[] is) {
		return (SLEXMMAttributeResultSet) getResultSetFor(SLEXMMAttributeResultSet.class,
				SLEXMMTables.T_ATTRIBUTE_NAME,
				SLEXMMTables.T_DATAMODEL, is);
	}

	@Override
	public SLEXMMAttributeResultSet getAttributesForLogs(int[] is) {
		return (SLEXMMAttributeResultSet) getResultSetFor(SLEXMMAttributeResultSet.class,
				SLEXMMTables.T_ATTRIBUTE_NAME,
				SLEXMMTables.T_LOG, is);
	}

	@Override
	public SLEXMMAttributeResultSet getAttributesForProcesses(int[] is) {
		return (SLEXMMAttributeResultSet) getResultSetFor(SLEXMMAttributeResultSet.class,
				SLEXMMTables.T_ATTRIBUTE_NAME,
				SLEXMMTables.T_PROCESS, is);
	}

	@Override
	public SLEXMMDataModelResultSet getDatamodelsForObjects(int[] is) {
		return (SLEXMMDataModelResultSet) getResultSetFor(SLEXMMDataModelResultSet.class,
				SLEXMMTables.T_DATAMODEL,
				SLEXMMTables.T_OBJECT, is);
	}

	@Override
	public SLEXMMDataModelResultSet getDatamodelsForEvents(int[] is) {
		return (SLEXMMDataModelResultSet) getResultSetFor(SLEXMMDataModelResultSet.class,
				SLEXMMTables.T_DATAMODEL,
				SLEXMMTables.T_EVENT, is);
	}

	@Override
	public SLEXMMDataModelResultSet getDatamodelsForCases(int[] is) {
		return (SLEXMMDataModelResultSet) getResultSetFor(SLEXMMDataModelResultSet.class,
				SLEXMMTables.T_DATAMODEL,
				SLEXMMTables.T_CASE, is);
	}

	@Override
	public SLEXMMDataModelResultSet getDatamodelsForActivities(int[] is) {
		return (SLEXMMDataModelResultSet) getResultSetFor(SLEXMMDataModelResultSet.class,
				SLEXMMTables.T_DATAMODEL,
				SLEXMMTables.T_ACTIVITY, is);
	}

	@Override
	public SLEXMMDataModelResultSet getDatamodelsForClasses(int[] is) {
		return (SLEXMMDataModelResultSet) getResultSetFor(SLEXMMDataModelResultSet.class,
				SLEXMMTables.T_DATAMODEL,
				SLEXMMTables.T_CLASS, is);
	}

	@Override
	public SLEXMMDataModelResultSet getDatamodelsForRelationships(int[] is) {
		return (SLEXMMDataModelResultSet) getResultSetFor(SLEXMMDataModelResultSet.class,
				SLEXMMTables.T_DATAMODEL,
				SLEXMMTables.T_RELATIONSHIP, is);
	}

	@Override
	public SLEXMMDataModelResultSet getDatamodelsForObjectVersions(int[] is) {
		return (SLEXMMDataModelResultSet) getResultSetFor(SLEXMMDataModelResultSet.class,
				SLEXMMTables.T_DATAMODEL,
				SLEXMMTables.T_OBJECT_VERSION, is);
	}

	@Override
	public SLEXMMDataModelResultSet getDatamodelsForRelations(int[] is) {
		return (SLEXMMDataModelResultSet) getResultSetFor(SLEXMMDataModelResultSet.class,
				SLEXMMTables.T_DATAMODEL,
				SLEXMMTables.T_RELATION, is);
	}

	@Override
	public SLEXMMDataModelResultSet getDatamodelsForActivityInstances(int[] is) {
		return (SLEXMMDataModelResultSet) getResultSetFor(SLEXMMDataModelResultSet.class,
				SLEXMMTables.T_DATAMODEL,
				SLEXMMTables.T_ACTIVITY_INSTANCE, is);
	}

	@Override
	public SLEXMMDataModelResultSet getDatamodelsForAttributes(int[] is) {
		return (SLEXMMDataModelResultSet) getResultSetFor(SLEXMMDataModelResultSet.class,
				SLEXMMTables.T_DATAMODEL,
				SLEXMMTables.T_ATTRIBUTE_NAME, is);
	}

	@Override
	public SLEXMMDataModelResultSet getDatamodelsForLogs(int[] is) {
		return (SLEXMMDataModelResultSet) getResultSetFor(SLEXMMDataModelResultSet.class,
				SLEXMMTables.T_DATAMODEL,
				SLEXMMTables.T_LOG, is);
	}

	@Override
	public SLEXMMDataModelResultSet getDatamodelsForProcesses(int[] is) {
		return (SLEXMMDataModelResultSet) getResultSetFor(SLEXMMDataModelResultSet.class,
				SLEXMMTables.T_DATAMODEL,
				SLEXMMTables.T_PROCESS, is);
	}

	@Override
	public SLEXMMProcessResultSet getProcessesForObjects(int[] is) {
		return (SLEXMMProcessResultSet) getResultSetFor(SLEXMMProcessResultSet.class,
				SLEXMMTables.T_PROCESS,
				SLEXMMTables.T_OBJECT, is);
	}

	@Override
	public SLEXMMProcessResultSet getProcessesForEvents(int[] is) {
		return (SLEXMMProcessResultSet) getResultSetFor(SLEXMMProcessResultSet.class,
				SLEXMMTables.T_PROCESS,
				SLEXMMTables.T_EVENT, is);
	}

	@Override
	public SLEXMMProcessResultSet getProcessesForCases(int[] is) {
		return (SLEXMMProcessResultSet) getResultSetFor(SLEXMMProcessResultSet.class,
				SLEXMMTables.T_PROCESS,
				SLEXMMTables.T_CASE, is);
	}

	@Override
	public SLEXMMProcessResultSet getProcessesForActivities(int[] is) {
		return (SLEXMMProcessResultSet) getResultSetFor(SLEXMMProcessResultSet.class,
				SLEXMMTables.T_PROCESS,
				SLEXMMTables.T_ACTIVITY, is);
	}

	@Override
	public SLEXMMProcessResultSet getProcessesForClasses(int[] is) {
		return (SLEXMMProcessResultSet) getResultSetFor(SLEXMMProcessResultSet.class,
				SLEXMMTables.T_PROCESS,
				SLEXMMTables.T_CLASS, is);
	}

	@Override
	public SLEXMMProcessResultSet getProcessesForRelationships(int[] is) {
		return (SLEXMMProcessResultSet) getResultSetFor(SLEXMMProcessResultSet.class,
				SLEXMMTables.T_PROCESS,
				SLEXMMTables.T_RELATIONSHIP, is);
	}

	@Override
	public SLEXMMProcessResultSet getProcessesForObjectVersions(int[] is) {
		return (SLEXMMProcessResultSet) getResultSetFor(SLEXMMProcessResultSet.class,
				SLEXMMTables.T_PROCESS,
				SLEXMMTables.T_OBJECT_VERSION, is);
	}

	@Override
	public SLEXMMProcessResultSet getProcessesForRelations(int[] is) {
		return (SLEXMMProcessResultSet) getResultSetFor(SLEXMMProcessResultSet.class,
				SLEXMMTables.T_PROCESS,
				SLEXMMTables.T_RELATION, is);
	}

	@Override
	public SLEXMMProcessResultSet getProcessesForActivityInstances(int[] is) {
		return (SLEXMMProcessResultSet) getResultSetFor(SLEXMMProcessResultSet.class,
				SLEXMMTables.T_PROCESS,
				SLEXMMTables.T_ACTIVITY_INSTANCE, is);
	}

	@Override
	public SLEXMMProcessResultSet getProcessesForAttributes(int[] is) {
		return (SLEXMMProcessResultSet) getResultSetFor(SLEXMMProcessResultSet.class,
				SLEXMMTables.T_PROCESS,
				SLEXMMTables.T_ATTRIBUTE_NAME, is);
	}

	@Override
	public SLEXMMProcessResultSet getProcessesForDatamodels(int[] is) {
		return (SLEXMMProcessResultSet) getResultSetFor(SLEXMMProcessResultSet.class,
				SLEXMMTables.T_PROCESS,
				SLEXMMTables.T_DATAMODEL, is);
	}

	@Override
	public SLEXMMProcessResultSet getProcessesForLogs(int[] is) {
		return (SLEXMMProcessResultSet) getResultSetFor(SLEXMMProcessResultSet.class,
				SLEXMMTables.T_PROCESS,
				SLEXMMTables.T_LOG, is);
	}

	@Override
	public SLEXMMLogResultSet getLogsForObjects(int[] is) {
		return (SLEXMMLogResultSet) getResultSetFor(SLEXMMLogResultSet.class,
				SLEXMMTables.T_LOG,
				SLEXMMTables.T_OBJECT, is);
	}

	@Override
	public SLEXMMLogResultSet getLogsForEvents(int[] is) {
		return (SLEXMMLogResultSet) getResultSetFor(SLEXMMLogResultSet.class,
				SLEXMMTables.T_LOG,
				SLEXMMTables.T_EVENT, is);
	}

	@Override
	public SLEXMMLogResultSet getLogsForCases(int[] is) {
		return (SLEXMMLogResultSet) getResultSetFor(SLEXMMLogResultSet.class,
				SLEXMMTables.T_LOG,
				SLEXMMTables.T_CASE, is);
	}

	@Override
	public SLEXMMLogResultSet getLogsForActivities(int[] is) {
		return (SLEXMMLogResultSet) getResultSetFor(SLEXMMLogResultSet.class,
				SLEXMMTables.T_LOG,
				SLEXMMTables.T_ACTIVITY, is);
	}

	@Override
	public SLEXMMLogResultSet getLogsForClasses(int[] is) {
		return (SLEXMMLogResultSet) getResultSetFor(SLEXMMLogResultSet.class,
				SLEXMMTables.T_LOG,
				SLEXMMTables.T_CLASS, is);
	}

	@Override
	public SLEXMMLogResultSet getLogsForRelationships(int[] is) {
		return (SLEXMMLogResultSet) getResultSetFor(SLEXMMLogResultSet.class,
				SLEXMMTables.T_LOG,
				SLEXMMTables.T_RELATIONSHIP, is);
	}

	@Override
	public SLEXMMLogResultSet getLogsForObjectVersions(int[] is) {
		return (SLEXMMLogResultSet) getResultSetFor(SLEXMMLogResultSet.class,
				SLEXMMTables.T_LOG,
				SLEXMMTables.T_OBJECT_VERSION, is);
	}

	@Override
	public SLEXMMLogResultSet getLogsForRelations(int[] is) {
		return (SLEXMMLogResultSet) getResultSetFor(SLEXMMLogResultSet.class,
				SLEXMMTables.T_LOG,
				SLEXMMTables.T_RELATION, is);
	}

	@Override
	public SLEXMMLogResultSet getLogsForActivityInstances(int[] is) {
		return (SLEXMMLogResultSet) getResultSetFor(SLEXMMLogResultSet.class,
				SLEXMMTables.T_LOG,
				SLEXMMTables.T_ACTIVITY_INSTANCE, is);
	}

	@Override
	public SLEXMMLogResultSet getLogsForAttributes(int[] is) {
		return (SLEXMMLogResultSet) getResultSetFor(SLEXMMLogResultSet.class,
				SLEXMMTables.T_LOG,
				SLEXMMTables.T_ATTRIBUTE_NAME, is);
	}

	@Override
	public SLEXMMLogResultSet getLogsForDatamodels(int[] is) {
		return (SLEXMMLogResultSet) getResultSetFor(SLEXMMLogResultSet.class,
				SLEXMMTables.T_LOG,
				SLEXMMTables.T_DATAMODEL, is);
	}

	@Override
	public SLEXMMLogResultSet getLogsForProcesses(int[] is) {
		return (SLEXMMLogResultSet) getResultSetFor(SLEXMMLogResultSet.class,
				SLEXMMTables.T_LOG,
				SLEXMMTables.T_PROCESS, is);
	}

	@Override
	public SLEXMMObjectResultSet getObjectsForPeriod(SLEXMMPeriod p) {
		return (SLEXMMObjectResultSet) getResultSetForPeriod(SLEXMMObjectResultSet.class,
				SLEXMMTables.T_OBJECT, p);
	}

	@Override
	public SLEXMMCaseResultSet getCasesForPeriod(SLEXMMPeriod p) {
		return (SLEXMMCaseResultSet) getResultSetForPeriod(SLEXMMCaseResultSet.class,
				SLEXMMTables.T_CASE, p);
	}

	@Override
	public SLEXMMEventResultSet getEventsForPeriod(SLEXMMPeriod p) {
		return (SLEXMMEventResultSet) getResultSetForPeriod(SLEXMMEventResultSet.class,
				SLEXMMTables.T_EVENT, p);
	}

	@Override
	public SLEXMMObjectVersionResultSet getVersionsForPeriod(SLEXMMPeriod p) {
		return (SLEXMMObjectVersionResultSet) getResultSetForPeriod(SLEXMMObjectVersionResultSet.class,
				SLEXMMTables.T_OBJECT_VERSION, p);
	}

	@Override
	public SLEXMMActivityResultSet getActivitiesForPeriod(SLEXMMPeriod p) {
		return (SLEXMMActivityResultSet) getResultSetForPeriod(SLEXMMActivityResultSet.class,
				SLEXMMTables.T_ACTIVITY, p);
	}

	@Override
	public SLEXMMClassResultSet getClassesForPeriod(SLEXMMPeriod p) {
		return (SLEXMMClassResultSet) getResultSetForPeriod(SLEXMMClassResultSet.class,
				SLEXMMTables.T_CLASS, p);
	}

	@Override
	public SLEXMMRelationResultSet getRelationsForPeriod(SLEXMMPeriod p) {
		return (SLEXMMRelationResultSet) getResultSetForPeriod(SLEXMMRelationResultSet.class,
				SLEXMMTables.T_RELATION, p);
	}

	@Override
	public SLEXMMRelationshipResultSet getRelationshipsForPeriod(SLEXMMPeriod p) {
		return (SLEXMMRelationshipResultSet) getResultSetForPeriod(SLEXMMRelationshipResultSet.class,
				SLEXMMTables.T_RELATIONSHIP, p);
	}

	@Override
	public SLEXMMActivityInstanceResultSet getActivityInstancesForPeriod(SLEXMMPeriod p) {
		return (SLEXMMActivityInstanceResultSet) getResultSetForPeriod(SLEXMMActivityInstanceResultSet.class,
				SLEXMMTables.T_ACTIVITY_INSTANCE, p);
	}

	@Override
	public SLEXMMAttributeResultSet getAttributesForPeriod(SLEXMMPeriod p) {
		return (SLEXMMAttributeResultSet) getResultSetForPeriod(SLEXMMAttributeResultSet.class,
				SLEXMMTables.T_ATTRIBUTE_NAME, p);
	}

	@Override
	public SLEXMMDataModelResultSet getDatamodelsForPeriod(SLEXMMPeriod p) {
		return (SLEXMMDataModelResultSet) getResultSetForPeriod(SLEXMMDataModelResultSet.class,
				SLEXMMTables.T_DATAMODEL, p);
	}

	@Override
	public SLEXMMProcessResultSet getProcessesForPeriod(SLEXMMPeriod p) {
		return (SLEXMMProcessResultSet) getResultSetForPeriod(SLEXMMProcessResultSet.class,
				SLEXMMTables.T_PROCESS, p);
	}

	@Override
	public SLEXMMLogResultSet getLogsForPeriod(SLEXMMPeriod p) {
		return (SLEXMMLogResultSet) getResultSetForPeriod(SLEXMMLogResultSet.class,
				SLEXMMTables.T_LOG, p);
	}

	@Override
	public SLEXMMPeriodResultSet getPeriodsForObjects(int[] is) {
		return (SLEXMMPeriodResultSet) getPeriodsFor(
				SLEXMMTables.T_OBJECT, is);
	}
	
	@Override
	public SLEXMMPeriodResultSet getPeriodsForEvents(int[] is) {
		return (SLEXMMPeriodResultSet) getPeriodsFor(
				SLEXMMTables.T_EVENT, is);
	}

	@Override
	public SLEXMMPeriodResultSet getPeriodsForCases(int[] is) {
		return (SLEXMMPeriodResultSet) getPeriodsFor(
				SLEXMMTables.T_CASE, is);
	}

	@Override
	public SLEXMMPeriodResultSet getPeriodsForActivities(int[] is) {
		return (SLEXMMPeriodResultSet) getPeriodsFor(
				SLEXMMTables.T_ACTIVITY, is);
	}

	@Override
	public SLEXMMPeriodResultSet getPeriodsForClasses(int[] is) {
		return (SLEXMMPeriodResultSet) getPeriodsFor(
				SLEXMMTables.T_CLASS, is);
	}

	@Override
	public SLEXMMPeriodResultSet getPeriodsForRelationships(int[] is) {
		return (SLEXMMPeriodResultSet) getPeriodsFor(
				SLEXMMTables.T_RELATIONSHIP, is);
	}

	@Override
	public SLEXMMPeriodResultSet getPeriodsForVersions(int[] is) {
		return (SLEXMMPeriodResultSet) getPeriodsFor(
				SLEXMMTables.T_OBJECT_VERSION, is);
	}

	@Override
	public SLEXMMPeriodResultSet getPeriodsForRelations(int[] is) {
		return (SLEXMMPeriodResultSet) getPeriodsFor(
				SLEXMMTables.T_RELATION, is);
	}

	@Override
	public SLEXMMPeriodResultSet getPeriodsForActivityInstances(int[] is) {
		return (SLEXMMPeriodResultSet) getPeriodsFor(
				SLEXMMTables.T_ACTIVITY_INSTANCE, is);
	}

	@Override
	public SLEXMMPeriodResultSet getPeriodsForAttributes(int[] is) {
		return (SLEXMMPeriodResultSet) getPeriodsFor(
				SLEXMMTables.T_ATTRIBUTE_NAME, is);
	}

	@Override
	public SLEXMMPeriodResultSet getPeriodsForDatamodels(int[] is) {
		return (SLEXMMPeriodResultSet) getPeriodsFor(
				SLEXMMTables.T_DATAMODEL, is);
	}

	@Override
	public SLEXMMPeriodResultSet getPeriodsForLogs(int[] is) {
		return (SLEXMMPeriodResultSet) getPeriodsFor(
				SLEXMMTables.T_LOG, is);
	}

	@Override
	public SLEXMMPeriodResultSet getPeriodsForProcesses(int[] is) {
		return (SLEXMMPeriodResultSet) getPeriodsFor(
				SLEXMMTables.T_PROCESS, is);
	}

	@Override
	public SLEXMMLog createLog(int process_id, String name) {
		SLEXMMLog l = new SLEXMMLog(this);
		l.setName(name);
		l.setProcessId(process_id);
		if (isAutoCommitOnCreationEnabled()) {
			l.commit();
		}
		return l;
	}
	
	public AbstractRSetElement<?> getResultSetFor(Class<?> rsetClass, SLEXMMTables tableA, SLEXMMTables tableB, int[] idsB) {
		
		List<List<SLEXMMEdge>> paths = slxmmstrqgen.getPaths(tableA, tableB);
				
		String query = slxmmstrqgen.getSelectQuery(paths, idsB);
		
		AbstractRSetElement<?> arset = null;
		Statement statement = null;
		
		try {
			statement = createStatement();
			ResultSet rset = statement.executeQuery(query);
			arset = (AbstractRSetElement<?>) rsetClass.
					getConstructor(SLEXMMStorageMetaModel.class,ResultSet.class).
					newInstance(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return arset;
		
	}
	
	public AbstractRSetElement<?> getResultSetForPeriod(Class<? extends AbstractRSetElement<?>> rsetClass, SLEXMMTables tableA, SLEXMMPeriod p) {
		
		SLEXMMTables tableB = null;
		
		switch (tableA) {
		case T_DATAMODEL:
		case T_CLASS:
		case T_OBJECT:
		case T_OBJECT_VERSION:
		case T_ATTRIBUTE_NAME:
		case T_ATTRIBUTE_VALUE:
			tableB = SLEXMMTables.T_OBJECT_VERSION;
			break;
		case T_RELATIONSHIP:
		case T_RELATION:
			tableB = SLEXMMTables.T_RELATION;
			break;
		default:
			tableB = SLEXMMTables.T_EVENT;
		}
		
		List<List<SLEXMMEdge>> paths = slxmmstrqgen.getPaths(tableA, tableB);
		
		String query = slxmmstrqgen.getSelectQueryForPeriod(paths, p);
		
		AbstractRSetElement<?> arset = null;
		Statement statement = null;
		
		try {
			statement = createStatement();
			ResultSet rset = statement.executeQuery(query);
			arset = rsetClass.
					getConstructor(SLEXMMStorageMetaModel.class,ResultSet.class).
					newInstance(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return arset;
		
	}
	
	public SLEXMMPeriodResultSet getPeriodsFor(SLEXMMTables tableB, int[] idsB) {
		
		SLEXMMTables tableA = null;
		
		switch (tableB) {
		case T_DATAMODEL:
		case T_CLASS:
		case T_OBJECT:
		case T_OBJECT_VERSION:
		case T_ATTRIBUTE_NAME:
		case T_ATTRIBUTE_VALUE:
			tableA = SLEXMMTables.T_OBJECT_VERSION;
			break;
		case T_RELATIONSHIP:
		case T_RELATION:
			tableA = SLEXMMTables.T_RELATION;
			break;
		default:
			tableA = SLEXMMTables.T_EVENT;
		}

		String query = slxmmstrqgen.getPeriodsQuery(slxmmstrqgen.getPaths(tableA, tableB), idsB);
		
		SLEXMMPeriodResultSet prset = null;
		Statement statement = null;
		
		try {
			statement = createStatement();
			ResultSet rset = statement.executeQuery(query);
			prset = new SLEXMMPeriodResultSet(this, rset);
		} catch (Exception e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		
		return prset;
		
	}
	
	public SLEXMMEventResultSet getEventsAndAttributeValues(Set<SLEXMMEvent> set) {
		
		int[] ids = new int[set.size()];
		
		int i = 0;
		for (SLEXMMEvent e : set) {
			ids[i] = e.getId();
			i++;
		}
		
		return getEventsAndAttributeValues(ids);
	}
	
	public SLEXMMEventResultSet getEventsAndAttributeValues(int[] ids) {
		SLEXMMEventResultSet erset = null;
		Statement statement = null;
		try {
			statement = createStatement();
			String query = "SELECT EV.*, EVAT.id as atId, "
					+ " EVAT.name as atName,"
					+ " EVATV.value as atValue, "
					+ " EVATV.type as atType, "
					+ " EVATV.id as atvId "
					+ " FROM "
					+METAMODEL_ALIAS+".event as EV "
					+ " LEFT OUTER JOIN "
					+METAMODEL_ALIAS+".event_attribute_value as EVATV "
					+ " ON EV.id = EVATV.event_id "
					+ " LEFT OUTER JOIN "
					+METAMODEL_ALIAS+".event_attribute_name as EVAT "
					+ " ON EVATV.event_attribute_name_id = EVAT.id "
					+ " WHERE EV.id IN ("+buildStringFromArray(ids)+") "
					+" ORDER BY EV.id ";
			ResultSet rset = statement.executeQuery(query);
			erset = new SLEXMMEventResultSet(this, rset);
		} catch (Exception ex) {
			ex.printStackTrace();
			closeStatement(statement);
		}
		
		return erset;
	}
	
	public SLEXMMEventResultSet getAllEventsAndAttributeValues() {
		SLEXMMEventResultSet erset = null;
		Statement statement = null;
		try {
			statement = createStatement();
			String query = "SELECT EV.*, EVAT.id as atId, "
					+ " EVAT.name as atName,"
					+ " EVATV.value as atValue, "
					+ " EVATV.type as atType, "
					+ " EVATV.id as atvId "
					+ " FROM "
					+METAMODEL_ALIAS+".event as EV "
					+ " LEFT OUTER JOIN "
					+METAMODEL_ALIAS+".event_attribute_value as EVATV "
					+ " ON EV.id = EVATV.event_id "
					+ " LEFT OUTER JOIN "
					+METAMODEL_ALIAS+".event_attribute_name as EVAT "
					+ " ON EVATV.event_attribute_name_id = EVAT.id "
					+" ORDER BY EV.id ";
			ResultSet rset = statement.executeQuery(query);
			erset = new SLEXMMEventResultSet(this, rset);
		} catch (Exception ex) {
			ex.printStackTrace();
			closeStatement(statement);
		}
		
		return erset;
	}
	
	public SLEXMMObjectVersionResultSet getVersionsAndAttributeValues(Set<SLEXMMObjectVersion> set) {
		
		int[] ids = new int[set.size()];
		
		int i = 0;
		for (SLEXMMObjectVersion e : set) {
			ids[i] = e.getId();
			i++;
		}
		
		return getVersionsAndAttributeValues(ids);
	}
	
	public SLEXMMObjectVersionResultSet getAllVersionsAndAttributeValues() {
		SLEXMMObjectVersionResultSet ovrset = null;
		Statement statement = null;
		try {
			statement = createStatement();
			String query = "SELECT OV.*, OVAT.id as atId, "
					+ " OVAT.name as atName,"
					+ " OVATV.value as atValue, "
					+ " OVATV.type as atType, "
					+ " OVATV.id as atvId "
					+ " FROM "
					+METAMODEL_ALIAS+".object_version as OV "
					+ " LEFT OUTER JOIN "
					+METAMODEL_ALIAS+".attribute_value as OVATV "
					+ " ON OV.id = OVATV.object_version_id "
					+ " LEFT OUTER JOIN "
					+METAMODEL_ALIAS+".attribute_name as OVAT "
					+ " ON OVATV.attribute_name_id = OVAT.id "
					+" ORDER BY OV.id ";
			ResultSet rset = statement.executeQuery(query);
			ovrset = new SLEXMMObjectVersionResultSet(this, rset);
		} catch (Exception ex) {
			ex.printStackTrace();
			closeStatement(statement);
		}
		
		return ovrset;
	}
	
	public SLEXMMObjectVersionResultSet getVersionsAndAttributeValues(int[] ids) {
		SLEXMMObjectVersionResultSet ovrset = null;
		Statement statement = null;
		try {
			statement = createStatement();
			String query = "SELECT OV.*, OVAT.id as atId, "
					+ " OVAT.name as atName,"
					+ " OVATV.value as atValue, "
					+ " OVATV.type as atType, "
					+ " OVATV.id as atvId "
					+ " FROM "
					+METAMODEL_ALIAS+".object_version as OV "
					+ " LEFT OUTER JOIN "
					+METAMODEL_ALIAS+".attribute_value as OVATV "
					+ " ON OV.id = OVATV.object_version_id "
					+ " LEFT OUTER JOIN "
					+METAMODEL_ALIAS+".attribute_name as OVAT "
					+ " ON OVATV.attribute_name_id = OVAT.id "
					+ " WHERE OV.id IN ("+buildStringFromArray(ids)+") "
					+" ORDER BY OV.id ";
			ResultSet rset = statement.executeQuery(query);
			ovrset = new SLEXMMObjectVersionResultSet(this, rset);
		} catch (Exception ex) {
			ex.printStackTrace();
			closeStatement(statement);
		}
		
		return ovrset;
	}
	
	
	
	public SLEXMMCaseResultSet getCasesAndAttributeValues(Set<SLEXMMCase> set) {
		
		int[] ids = new int[set.size()];
		
		int i = 0;
		for (SLEXMMCase c : set) {
			ids[i] = c.getId();
			i++;
		}
		
		return getCasesAndAttributeValues(ids);
	}
	
	public SLEXMMCaseResultSet getAllCasesAndAttributeValues() {
		SLEXMMCaseResultSet crset = null;
		Statement statement = null;
		try {
			statement = createStatement();
			String query = "SELECT C.*, CAT.id as atId, "
					+ " CAT.name as atName,"
					+ " CATV.value as atValue, "
					+ " CATV.type as atType, "
					+ " CATV.id as atvId "
					+ " FROM "
					+METAMODEL_ALIAS+".'case' as C "
					+ " LEFT OUTER JOIN "
					+METAMODEL_ALIAS+".case_attribute_value as CATV "
					+ " ON C.id = CATV.case_id "
					+ " LEFT OUTER JOIN "
					+METAMODEL_ALIAS+".case_attribute_name as CAT "
					+ " ON CATV.case_attribute_name_id = CAT.id "
					+" ORDER BY C.id ";
			ResultSet rset = statement.executeQuery(query);
			crset = new SLEXMMCaseResultSet(this, rset);
		} catch (Exception ex) {
			ex.printStackTrace();
			closeStatement(statement);
		}
		
		return crset;
	}
	
	public SLEXMMCaseResultSet getCasesAndAttributeValues(int[] ids) {
		SLEXMMCaseResultSet crset = null;
		Statement statement = null;
		try {
			statement = createStatement();
			String query = "SELECT C.*, CAT.id as atId, "
					+ " CAT.name as atName,"
					+ " CATV.value as atValue, "
					+ " CATV.type as atType, "
					+ " CATV.id as atvId "
					+ " FROM "
					+METAMODEL_ALIAS+".'case' as C "
					+ " LEFT OUTER JOIN "
					+METAMODEL_ALIAS+".case_attribute_value as CATV "
					+ " ON C.id = CATV.case_id "
					+ " LEFT OUTER JOIN "
					+METAMODEL_ALIAS+".case_attribute_name as CAT "
					+ " ON CATV.case_attribute_name_id = CAT.id "
					+ " WHERE C.id IN ("+buildStringFromArray(ids)+") "
					+" ORDER BY C.id ";
			ResultSet rset = statement.executeQuery(query);
			crset = new SLEXMMCaseResultSet(this, rset);
		} catch (Exception ex) {
			ex.printStackTrace();
			closeStatement(statement);
		}
		
		return crset;
	}
	
	public SLEXMMLogResultSet getLogsAndAttributeValues(Set<SLEXMMLog> set) {
		
		int[] ids = new int[set.size()];
		
		int i = 0;
		for (SLEXMMLog c : set) {
			ids[i] = c.getId();
			i++;
		}
		
		return getLogsAndAttributeValues(ids);
	}
	
	public SLEXMMLogResultSet getAllLogsAndAttributeValues() {
		SLEXMMLogResultSet lrset = null;
		Statement statement = null;
		try {
			statement = createStatement();
			String query = "SELECT L.*, LAT.id as atId, "
					+ " LAT.name as atName,"
					+ " LATV.value as atValue, "
					+ " LATV.type as atType, "
					+ " LATV.id as atvId "
					+ " FROM "
					+METAMODEL_ALIAS+".log as L "
					+ " LEFT OUTER JOIN "
					+METAMODEL_ALIAS+".log_attribute_value as LATV "
					+ " ON L.id = LATV.log_id "
					+ " LEFT OUTER JOIN "
					+METAMODEL_ALIAS+".log_attribute_name as LAT "
					+ " ON LATV.log_attribute_name_id = LAT.id "
					+" ORDER BY L.id ";
			ResultSet rset = statement.executeQuery(query);
			lrset = new SLEXMMLogResultSet(this, rset);
		} catch (Exception ex) {
			ex.printStackTrace();
			closeStatement(statement);
		}
		
		return lrset;
	}
	
	public SLEXMMLogResultSet getLogsAndAttributeValues(int[] ids) {
		SLEXMMLogResultSet lrset = null;
		Statement statement = null;
		try {
			statement = createStatement();
			String query = "SELECT L.*, LAT.id as atId, "
					+ " LAT.name as atName,"
					+ " LATV.value as atValue, "
					+ " LATV.type as atType, "
					+ " LATV.id as atvId "
					+ " FROM "
					+METAMODEL_ALIAS+".log as L "
					+ " LEFT OUTER JOIN "
					+METAMODEL_ALIAS+".log_attribute_value as LATV "
					+ " ON L.id = LATV.log_id "
					+ " LEFT OUTER JOIN "
					+METAMODEL_ALIAS+".log_attribute_name as LAT "
					+ " ON LATV.log_attribute_name_id = LAT.id "
					+ " WHERE L.id IN ("+buildStringFromArray(ids)+") "
					+" ORDER BY L.id ";
			ResultSet rset = statement.executeQuery(query);
			lrset = new SLEXMMLogResultSet(this, rset);
		} catch (Exception ex) {
			ex.printStackTrace();
			closeStatement(statement);
		}
		
		return lrset;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends AbstractDBElement> T getFromCache(Class<?> c, int id) {
		String uniqueId = AbstractDBElement.computeUniqueId(c, id);
		if (objectRepo.containsKey(uniqueId)) {
			AbstractDBElement o = objectRepo.get(uniqueId);
			return (T) o;
		} else {
			return null;
		}
	}
	
	@Override
	public void putInCache(AbstractDBElement o) {
		objectRepo.put(o.getUniqueId(), o);
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HashMap<AbstractAttDBElement, AbstractDBElementWithValue> getAttsFromCache(
			Class<? extends AbstractDBElementWithAtts> c, int id) {
		String uniqueId = AbstractDBElement.computeUniqueId(c, id);
		if (attsRepo.containsKey(uniqueId)) {
			HashMap o = attsRepo.get(uniqueId);
			return o;
		} else {
			return null;
		}
	}
	
	@Override
	public void putAttsInCache(AbstractDBElement o, HashMap<AbstractAttDBElement, AbstractDBElementWithValue> map) {
		attsRepo.put(o.getUniqueId(), map);
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HashMap<String, AbstractAttDBElement> getAttNamesFromCache(Class<? extends AbstractDBElementWithAtts> c,
			int id) {
		String uniqueId = AbstractDBElement.computeUniqueId(c, id);
		if (attNamesRepo.containsKey(uniqueId)) {
			HashMap o = attNamesRepo.get(uniqueId);
			return o;
		} else {
			return null;
		}
	}
	
	@Override
	public void putAttNamesInCache(AbstractDBElement o, HashMap<String, AbstractAttDBElement> map) {
		attNamesRepo.put(o.getUniqueId(), map);
	}
	
}
