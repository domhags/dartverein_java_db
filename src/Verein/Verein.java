package Verein;

import java.time.LocalDate;

public class Verein {
    private String name;
    private LocalDate gegruendet;

    public Verein(String name, LocalDate gegruendet) {
        setName(name);
        setGegruendet(gegruendet);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Der Name des Vereins darf nicht leer sein.");
        }
        this.name = name;
    }

    public LocalDate getGegruendet() {
        return gegruendet;
    }

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
