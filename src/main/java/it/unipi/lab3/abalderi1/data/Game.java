package it.unipi.lab3.abalderi1.data;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Classe che rappresenta una singola sessione di gioco con metriche associate.
 */
public class Game implements Comparable<Game> {
    LocalDateTime datetime;
    boolean partitaFinita;
    boolean vittoria;
    int tentativi;
    boolean valida;
    ArrayList<String> parole = new ArrayList<>(12);

    public Game() {
        this.vittoria = false;
        this.partitaFinita = false;
        this.valida = true;
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

    public boolean isValid() {
        return valida;
    }

    public void setValida(boolean valida) {
        this.valida = valida;
    }


    /**
     * Confronta questo gioco con un altro in base ai loro timestamp.
     *
     * @param o L'altro gioco da confrontare.
     * @return Valore negativo, zero o positivo a seconda se il timestamp di questo gioco è minore, uguale o maggiore rispetto all'altro gioco.
     */
    @Override
    public int compareTo(Game o) {
        return o.datetime.compareTo(this.datetime);
    }

}
