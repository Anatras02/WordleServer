package it.unipi.lab3.abalderi1.data;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Game implements Comparable<Game> {
    LocalDateTime datetime;
    boolean partitaFinita;
    boolean vittoria;
    int tentativi;
    ArrayList<String> parole = new ArrayList<>(12);

    public Game() {
        this.vittoria = false;
        this.partitaFinita = false;
        this.tentativi = 0;
        this.datetime = LocalDateTime.now();
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public boolean isPartitaFinita() {
        return partitaFinita;
    }

    public void finisci() {
        this.partitaFinita = true;
    }

    public int getTentativi() {
        return tentativi;
    }

    public void incrementTentativi() {
        this.tentativi++;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<String> getParole() {
        return (ArrayList<String>) parole.clone();
    }

    public void addParola(String parola) {
        this.parole.add(parola);
    }

    public boolean isVinta() {
        return vittoria;
    }

    public void setVittoria(boolean vittoria) {
        this.vittoria = vittoria;
    }

    public boolean isDiOggi() {
        if (datetime == null) {
            return false;
        }

        return datetime.getDayOfYear() == LocalDateTime.now().getDayOfYear() &&
                datetime.getYear() == LocalDateTime.now().getYear();
    }

    @Override
    public int compareTo(Game o) {
        return o.datetime.compareTo(this.datetime);
    }
}
