package com.todo.util;
import java.sql.Connection;
import java.sql.DriverManager; 
import java.sql.SQLException;
public class db {
    public static final String DB_URL = "jdbc:mysql://localhost:3306/todo";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "Ammar@123";

    static {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch(ClassNotFoundException e){
            System.out.println("JDBC DRIVER is missing"); 
        }
    }
    public static Connection getDBConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }
    public class getDBConnection {
    }
}
