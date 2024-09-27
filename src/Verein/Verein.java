package Verein;

import java.time.LocalDate;

public class Verein {
    private String name;
    private LocalDate gegruendet;

    // Konstruktor
    public Verein(String name, LocalDate gegruendet) {
        setName(name);
        setGegruendet(gegruendet);
    }

    // Getter für den Vereinsnamen
    public String getName() {
        return name;
    }

    // Setter für den Vereinsnamen
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Der Name des Vereins darf nicht leer sein.");
        }
        this.name = name;
    }

    // Getter für das Gründungsdatum des Vereins
    public LocalDate getGegruendet() {
        return gegruendet;
    }

    // Setter für das Gründungsdatum des Vereins
    public void setGegruendet(LocalDate gegruendet) {
       if (gegruendet.isAfter(LocalDate.now())) {
           throw new IllegalArgumentException("Die Gründung des Vereins darf nicht in der Zukunft liegen.");
       }
       this.gegruendet = gegruendet;
    }

    @Override
    public String toString() {
        return "Verein{" +
                "name='" + getName() + '\'' +
                ", gegruendet=" + getGegruendet() +
                '}';
    }
}
