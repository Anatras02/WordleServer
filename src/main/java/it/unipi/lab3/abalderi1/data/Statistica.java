package it.unipi.lab3.abalderi1.data;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Classe che gestisce e calcola le statistiche del gioco.
 * <p>
 * Questa classe aggrega le metriche dai giochi completati e fornisce un riepilogo statistico.
 */
public class Statistica {
    AtomicInteger partiteGiocate;
    AtomicInteger partiteVinte;
    AtomicInteger streakVittorie;
    AtomicInteger maxStreakVittorie;
    ConcurrentHashMap<Integer, Integer> distribuzioneTentativi;

    public Statistica() {
        this.partiteGiocate = new AtomicInteger(0);
        this.partiteVinte = new AtomicInteger(0);
        this.streakVittorie = new AtomicInteger(0);
        this.maxStreakVittorie = new AtomicInteger(0);
        this.distribuzioneTentativi = new ConcurrentHashMap<>();
    }

    public int getPartiteGiocate() {
        return partiteGiocate.get();
    }

    public int getPartiteVinte() {
        return partiteVinte.get();
    }

    public int getStreakVittorie() {
        return streakVittorie.get();
    }

    public int getMaxStreakVittorie() {
        return maxStreakVittorie.get();
    }

    public ConcurrentHashMap<Integer, Integer> getDistribuzioneTentativi() {
        return distribuzioneTentativi;
    }

    /**
     * Aggiunge una partita finita alle statistiche correnti.
     *
     * @param partita La partita finita da aggiungere.
     */
    private void addPartita(Game partita) {

        if (partita.isPartitaFinita()) {
            partiteGiocate.incrementAndGet();

            if (partita.isVinta()) {
                partiteVinte.incrementAndGet();
                streakVittorie.incrementAndGet();
                if (streakVittorie.get() > maxStreakVittorie.get()) {
                    maxStreakVittorie = streakVittorie;
                }
            } else {
                streakVittorie.set(0);
            }

            if (partita.isPartitaFinita() && partita.isVinta()) {
                int numeroTentativi = partita.getTentativi();
                if (distribuzioneTentativi.containsKey(numeroTentativi)) {
                    distribuzioneTentativi.put(numeroTentativi, distribuzioneTentativi.get(numeroTentativi) + 1);
                } else {
                    distribuzioneTentativi.put(numeroTentativi, 1);
                }
            }
        }
    }

    /**
     * Calcola le statistiche a partire da una coda di partite
     *
     * @param games, coda di partite ordinate per data
     */
    public synchronized void calcola(PriorityBlockingQueue<Game> games) {
        PriorityQueue<Game> reversedGames = new PriorityQueue<>(Collections.reverseOrder());

        reversedGames.addAll(games);

        for (Game game : reversedGames) {

            addPartita(game);
        }
    }
}
