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

    public static void main(String[] args) {
        Connection con = DB.postgresql();
//        System.out.println("Testing application");
        Movies.Url();
        Movies.createTables(con);
        Movies.insertGenres(con);
        Movies.insertMovies(con);
        Movies.insertMoviesGenres(con);
        String count = "SELECT genre.genre, COUNT(movieid) AS No_of_movies FROM moviesgenres left join  genre on  genre.genreid = moviesgenres.genreid GROUP BY genre.genre";
        System.out.println("run the query to get number of movies per Genre --> " + count);

    }

}
