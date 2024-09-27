package Datenbank;

import Verein.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class VereinDAO {
    private final Connection connection;

    public VereinDAO(Connection connection) {
        this.connection = connection;
    }

    public void vereinHinzufuegen(Verein verein) throws SQLException {
        String insertSQL = "INSERT INTO Verein (name, gegruendet) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, verein.getName());
            pstmt.setDate(2, java.sql.Date.valueOf(verein.getGegruendet()));

            pstmt.executeUpdate();
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                System.out.println("Verein erfolgreich hinzugefügt mit ID: " + id);
            }
        }
    }

    public static Verein getVerein(Scanner scanner) {
        System.out.println("Geben Sie den Namen des Vereins ein: ");
        String name = scanner.nextLine();
        System.out.println("Geben Sie das Gründungsdatum des Vereins ein (YYYY-MM-DD): ");
        LocalDate gegruendet = LocalDate.parse(scanner.nextLine());

        return new Verein(name, gegruendet);
    }

    public static void zeigeVereine(Statement stmt) throws SQLException {
        System.out.println("--- Alle Vereine ---");
        String query = "SELECT * FROM Verein";
        ResultSet rs = stmt.executeQuery(query);

        if (!rs.isBeforeFirst()) {
            System.out.println("Keine Vereine in der Tabelle vorhanden.");
            return;
        }

        // Kopfzeile
        System.out.format("%-5s %-20s %-12s\n", "ID", "Name", "Gegründet am");
        System.out.println("---------------------------------------------");

        // Datenzeilen
        while (rs.next()) {
            int id = rs.getInt("verein_id");
            String name = rs.getString("name");
            Date gegruendet = rs.getDate("gegruendet");

            System.out.format("%-5d %-20s %-12s\n", id, name, gegruendet);
        }
    }
}
