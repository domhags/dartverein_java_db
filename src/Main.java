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
        Connection connection = DatenbankVerbindung.connect();

        if (connection != null) {
            try {
                startMenu(connection);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Ein Fehler beim Verbinden mit der Datenbank", e);
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

            MitgliedDAO mitgliedDAO = new MitgliedDAO(connection);
            VereinDAO vereinDAO = new VereinDAO(connection);

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
                        Mitglied mitglied = MitgliedDAO.getMitglied(scanner);
                        if (mitglied != null) {
                            mitgliedDAO.mitgliedHinzufuegen(mitglied);
                        }
                        break;
                    case 2:
                        Verein verein = VereinDAO.getVerein(scanner);
                        vereinDAO.vereinHinzufuegen(verein);
                        break;
                    case 3:
                        mitgliedDAO.zeigeMitglieder(stmt);
                        break;
                    case 4:
                        VereinDAO.zeigeVereine(stmt);
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
}
