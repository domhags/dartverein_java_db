package Mitglied;

import java.time.LocalDate;

public class Trainer extends Mitglied {
    private String lizenznummer;
    private int erfahrungsjahre;

    // Konstruktor
    public Trainer(String vorname, String nachname, LocalDate geburtsdatum, String lizenznummer, int erfahrungsjahre) {
        super(vorname, nachname, geburtsdatum);
        setLizenznummer(lizenznummer);
        setErfahrungsjahre(erfahrungsjahre);
    }

    // Getter für die Lizenznummer
    public String getLizenznummer() {
        return lizenznummer;
    }

    // Setter für die Lizenznummer
    public void setLizenznummer(String lizenznummer) {
        if (lizenznummer == null || lizenznummer.trim().isEmpty()) {
            throw new IllegalArgumentException("Die Lizenznummer darf nicht leer sein.");
        }
        this.lizenznummer = lizenznummer;
    }

    // Getter für die Erfahrungsjahre
    public int getErfahrungsjahre() {
        return erfahrungsjahre;
    }

    // Setter für die Erfahrungsjahre
    public void setErfahrungsjahre(int erfahrungsjahre) {
        if (erfahrungsjahre <= 0) {
            throw new IllegalArgumentException("Die Erfahrungsjahre dürfen nicht 0 oder negativ sein.");
        }
        this.erfahrungsjahre = erfahrungsjahre;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "lizenznummer='" + getLizenznummer() + '\'' +
                ", erfahrungsjahre=" + getErfahrungsjahre() +
                "} " + super.toString();
    }
}
