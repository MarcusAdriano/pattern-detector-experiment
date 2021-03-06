/*
 *
 * Derby - Class org.apache.derbyTesting.functionTests.util.BaseJDBCTestSetup
 *
 * Copyright 2006 The Apache Software Foundation or its 
 * licensors, as applicable.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific 
 * language governing permissions and limitations under the License.
 */
package org.apache.derbyTesting.functionTests.util;

import java.sql.*;

import junit.extensions.TestSetup;
import junit.framework.Test;

/**
 * Base class for JDBC JUnit test decorators.
 */
public abstract class BaseJDBCTestSetup
    extends TestSetup {
	
	public BaseJDBCTestSetup(Test test) {
		super(test);
	}
	
	/**
	 * Maintain a single connection to the default
	 * database, opened at the first call to getConnection.
	 * Typical setup will just require a single connection.
	 * @see BaseJDBCTestSetup#getConnection()
	 */
	private Connection conn;
	
    /**
     * Return the current configuration for the test.
     */
    public final TestConfiguration getTestConfiguration()
    {
    	return TestConfiguration.DERBY_TEST_CONFIG;
    }
    
    /**
     * Obtain the connection to the default database.
     * This class maintains a single connection returned
     * by this class, it is opened on the first call to
     * this method. Subsequent calls will return the same
     * connection object unless it has been closed. In that
     * case a new connection object will be returned.
     * <P>
     * The tearDown method will close the connection if
     * it is open.
     * @see TestConfiguration#getDefaultConnection()
     */
    public final Connection getConnection() throws SQLException
    {
    	if (conn != null)
    	{
    		if (!conn.isClosed())
    			return conn;
    		conn = null;
    	}
    	return conn = getTestConfiguration().getDefaultConnection();
    }
    
    /**
     * Print debug string.
     * @param text String to print
     */
    public void println(final String text) {
        if (getTestConfiguration().isVerbose()) {
            System.out.println("DEBUG: " + text);
        }
    }
    
    /**
     * Tear down this fixture, sub-classes should call
     * super.tearDown(). This cleanups & closes the connection
     * if it is open.
     */
    protected void tearDown()
    throws java.lang.Exception
    {
    	JDBC.cleanup(conn);
    }
}
