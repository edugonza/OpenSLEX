/*
 * 
 */
package org.processmining.openslex.metamodel;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * The Interface SLEXMMStorage.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public interface SLEXMMStorage {

	/** The Constant TYPE_METAMODEL. */
	public static final int TYPE_METAMODEL = 4;
	
	/** The Constant COMMON_CLASS_NAME. */
	public static final String COMMON_CLASS_NAME = "COMMON";
	
	/** The Constant METAMODEL_FILE_EXTENSION. */
	public static final String METAMODEL_FILE_EXTENSION = ".slexmm";

	/** The Constant PATH. */
	public static final String PATH = "data";
	
	/**
	 * Gets the filename.
	 *
	 * @return the filename
	 */
	public abstract String getFilename();

	/**
	 * Gets the path.
	 *
	 * @return the path
	 */
	public abstract String getPath();

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public abstract int getType();

	/**
	 * Sets the auto commit.
	 *
	 * @param autoCommit the new auto commit
	 */
	public abstract void setAutoCommit(boolean autoCommit);

	/**
	 * Commit.
	 */
	public abstract void commit();

	/**
	 * Disconnect.
	 */
	public abstract void disconnect();

	/**
	 * Sets the auto commit on creation.
	 *
	 * @param flag the new auto commit on creation
	 */
	public abstract void setAutoCommitOnCreation(boolean flag);

	/**
	 * Checks if is auto commit on creation enabled.
	 *
	 * @return true, if is auto commit on creation enabled
	 */
	public abstract boolean isAutoCommitOnCreationEnabled();

	/**
	 * Abort.
	 */
	public abstract void abort();

	/**
	 * Close statement.
	 *
	 * @param statement the statement
	 */
	public abstract void closeStatement(Statement statement);

	/**
	 * Close result set.
	 *
	 * @param rset the rset
	 */
	public abstract void closeResultSet(ResultSet rset);
	
	public abstract <T extends AbstractDBElement> T getFromCache(Class<?> c, int id);
	
	public abstract void putInCache(AbstractDBElement o);

	public abstract HashMap getAttsFromCache(Class<?> c, int id);
	
	public abstract void putAttsInCache(AbstractDBElement o, HashMap map);

	public abstract HashMap getAttNamesFromCache(Class<?> c, int id);
	
	public abstract void putAttNamesInCache(AbstractDBElement o, HashMap map);

}