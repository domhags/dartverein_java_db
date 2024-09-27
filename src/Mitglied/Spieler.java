package Mitglied;

import java.time.LocalDate;

public class Spieler extends Mitglied {
    private int anzahlSpiele;
    private String wurftechnik;

    // Konstruktor
    public Spieler(String vorname, String nachname, LocalDate geburtsdatum, int anzahlSpiele, String wurftechnik) {
        super(vorname, nachname, geburtsdatum);
        setAnzahlSpiele(anzahlSpiele);
        setWurftechnik(wurftechnik);
    }

    // Getter für die Anzahl der gespielten Spiele
    public int getAnzahlSpiele() {
        return anzahlSpiele;
    }

    // Setter für die Anzahl der gespielten Spiele
    public void setAnzahlSpiele(int anzahlSpiele) {
        if (anzahlSpiele <= 0) {
            throw new IllegalArgumentException("Die Anzahl der Spiele darf nicht 0 oder negativ sein.");
        }
        this.anzahlSpiele = anzahlSpiele;
    }

    // Getter für die Wurftechnik
    public String getWurftechnik() {
        return wurftechnik;
    }

    // Setter für die Wurftechnik
    public void setWurftechnik(String wurftechnik) {
        if (wurftechnik == null || wurftechnik.trim().isEmpty()) {
            throw new IllegalArgumentException("Die Wurftechnik darf nicht leer sein.");
        }
        this.wurftechnik = wurftechnik;
    }

    @Override
    public String toString() {
        return "Spieler{" +
                "anzahlSpiele=" + getAnzahlSpiele() +
                ", wurftechnik='" + getWurftechnik() + '\'' +
                "} " + super.toString();
    }
}
