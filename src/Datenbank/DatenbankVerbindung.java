package Datenbank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatenbankVerbindung {
    private static final String URL = "jdbc:mysql://localhost:3307/dartvereinsverwaltung"; // Datenbank-URL
    private static final String USER = "root"; // Datenbank-Benutzername
    private static final String PASSWORD = ""; // Datenbank-Passwort

    public static Connection connect() {
        Connection connection = null;
        try {
            // Verbindung zur Datenbank herstellen
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Verbindung zur Datenbank erfolgreich!");
        } catch (SQLException e) {
            // Fehler bei der Verbindungsherstellung behandeln
            System.out.println("Verbindung zur Datenbank fehlgeschlagen: " + e.getMessage());
        }
        // Verbindung zurückgeben (kann null sein, wenn fehlgeschlagen)
        return connection;
    }
}
