package Datenbank;

import Mitglied.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class MitgliedDAO {
    private final Connection connection;

    public MitgliedDAO(Connection connection) {
        this.connection = connection;
    }

    public void mitgliedHinzufuegen(Mitglied mitglied) throws SQLException {
        String insertSQL = "";

        if (mitglied instanceof Spieler) {
            insertSQL = "INSERT INTO Spieler (vorname, nachname, geburtsdatum, anzahl_spiele, wurftechnik) VALUES (?, ?, ?, ?, ?)";
        } else if (mitglied instanceof Trainer) {
            insertSQL = "INSERT INTO Trainer (vorname, nachname, geburtsdatum, lizenznummer, erfahrungsjahre) VALUES (?, ?, ?, ?, ?)";
        }

        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, mitglied.getVorname());
            pstmt.setString(2, mitglied.getNachname());
            pstmt.setDate(3, java.sql.Date.valueOf(mitglied.getGeburtsdatum()));

            if (mitglied instanceof Spieler) {
                Spieler s = (Spieler) mitglied;
                pstmt.setInt(4, s.getAnzahlSpiele());
                pstmt.setString(5, s.getWurftechnik());
            } else if (mitglied instanceof Trainer) {
                Trainer t = (Trainer) mitglied;
                pstmt.setString(4, t.getLizenznummer());
                pstmt.setInt(5, t.getErfahrungsjahre());
            }

            pstmt.executeUpdate();
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                System.out.println("Mitglied erfolgreich hinzugefügt mit ID: " + id);
            }
        }
    }

    public void zeigeMitglieder(Statement stmt) throws SQLException {
        zeigeMitgliederAusTabelle(stmt, "Spieler");
        zeigeMitgliederAusTabelle(stmt, "Trainer");
    }

    private void zeigeMitgliederAusTabelle(Statement stmt, String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName;
        ResultSet rs = stmt.executeQuery(query);

        if (!rs.isBeforeFirst()) {
            System.out.println("Keine Mitglieder in der Tabelle " + tableName);
            return;
        }

        // Kopfzeile
        if (tableName.equals("Spieler")) {
            System.out.format("%-5s %-15s %-15s %-12s %-10s %-15s\n", "ID", "Vorname", "Nachname", "Geburtsdatum", "Spiele", "Wurftechnik");
            System.out.println("-------------------------------------------------------------------------------");
        } else if (tableName.equals("Trainer")) {
            System.out.format("%-5s %-15s %-15s %-12s %-15s %-10s\n", "ID", "Vorname", "Nachname", "Geburtsdatum", "Lizenznummer", "Erfahrungsjahre");
            System.out.println("-------------------------------------------------------------------------------");
        }

        // Datenzeilen
        while (rs.next()) {
            int id = rs.getInt(tableName.toLowerCase() + "_id");
            String vorname = rs.getString("vorname");
            String nachname = rs.getString("nachname");
            LocalDate geburtsdatum = rs.getDate("geburtsdatum").toLocalDate();

            if (tableName.equals("Spieler")) {
                int anzahlSpiele = rs.getInt("anzahl_spiele");
                String wurftechnik = rs.getString("wurftechnik");
                System.out.format("%-5d %-15s %-15s %-12s %-10d %-15s\n", id, vorname, nachname, geburtsdatum, anzahlSpiele, wurftechnik);
            } else if (tableName.equals("Trainer")) {
                String lizenznummer = rs.getString("lizenznummer");
                int erfahrungsjahre = rs.getInt("erfahrungsjahre");
                System.out.format("%-5d %-15s %-15s %-12s %-15s %-10d\n", id, vorname, nachname, geburtsdatum, lizenznummer, erfahrungsjahre);
            }
        }
        if (tableName.equals("Spieler")) {
            System.out.println();
        }
    }


    public static Mitglied getMitglied(Scanner scanner) {
        System.out.println("Wählen Sie die Art des Mitglieds aus:");
        System.out.println("1. Spieler");
        System.out.println("2. Trainer");
        System.out.println("Option wählen (1 oder 2): ");

        int art;
        try {
            art = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ungültige Eingabe. Bitte geben Sie eine Zahl zwischen 1 und 2 ein.");
            return null;
        }

        Mitglied mitglied;
        switch (art) {
            case 1:
                mitglied = erstelleSpieler(scanner);
                break;
            case 2:
                mitglied = erstelleTrainer(scanner);
                break;
            default:
                System.out.println("Ungültige Eingabe. Bitte wählen Sie einen Mitgliedstyp aus.");
                return null;
        }
        return mitglied;
    }

    private static Spieler erstelleSpieler(Scanner scanner) {
        System.out.println("Geben Sie den Vornamen des Spielers ein: ");
        String vorname = scanner.nextLine();
        System.out.println("Geben Sie den Nachname des Spielers ein: ");
        String nachname = scanner.nextLine();
        System.out.println("Geben Sie das Geburtsdatum (YYYY-MM-DD) ein: ");
        LocalDate geburtsdatum = LocalDate.parse(scanner.nextLine());
        System.out.println("Geben Sie die Anzahl der gespielten Spiele ein: ");
        int anzahlSpiele = Integer.parseInt(scanner.nextLine());
        System.out.println("Geben Sie die Wurftechnik (zB Präzisionswurf, Kraftwurf, etc) ein: ");
        String wurftechnik = scanner.nextLine();

        return new Spieler(vorname, nachname, geburtsdatum, anzahlSpiele, wurftechnik);
    }

    private static Trainer erstelleTrainer(Scanner scanner) {
        System.out.println("Geben Sie den Vornamen des Trainers ein: ");
        String vorname = scanner.nextLine();
        System.out.println("Geben Sie den Nachname des Trainers ein: ");
        String nachname = scanner.nextLine();
        System.out.println("Geben Sie das Geburtsdatum (YYYY-MM-DD) ein: ");
        LocalDate geburtsdatum = LocalDate.parse(scanner.nextLine());
        System.out.println("Geben Sie die Lizenznummer des Trainers ein: ");
        String lizenznummer = scanner.nextLine();
        System.out.println("Geben Sie die Erfahrungsjahre des Trainers ein: ");
        int erfahrungsjahre = Integer.parseInt(scanner.nextLine());

        return new Trainer(vorname, nachname, geburtsdatum, lizenznummer, erfahrungsjahre);
    }
}

