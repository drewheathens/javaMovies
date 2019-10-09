package com.evens.net;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/moviedb", "evans", "movies");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection failure.");
            return con;
        }
        return con;
    }


    

}
