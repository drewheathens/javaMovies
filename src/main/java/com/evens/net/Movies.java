package com.evens.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
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

//    public static void createTables(){
//        
//        String movies = "create table  IF NOT EXISTS movies (movieID int primary key not null, Title VARCHAR(250) not null, genre VARCHAR(250) unique not null)";
//        String genre =  "create table IF NOT EXISTS Genre(genre VARCHAR(50) unique not null, )";
//        
//        Connection con = DB.postgresql();
//        if(con == null){
//            System.err.println("Connection is null");// check if connection is null
//            return;
//        }
//        try {
//            PreparedStatement ps = con.prepareStatement(movies);
//            PreparedStatement ps1 = con.prepareStatement(genre);
//            
//            int item = ps.executeUpdate();
//                 System.out.println("created table  >> "+ item);
//            
//            int item1 = ps1.executeUpdate();
//                 System.out.println("created table  >> "+ item1);
//                 
//                 
//                 
//                 
//        } catch (SQLException ex) {
//            Logger.getLogger(Movies.class.getName()).log(Level.SEVERE, null, ex);
//        }     
//        
//    }
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
                String query = "INSERT INTO movies(movieid, title, genre) VALUES (?,?,?) ON CONFLICT (movieid)\n"
                        + "DO NOTHING;";

                String GenresQuery = "INSERT  INTO Genre(genre) values (?) ON CONFLICT (genre) DO NOTHING";

//                System.out.println(ql);
                PreparedStatement ps = con.prepareStatement(query);
                ps.setInt(1, jsonObject.getInt("movieID"));
                ps.setString(2, jsonObject.getString("title"));
                ps.setString(3, jsonObject.getString("genre"));
//                             
                int item = ps.executeUpdate();
                System.out.println("Items inserted are >> " + item);

                // Genre table
                String[] split = jsonObject.getString("genre").split("\\|"); // escape metacharacter
                
                // advanced for loop
                for (String arrayItem : split) {
//                    System.out.println("Item is "+arrayItem);
                    PreparedStatement genretable = con.prepareStatement(GenresQuery);
                    genretable.setString(1, arrayItem);
                    
                    int genre = genretable.executeUpdate();
                    System.out.println("genre data inserted :" + genre);

                }
            }

        } catch (IOException | SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger(Movies.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

//    public static void countGenre(){
//        Connection con = DB.postgresql();
//        if (con == null) {
//            System.err.println("Connection is null");
//            return;
//        }
//        
//        String  count = "select count(movieid) from movies where genre like '%Romance'";       
//        
//        
//    }
}
