package it.unipi.lab3.abalderi1.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Classe che rappresenta una singola sessione di gioco con metriche associate.
 */
public class Game implements Comparable<Game> {
    LocalDateTime datetime;
    private final AtomicBoolean partitaFinita;
    private final AtomicBoolean vittoria;

    AtomicInteger tentativi;
    private final AtomicBoolean valida;
    ArrayList<String> parole = new ArrayList<>(12);

    public Game() {
        this.vittoria = new AtomicBoolean(false);
        this.partitaFinita = new AtomicBoolean(false);
        this.valida = new AtomicBoolean(true);
        this.tentativi = new AtomicInteger(0);
        this.datetime = LocalDateTime.now();
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public boolean isPartitaFinita() {
        return partitaFinita.get();
    }

    public void finisci() {
        this.partitaFinita.set(true);
    }

    public int getTentativi() {
        return tentativi.get();
    }

    public synchronized void incrementTentativi() {
        this.tentativi.incrementAndGet();
    }

    public synchronized ArrayList<String> getParole() {
        return new ArrayList<>(parole);
    }

    public synchronized void addParola(String parola) {
        this.parole.add(parola);
    }

    public boolean isVinta() {
        return vittoria.get();
    }

    public void setVittoria(boolean vittoria) {
        this.vittoria.set(vittoria);
    }

    public boolean isValid() {
        return valida.get();
    }

    public void setValida(boolean valida) {
        this.valida.set(valida);
    }


    /**
     * Confronta questo gioco con un altro in base ai loro timestamp.
     *
     * @param o L'altro gioco da confrontare.
     * @return Valore negativo, zero o positivo a seconda se il timestamp di questo gioco è minore, uguale o maggiore rispetto all'altro gioco.
     */
    @Override
    public synchronized int compareTo(Game o) {
        return o.datetime.compareTo(this.datetime);
    }

    public String toString() {
        return "Game{" +
                "datetime=" + datetime +
                ", partitaFinita=" + partitaFinita +
                ", vittoria=" + vittoria +
                ", tentativi=" + tentativi +
                ", valida=" + valida +
                ", parole=" + parole +
                '}';
    }
}
