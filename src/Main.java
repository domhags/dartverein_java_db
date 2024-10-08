import Datenbank.*;
import Mitglied.*;
import Verein.*;

import java.sql.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        Connection connection = DatenbankVerbindung.connect(); // Verbindung zur Datenbank herstellen

        if (connection != null) {
            try {
                startMenu(connection); // Hauptmenü starten
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Ein Fehler beim Verbinden mit der Datenbank", e);
            } finally {
                try {
                    if (!connection.isClosed()) {
                        connection.close(); // Verbindung schließen
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

            MitgliedDAO mitgliedDAO = new MitgliedDAO(connection); // DAO für Mitglieder
            VereinDAO vereinDAO = new VereinDAO(connection); // DAO für Vereine

            while (true) {
                // Menüoptionen anzeigen
                System.out.println("\n--- Dartvereinsverwaltung ---");
                System.out.println("1. Neues Mitglied hinzufügen");
                System.out.println("2. Neuen Verein hinzufügen");
                System.out.println("3. Alle Mitglieder anzeigen");
                System.out.println("4. Alle Vereine anzeigen");
                System.out.println("5. Beenden");
                System.out.print("Wählen Sie eine Option (1-5): ");

                try {
                    auswahl = Integer.parseInt(scanner.nextLine()); // Benutzerauswahl einlesen
                    if (auswahl < 1 || auswahl > 5) {
                        throw new NumberFormatException(); // Eingabe validieren
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ungültige Eingabe. Bitte wählen Sie eine Option (1-5): ");
                    continue; // Fehlerbehandlung für ungültige Eingaben
                }

                switch (auswahl) {
                    case 1:
                        // Neues Mitglied hinzufügen
                        Mitglied mitglied = MitgliedDAO.getMitglied(scanner);
                        if (mitglied != null) {
                            mitgliedDAO.mitgliedHinzufuegen(mitglied);
                        }
                        break;
                    case 2:
                        // Neuen Verein hinzufügen
                        Verein verein = VereinDAO.getVerein(scanner);
                        vereinDAO.vereinHinzufuegen(verein);
                        break;
                    case 3:
                        // Alle Mitglieder anzeigen
                        mitgliedDAO.zeigeMitglieder(stmt);
                        break;
                    case 4:
                        // Alle Vereine anzeigen
                        VereinDAO.zeigeVereine(stmt);
                        break;
                    case 5:
                        // Programm beenden
                        System.out.println("Programm wird beendet.");
                        return;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Datenbankfehler aufgetreten", e); // Fehlerbehandlung für SQL-Fehler
        }
    }
}
