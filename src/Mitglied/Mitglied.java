package Mitglied;

import java.time.LocalDate;

public abstract class Mitglied {
    private String vorname;
    private String nachname;
    private LocalDate geburtsdatum;

    public Mitglied(String vorname, String nachname, LocalDate geburtsdatum) {
        setVorname(vorname);
        setNachname(nachname);
        setGeburtsdatum(geburtsdatum);
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        if (vorname == null || vorname.trim().isEmpty()) {
            throw new IllegalArgumentException("Der Vorname darf nicht leer sein.");
        }
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        if (nachname == null || nachname.trim().isEmpty()) {
            throw new IllegalArgumentException("Der Nachname darf nicht leer sein.");
        }
        this.nachname = nachname;
    }

    public LocalDate getGeburtsdatum() {
        return geburtsdatum;
    }

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
