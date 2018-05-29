package ru.job4j.tracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class StorageSQL {
    public static void main(String[] args) {
        StorageSQL m = new StorageSQL();
        m.testDatabase();
    }

    private void testDatabase() {
        try {
            //Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/cars";
            String login = "postgres";
            String password = "password";
            try (Connection con = DriverManager.getConnection(url, login, password)) {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM car");
                while (rs.next()) {
                    String str = rs.getString("id") + " : " + rs.getString(2);
                    System.out.println(str);
                }
                rs.close();
                stmt.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}