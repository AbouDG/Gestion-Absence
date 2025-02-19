package sn.uasz.gestionConge.gestionConge.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/GestionConge";
        String user = "postgres";
        String password = "Adg201800743";

        return DriverManager.getConnection(url, user, password);
    }
}
