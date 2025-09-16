package com.todo;
import java.sql.Connection;

//import javax.xml.crypto.Data;

import com.todo.util.db;

public class Main {
    public static void main(String[] args) {
        db databaseConnection = new db();
        //DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            Connection cn = databaseConnection.getDBConnection();
            System.out.println("Connection Successful");
            //Connection cn = databaseConnection.getDBConnection();
        } 
        catch(Exception e){
            System.out.println("Connection Failed");

        }
        // catch (SQLException e) {
        //     System.out.println("Connection Failed");
        //     //e.printStackTrace();
        // }
        
    }
}
