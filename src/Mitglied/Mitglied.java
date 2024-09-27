package Mitglied;

import java.time.LocalDate;

public abstract class Mitglied {
    private String vorname;
    private String nachname;
    private LocalDate geburtsdatum;

    // Konstruktor
    public Mitglied(String vorname, String nachname, LocalDate geburtsdatum) {
        setVorname(vorname);
        setNachname(nachname);
        setGeburtsdatum(geburtsdatum);
    }

    // Getter für Vorname
    public String getVorname() {
        return vorname;
    }

    // Setter für Vorname
    public void setVorname(String vorname) {
        if (vorname == null || vorname.trim().isEmpty()) {
            throw new IllegalArgumentException("Der Vorname darf nicht leer sein.");
        }
        this.vorname = vorname;
    }

    // Getter für Nachname
    public String getNachname() {
        return nachname;
    }

    // Setter für Nachname
    public void setNachname(String nachname) {
        if (nachname == null || nachname.trim().isEmpty()) {
            throw new IllegalArgumentException("Der Nachname darf nicht leer sein.");
        }
        this.nachname = nachname;
    }

    // Getter für Geburtsdatum
    public LocalDate getGeburtsdatum() {
        return geburtsdatum;
    }

    // Setter für Geburtsdatum
    public void setGeburtsdatum(LocalDate geburtsdatum) {
        if (geburtsdatum.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Das Geburtsdatum darf nicht in der Zukunft liegen.");
        }
        this.geburtsdatum = geburtsdatum;
    }

    @Override
    public String toString() {
        return "Mitglied{" +
                "vorname='" + getVorname() + '\'' +
                ", nachname='" + getNachname() + '\'' +
                ", geburtsdatum=" + getGeburtsdatum() +
                '}';
    }
}
