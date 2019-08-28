package com.evens.net;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
//import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author evans
 */
public class DB {

    public static Connection postgresql() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/moviedb", "evans", "movies");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection failure.");
            return connection;
        }
        return connection;
    }

    public static void main(String[] args) throws IOException, SQLException {
        
//        System.out.println("Testing application");

//        Movies.Url();           
        
        Movies.insert();

    }

}
