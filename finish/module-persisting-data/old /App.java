package io.openliberty.guides;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {
    private final String url = "jdbc:postgresql://localhost:5432/";
    private final String user = "admin";
    private final String password = "adminpwd";

    public Connection connect(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to Postgresssss");
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public static void main(String[] args){
        App app = new App();
        app.connect();
    }
}
