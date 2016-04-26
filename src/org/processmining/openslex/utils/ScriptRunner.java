/*
 * 
 */
package org.processmining.openslex.utils;

/*
 * Slightly modified version of the com.ibatis.common.jdbc.ScriptRunner class
 * from the iBATIS Apache project. Only removed dependency on Resource class
 * and a constructor
 */
/*
 *  Copyright 2004 Clinton Begin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
 
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.*;
 
// TODO: Auto-generated Javadoc
/**
 * The Class ScriptRunner.
 *
 * @author <a href="mailto:e.gonzalez@tue.nl">Eduardo Gonzalez Lopez de Murillas</a>
 * @see <a href="https://www.win.tue.nl/~egonzale/projects/openslex/" target="_blank">OpenSLEX</a>
 */
public class ScriptRunner {
 
        /** The Constant DEFAULT_DELIMITER. */
        private static final String DEFAULT_DELIMITER = ";";
 
        /** The connection. */
        private Connection connection;
 
        /** The stop on error. */
        private boolean stopOnError;
        
        /** The auto commit. */
        private boolean autoCommit;
 
        /** The log writer. */
        private PrintWriter logWriter = new PrintWriter(System.out);
        
        /** The error log writer. */
        private PrintWriter errorLogWriter = new PrintWriter(System.err);
 
        /** The delimiter. */
        private String delimiter = DEFAULT_DELIMITER;
        
        /** The full line delimiter. */
        private boolean fullLineDelimiter = false;
 
        /**
         * Instantiates a new script runner.
         *
         * @param connection the connection
         * @param autoCommit the auto commit
         * @param stopOnError the stop on error
         */
        public ScriptRunner(Connection connection, boolean autoCommit,
                        boolean stopOnError) {
                this.connection = connection;
                this.autoCommit = autoCommit;
                this.stopOnError = stopOnError;
        }
 
        /**
         * Sets the delimiter.
         *
         * @param delimiter the delimiter
         * @param fullLineDelimiter the full line delimiter
         */
        public void setDelimiter(String delimiter, boolean fullLineDelimiter) {
                this.delimiter = delimiter;
                this.fullLineDelimiter = fullLineDelimiter;
        }
 
        /**
         * Sets the log writer.
         *
         * @param logWriter the new log writer
         */
        public void setLogWriter(PrintWriter logWriter) {
                this.logWriter = logWriter;
        }
 
        /**
         * Sets the error log writer.
         *
         * @param errorLogWriter the new error log writer
         */
        public void setErrorLogWriter(PrintWriter errorLogWriter) {
                this.errorLogWriter = errorLogWriter;
        }
 
        /**
         * Run script.
         *
         * @param reader the reader
         * @throws IOException Signals that an I/O exception has occurred.
         * @throws SQLException the SQL exception
         */
        public void runScript(Reader reader) throws IOException, SQLException {
                try {
                        boolean originalAutoCommit = connection.getAutoCommit();
                        try {
                                if (originalAutoCommit != this.autoCommit) {
                                        connection.setAutoCommit(this.autoCommit);
                                }
                                runScript(connection, reader);
                        } finally {
                                connection.setAutoCommit(originalAutoCommit);
                        }
                } catch (IOException e) {
                        throw e;
                } catch (SQLException e) {
                        throw e;
                } catch (Exception e) {
                        throw new RuntimeException("Error running script.  Cause: " + e, e);
                }
        }
 
        /**
         * Run script.
         *
         * @param conn the conn
         * @param reader the reader
         * @throws IOException Signals that an I/O exception has occurred.
         * @throws SQLException the SQL exception
         */
        private void runScript(Connection conn, Reader reader) throws IOException,
                        SQLException {
                StringBuffer command = null;
                try {
                        LineNumberReader lineReader = new LineNumberReader(reader);
                        String line = null;
                        while ((line = lineReader.readLine()) != null) {
                                if (command == null) {
                                        command = new StringBuffer();
                                }
                                String trimmedLine = line.trim();
                                if (trimmedLine.startsWith("--")) {
                                        println(trimmedLine);
                                } else if (trimmedLine.length() < 1
                                                || trimmedLine.startsWith("//")) {
                                        // Do nothing
                                } else if (trimmedLine.length() < 1
                                                || trimmedLine.startsWith("--")) {
                                        // Do nothing
                                } else if (!fullLineDelimiter
                                                && trimmedLine.endsWith(getDelimiter())
                                                || fullLineDelimiter
                                                && trimmedLine.equals(getDelimiter())) {
                                        command.append(line.substring(0, line
                                                        .lastIndexOf(getDelimiter())));
                                        command.append(" ");
                                        Statement statement = conn.createStatement();
 
                                        println(command);
 
                                        boolean hasResults = false;
                                        if (stopOnError) {
                                                hasResults = statement.execute(command.toString());
                                        } else {
                                                try {
                                                        statement.execute(command.toString());
                                                } catch (SQLException e) {
                                                        e.fillInStackTrace();
                                                        printlnError("Error executing: " + command);
                                                        printlnError(e);
                                                }
                                        }
 
                                        if (autoCommit && !conn.getAutoCommit()) {
                                                conn.commit();
                                        }
 
                                        ResultSet rs = statement.getResultSet();
                                        if (hasResults && rs != null) {
                                                ResultSetMetaData md = rs.getMetaData();
                                                int cols = md.getColumnCount();
                                                for (int i = 0; i < cols; i++) {
                                                        String name = md.getColumnLabel(i);
                                                        print(name + "\t");
                                                }
                                                println("");
                                                while (rs.next()) {
                                                        for (int i = 0; i < cols; i++) {
                                                                String value = rs.getString(i);
                                                                print(value + "\t");
                                                        }
                                                        println("");
                                                }
                                        }
 
                                        command = null;
                                        try {
                                                statement.close();
                                        } catch (Exception e) {
                                                // Ignore to workaround a bug in Jakarta DBCP
                                        }
                                        Thread.yield();
                                } else {
                                        command.append(line);
                                        command.append(" ");
                                }
                        }
                        if (!autoCommit) {
                                conn.commit();
                        }
                } catch (SQLException e) {
                        e.fillInStackTrace();
                        printlnError("Error executing: " + command);
                        printlnError(e);
                        throw e;
                } catch (IOException e) {
                        e.fillInStackTrace();
                        printlnError("Error executing: " + command);
                        printlnError(e);
                        throw e;
                } finally {
                        conn.rollback();
                        flush();
                }
        }
 
        /**
         * Gets the delimiter.
         *
         * @return the delimiter
         */
        private String getDelimiter() {
                return delimiter;
        }
 
        /**
         * Prints the.
         *
         * @param o the o
         */
        private void print(Object o) {
                if (logWriter != null) {
                        System.out.print(o);
                }
        }
 
        /**
         * Println.
         *
         * @param o the o
         */
        private void println(Object o) {
                if (logWriter != null) {
                        logWriter.println(o);
                }
        }
 
        /**
         * Println error.
         *
         * @param o the o
         */
        private void printlnError(Object o) {
                if (errorLogWriter != null) {
                        errorLogWriter.println(o);
                }
        }
 
        /**
         * Flush.
         */
        private void flush() {
                if (logWriter != null) {
                        logWriter.flush();
                }
                if (errorLogWriter != null) {
                        errorLogWriter.flush();
                }
        }
}