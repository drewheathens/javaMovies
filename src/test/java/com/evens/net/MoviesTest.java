package com.evens.net;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static javax.swing.UIManager.getInt;
import org.json.JSONArray;
import org.junit.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author evans
 */
public class MoviesTest {
//
//    @AfterClass
//    public void teardown() throws SQLException {
//        Connection con = DB.postgresql();
//        Statement statement = con.createStatement();
//        statement.executeUpdate("DROP TABLE test");
//    }

    /**
     * Test of Url() method, of class Movies.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testUrl() {
        System.out.println("Url method!!");
        String actual = Movies.Url();
        JSONArray Jsonarray;
        assertNotNull(actual);
    }

    /**
     * Test of createTables method, of class Movies.
     */
    @Test
    public void testCreateTables() throws SQLException {
        Connection con = DB.postgresql();
        System.out.println("createTables test");

        String str = "create table IF NOT EXISTS test(movieID int primary key not null, title VARCHAR NOT NULL)";
        PreparedStatement ps = con.prepareStatement(str);
        int expResults = ps.executeUpdate();

    }

    /**
     * Test of insert method, of class Movies.
     *
     */
    @Test
    public void testInsert() throws SQLException {
        Connection con = DB.postgresql();
        System.out.println("insert test!!");
        String string = "insert into test(movieID, title) VALUES (1, 'movietest') ON CONFLICT (movieID) DO NOTHING";
        String fetch = "select movieID from test";
        PreparedStatement ps = con.prepareStatement(string);
        int exec = ps.executeUpdate();

        PreparedStatement sel = con.prepareStatement(fetch);
        ResultSet select = sel.executeQuery();
        if (select.next()) {
            assertEquals(1, select.getInt("movieID"));
        }

    }
}
