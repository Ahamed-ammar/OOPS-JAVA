package com.todo;
import java.sql.Connection;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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
            System.out.println(1);
        }
        // catch (SQLException e) {
        //     System.out.println("Connection Failed");
        //     //e.printStackTrace();
        // }
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } 
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e){
            System.out.println("Failed to set Look and Feel" + e.getMessage());
        }
        SwingUtilities.invokeLater(() -> {
            try {
                com.todo.gui.TodoAppGui app = new com.todo.gui.TodoAppGui();
                app.setVisible(true);
            } catch (Exception e) {
                System.err.println("Failed to launch the application: " + e.getMessage());
            }
            
        });
    }
}
