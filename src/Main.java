import Datenbank.*;
import Mitglied.*;
import Verein.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        Connection connection = DatenbankVerbindung.connect();

        if (connection != null) {
            try {
                startMenu(connection);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Ein Fehler ist aufgetreten", e);
            } finally {
                try {
                    if (!connection.isClosed()) {
                        connection.close();
                    }
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "Fehler beim Schließen der Datenbankverbindung", ex);
                }
            }
        } else {
            logger.warning("Verbindung zur Datenbank konnte nicht hergestellt werden.");
        }
    }

    public static void startMenu(Connection connection) {
        int auswahl;
        try (Scanner scanner = new Scanner(System.in);

             Statement stmt = connection.createStatement()) {

            while (true) {
                System.out.println("\n--- Dartvereinsverwaltung ---");
                System.out.println("1. Neues Mitglied hinzufügen");
                System.out.println("2. Neuen Verein hinzufügen");
                System.out.println("3. Alle Mitglieder anzeigen");
                System.out.println("4. Alle Vereine anzeigen");
                System.out.println("5. Beenden");
                System.out.print("Wählen Sie eine Option (1-5): ");

                try {
                    auswahl = Integer.parseInt(scanner.nextLine());
                    if (auswahl < 1 || auswahl > 5) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ungültige Eingabe. Bitte wählen Sie eine Option (1-7): ");
                    continue;
                }

                switch (auswahl) {
                    case 1:
                        erstelleMitglied(scanner, connection);
                        break;
                    case 2:
                        erstelleVerein(scanner, connection);
                        break;
                    case 3:
                        zeigeMitglieder(stmt);
                        break;
                    case 4:
                        zeigeVereine(stmt);
                        break;
                    case 5:
                        System.out.println("Programm wird beendet.");
                        return;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Datenbankfehler aufgetreten", e);
        }
}

    private static void erstelleMitglied(Scanner scanner, Connection connection) {
        Mitglied mitglied = getMitglied(scanner);
        if (mitglied != null) {
            String insertSQL = "";

            if (mitglied instanceof Spieler) {
                insertSQL = "INSERT INTO Spieler (vorname, nachname, geburtsdatum, anzahl_spiele, wurftechnik) " +
                        "VALUES (?, ?, ?, ?, ?)";
            } else if (mitglied instanceof Trainer) {
                insertSQL = "INSERT INTO Trainer (vorname, nachname, geburtsdatum, lizenznummer, erfahrungsjahre) " +
                        "VALUES (?, ?, ?, ?, ?)";
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
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Fehler beim Hinzufügen des Mitglieds", e);
            }
        }
    }

    private static void zeigeMitglieder(Statement stmt) throws SQLException {
        System.out.println("--- Alle Mitglieder ---");

        zeigeMitgliederAusTabelle(stmt, "Spieler");
        zeigeMitgliederAusTabelle(stmt, "Trainer");
    }

    private static void zeigeMitgliederAusTabelle(Statement stmt, String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName;
        ResultSet rs = stmt.executeQuery(query);

        if (!rs.isBeforeFirst()) {
            System.out.println("Keine Mitglieder in der Tabelle " + tableName);
            return;
        }

        while (rs.next()) {
            int id = rs.getInt(tableName.toLowerCase() + "_id");
            String vorname = rs.getString("vorname");
            String nachname = rs.getString("nachname");
            LocalDate geburtsdatum = rs.getDate("geburtsdatum").toLocalDate();

            System.out.println("ID: " + id + ", Vorname: " + vorname + ", Nachname: " + nachname +
                    ", Geburtsdatum: " + geburtsdatum);

            switch (tableName) {
                case "Spieler":
                    zeigeSpielerDetails(rs);
                    System.out.println("Typ: Spieler");
                    break;
                case "Trainer":
                    zeigeTrainerDetails(rs);
                    System.out.println("Typ: Trainer");
                    break;
            }

            System.out.println("-----------------------");
        }
    }

    private static void zeigeSpielerDetails(ResultSet rs) throws SQLException {
        int anzahlSpiele = rs.getInt("anzahl_spiele");
        String wurftechnik = rs.getString("wurftechnik");

        System.out.println("Anzahl der Spiele: " + anzahlSpiele);
        System.out.println("Wurftechnik: " + wurftechnik);
    }

    private static void zeigeTrainerDetails(ResultSet rs) throws SQLException {
        String lizenznummer = rs.getString("lizenznummer");
        int erfahrungsjahre = rs.getInt("erfahrungsjahre");

        System.out.println("Lizenznummer: " + lizenznummer);
        System.out.println("Erfahrungsjahre: " + erfahrungsjahre);
    }

    private static void erstelleVerein(Scanner scanner, Connection connection) {
        Verein verein = getVerein(scanner);
        String insertSQL;

        insertSQL = "INSERT INTO Verein (name, gegruendet) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, verein.getName());
            pstmt.setDate(2, java.sql.Date.valueOf(verein.getGegruendet()));

            pstmt.executeUpdate();
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);

                System.out.println("Verein erfolgreich hinzugefügt mit ID: " + id);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Fehler beim Hinzufügen des Vereins", e);
        }
    }

    private static Mitglied getMitglied(Scanner scanner) {
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

    private static Verein getVerein(Scanner scanner) {
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

        while (rs.next()) {
            int id = rs.getInt("verein_id");
            String name = rs.getString("name");
            Date gegruendet = rs.getDate("gegruendet");

            System.out.println("ID: " + id + ", Name: " + name + ", Gegründet am: " + gegruendet);
            System.out.println("-----------------");
        }
    }
}
