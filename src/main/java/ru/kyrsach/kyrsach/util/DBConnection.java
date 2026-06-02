package ru.kyrsach.kyrsach.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection conn;
    private static String url = "jdbc:postgresql://localhost:5432/coursework";
    private static String user = "postgres";
    private static String password = "la123";

    public static Connection getConn() throws SQLException {
        if(conn == null || conn.isClosed()){
            try {
                conn = DriverManager.getConnection(url,user,password);
                System.out.println("yes");
            } catch (SQLException e) {
                System.out.println("no");
                e.printStackTrace();
            }
        }
        return conn;
    }
    public static void closeConnection() {

        try {

            if (conn != null && !conn.isClosed()) {

                conn.close();
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
}
