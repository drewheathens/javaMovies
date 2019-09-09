package com.evens.net;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.JSONArray;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
//import org.junit.runner.RunWith;
//import org.mockito.runners.MockitoJUnitRunner;

///**
// *
// * @author evans
// */
//@RunWith(MockitoJUnitRunner.class)
public class MoviesTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
//
    /**
     * Test of Url() method, of class Movies.
     *
     * @throws java.lang.Exception
     */
    @Test
   
    public void testUrl() {
        System.out.println("Url method!!");
        JSONArray actual = Movies.Url();
        JSONArray Jsonarray;
        assertNotNull(actual);
    }
//
//    /**
//     * Test of createTables method, of class Movies.
//     */
    @Test
    public void testCreateTables() throws SQLException {
        Connection con = DB.postgresql();
        System.out.println("createTables test");

        String str = "create table IF NOT EXISTS test(movieID int primary key not null, title VARCHAR NOT NULL)";
        PreparedStatement ps = con.prepareStatement(str);
        int expResults = ps.executeUpdate();

    }


    /**
     * Test of insertMovies method, of class Movies.
     */
    @Test
    public void testInsertMovies() {
        System.out.println("insertMovies");
        Movies.insertMovies();

    }

    /**
     * Test of insertGenres method, of class Movies.
     */
    @Test
    public void testInsertGenres() {
        System.out.println("insertGenres");
        Movies.insertGenres();
   
    }

    /**
     * Test of insertMoviesGenres method, of class Movies.
     */
    @Test
    public void testInsertMoviesGenres() {
        System.out.println("insertMoviesGenres");
        Movies.insertMoviesGenres();
     
    }


}
