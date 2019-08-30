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

//    private static Object response;
    public static String Url() throws IOException {
        String urlString = "https://beep2.cellulant.com:9001/assessment/";
        // create the url
        URL url = new URL(urlString);

        // open the url stream, wrap it an a few "readers"
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

        // write the output to stdout
        String line = reader.readLine(); // json array of records 
        reader.close();
        return line;
    }

    public static void createTables() {

        String genre = "create table IF NOT EXISTS Genre(genreID SERIAL primary key NOT NULL, genre VARCHAR(50) unique not null)";
        String movies = "create table IF NOT EXISTS movies(movieID int primary key not null, title VARCHAR NOT NULL)";
        String moviesgenres = "create table IF NOT EXISTS moviesgenres(movieID int not null,genreID integer NOT NULL,primary key(genreid, movieid), FOREIGN KEY (genreid) REFERENCES genre(genreid), FOREIGN KEY (movieID) REFERENCES movies(movieID) )";

        Connection con = DB.postgresql();
        if (con == null) {
            System.err.println("Connection is null");// check if connection is null
            return;
        }
        try {
            PreparedStatement ps = con.prepareStatement(genre);
            PreparedStatement ps1 = con.prepareStatement(movies);
            PreparedStatement ps2 = con.prepareStatement(moviesgenres);
            
            int item = ps.executeUpdate();
                 System.out.println("created table genre >> "+ item);

            int item1 = ps1.executeUpdate();
                 System.out.println("created table  movies >> "+ item1);

            int item2 = ps2.executeUpdate();
            System.out.println("created table moviesgenres >> " + item2);
            
        } catch (SQLException ex) {
            
            Logger.getLogger(Movies.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void insert() {
        Connection con = DB.postgresql();
        if (con == null) {
            System.err.println("Connection is null");
            return;
        }

//        JSONArray jsonArray = new JSONArray("[{'name':'evens'},{'name':'even2'}]");
        try {
            String json = Movies.Url();
            JSONArray jsonArray = new JSONArray(json);
            System.out.println("Converted object = " + json); //Outputting the result
            System.out.println("..........................................");

            for (int i = 0; i < jsonArray.length(); i++) { //Iterating over array
                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                String query = "INSERT INTO movies(movieid, title, genre) VALUES (?,?,?) ON CONFLICT (movieid)\n"
//                        + "DO NOTHING;";
                String query = "INSERT INTO movies(movieid, title) VALUES (?,?) ON CONFLICT (movieid)\n"
                        + "DO NOTHING;";

                PreparedStatement ps = con.prepareStatement(query);
                ps.setInt(1, jsonObject.getInt("movieID"));
                ps.setString(2, jsonObject.getString("title"));
//             

                int item = ps.executeUpdate();// movies table
                System.out.println("Items inserted are >> " + item);

                // Genre table
                String[] split = jsonObject.getString("genre").split("\\|"); // escape metacharacter

                // advanced for loop
                for (String arrayItem : split) {
//                    System.out.println("Item is "+arrayItem);
                    String GenresQuery = "INSERT  INTO Genre(genre) values (?)"
                            + " ON CONFLICT (genre) DO NOTHING";

                    PreparedStatement genretable = con.prepareStatement(GenresQuery);
                    genretable.setString(1, arrayItem);

                    int genre = genretable.executeUpdate();// genre table
                    System.out.println("genre data inserted :" + genre);                    
                }

                // moviesgenres table
                for (String genre : split) {
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
                            + "VALUES (?,?)";

                    PreparedStatement mg = con.prepareStatement(moviesgenres);
                    mg.setInt(1, jsonObject.getInt("movieID"));
                    mg.setInt(2, id);                    

                    int moviegenre = mg.executeUpdate();// moviesgenres
                    System.out.println("moviesgenres data inserted :" + moviegenre);
                }

            }

        } catch (IOException | SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger(Movies.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void countGenre(){
        Connection con = DB.postgresql();
        if (con == null) {
            System.err.println("Connection is null");
            return;
        }
        
        String  count = "SELECT genre.genre, COUNT(movieid) AS No_of_movies FROM moviesgenres left join  genre on  genre.genreid = moviesgenres.genreid GROUP BY genre.genre";       
        
        
    }
}
