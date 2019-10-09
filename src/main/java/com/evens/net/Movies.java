package com.evens.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.Connection;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author evans
 */
public class Movies {

    public Connection con = DB.postgresql();

//    private static Object response;
    public static JSONArray Url() {
        try {
            String urlString = "https://beep2.cellulant.com:9001/assessment/";
            // create the url
            URL url = new URL(urlString);
            // open the url stream, wrap it an a few "readers"
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            // write the output to stdout
            String line = reader.readLine(); // json array of records
            reader.close();

            JSONArray jsonArray = new JSONArray(line);
            System.out.println("Converted object = " + jsonArray); //Outputting the result
            System.out.println("..........................................");
            return jsonArray;
//            return line;
        } catch (IOException ex) {
            Logger.getLogger(Movies.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public static void createTables(Connection con) {

        String genre = "create table IF NOT EXISTS Genre(genreID SERIAL primary key NOT NULL, genre VARCHAR(50) unique not null)";
        String movies = "create table IF NOT EXISTS movies(movieID int primary key not null, title VARCHAR NOT NULL)";
        String moviesgenres = "create table IF NOT EXISTS moviesgenres(movieID int not null,genreID integer NOT NULL,primary key(genreid, movieid), FOREIGN KEY (genreid) REFERENCES genre(genreid), FOREIGN KEY (movieID) REFERENCES movies(movieID))";

        try {
            PreparedStatement ps = con.prepareStatement(genre);
            PreparedStatement ps1 = con.prepareStatement(movies);
            PreparedStatement ps2 = con.prepareStatement(moviesgenres);

            int item = ps.executeUpdate();
            System.out.println("created table genre >> " + item);

            int item1 = ps1.executeUpdate();
            if (item > 0) {
                System.out.println("created table  movies >> " + item1);
            } else {
                System.out.println("table movies already exists!! >> " + item1);
            }

            int item2 = ps2.executeUpdate();
            if (item2 > 0) {
                System.out.println("created table moviesgenres >> " + item2);
            } else {
                System.out.println("table moviesgenres already exists!! >> " + item2);

            }

        } catch (SQLException ex) {

            Logger.getLogger(Movies.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void insertMovies(Connection con) {

        JSONArray JsonArray = Movies.Url();

//        System.out.println("Converted object = " + json); //Outputting the result
        for (int i = 0; i < JsonArray.length(); i++) {
            try {
                //Iterating over array
                JSONObject jsonObject = JsonArray.getJSONObject(i);

                String query = "INSERT INTO movies(movieid, title) VALUES (?,?) ON CONFLICT (movieid)\n"
                        + "DO NOTHING;";

                PreparedStatement ps = con.prepareStatement(query);
                ps.setInt(1, jsonObject.getInt("movieID"));
                ps.setString(2, jsonObject.getString("title"));

                int movie = ps.executeUpdate();// movies table
                if (movie > 0) {
                    System.out.println("movie inserted >> " + movie);
                } else {
                    System.out.println("movie already exists!!"
                            + "  >> " + movie);
                }
            } catch (SQLException ex) {
                Logger.getLogger(Movies.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public static void insertGenres(Connection con) {


        JSONArray JsonArray = Movies.Url();

        for (int i = 0; i < JsonArray.length(); i++) {

            JSONObject jsonObject = JsonArray.getJSONObject(i); // Genre table
            String[] split = jsonObject.getString("genre").split("\\|"); // escape metacharacter

            // advanced for loop
            for (String arrayItem : split) {
                try {
                    // iterating through genres
//                    System.out.println("Item is "+arrayItem);
                    String GenresQuery = "INSERT  INTO Genre(genre) values (?)"
                            + " ON CONFLICT (genre) DO NOTHING";

                    PreparedStatement genretable = con.prepareStatement(GenresQuery);
                    genretable.setString(1, arrayItem);

                    int genre = genretable.executeUpdate();// genre table
                    if (genre > 0) {
                        System.out.println("genre data inserted >> " + genre);
                    } else {
                        System.out.println("genre data already exists!! >> " + genre);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Movies.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }

    public static void insertMoviesGenres(Connection con) {

        JSONArray JsonArray = Movies.Url();
        for (int i = 0; i < JsonArray.length(); i++) {

            JSONObject jsonObject = JsonArray.getJSONObject(i); // moviesgenres table
            String[] split = jsonObject.getString("genre").split("\\|"); // escape metacharacter
            // moviesgenres table
            for (String genre : split) {
                try {
                    int id = 0;

                    String gID = "SELECT (genreid) FROM genre WHERE genre LIKE ?";
                    PreparedStatement result = con.prepareStatement(gID);
                    result.setString(1, genre);

                    ResultSet ID = result.executeQuery();//id moviesgenres table
                    while (ID.next()) {
                        id = ID.getInt("genreid");// get the id
//                        System.out.println("genre id is ->> " + id);//display
                    }

                    String moviesgenres = "INSERT INTO moviesgenres (movieid, genreid)\n"
                            + "VALUES (?,?) ON CONFLICT (movieid, genreid) DO NOTHING";

                    PreparedStatement mg = con.prepareStatement(moviesgenres);
                    mg.setInt(1, jsonObject.getInt("movieID"));
                    mg.setInt(2, id);

                    int moviegenre = mg.executeUpdate();// moviesgenres
                    if (moviegenre > 0) {
                        System.out.println("moviesgenres data inserted" + moviegenre);
                    } else {
                        System.out.println("moviesgenres data already exists!! >> " + moviegenre);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Movies.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }

    public static void main(String[] args, Connection con) {

//        System.out.println("Testing application");
        Movies.Url();
        Movies.createTables(con);
        Movies.insertGenres(con);
        Movies.insertMovies(con);
        Movies.insertMoviesGenres(con);
        String count = "SELECT genre.genre, COUNT(movieid) AS No_of_movies FROM moviesgenres left join  genre on  genre.genreid = moviesgenres.genreid GROUP BY genre.genre";
        System.out.println("run the query to get number of movies per Genre" + count);

    }

   

        //String count = "SELECT genre.genre, COUNT(movieid) AS No_of_movies FROM moviesgenres left join  genre on  genre.genreid = moviesgenres.genreid GROUP BY genre.genre"; // query getting number of movies per genre       

    
}
