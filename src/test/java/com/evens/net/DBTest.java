package com.evens.net;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tools.ant.types.Assertions;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author evans
 */
public class DBTest {

    public static Connection actual;
    public static Statement stmt;

    @Before
    public void setUp() {
       actual =  DB.postgresql();
    }

    @After
    public void tearDown() {
        try {
            actual.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of postgresql method, of class DB.
     */
    @Test
    public void testPostgresql() {
        System.out.println("postgresql connectivity method test!!");
        assertFalse(actual == null);
    }
}
