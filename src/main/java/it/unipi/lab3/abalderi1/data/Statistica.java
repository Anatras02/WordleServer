package it.unipi.lab3.abalderi1.data;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Statistica {
    int partiteGiocate;
    int partiteVinte;
    int streakVittorie;
    int maxStreakVittorie;
    HashMap<Integer, Integer> distribuzioneTentativi;

    public Statistica() {
        this.partiteGiocate = 0;
        this.partiteVinte = 0;
        this.streakVittorie = 0;
        this.maxStreakVittorie = 0;
        this.distribuzioneTentativi = new HashMap<>();
    }

    public int getPartiteGiocate() {
        return partiteGiocate;
    }

    public int getPartiteVinte() {
        return partiteVinte;
    }

    public int getStreakVittorie() {
        return streakVittorie;
    }

    public int getMaxStreakVittorie() {
        return maxStreakVittorie;
    }

    public HashMap<Integer, Integer> getDistribuzioneTentativi() {
        return distribuzioneTentativi;
    }

    private void addPartita(Game partita) {

        if(partita.isPartitaFinita()) {
            partiteGiocate++;

            if (partita.isVinta()) {
                partiteVinte++;
                streakVittorie++;
                if (streakVittorie > maxStreakVittorie) {
                    maxStreakVittorie = streakVittorie;
                }
            } else {
                streakVittorie = 0;
            }

            if (partita.isPartitaFinita()) {
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
    public void calcola(PriorityQueue<Game> games) {
        PriorityQueue<Game> reversedGames = new PriorityQueue<>(Collections.reverseOrder());
        reversedGames.addAll(games);

        for (Game game : reversedGames) {

            addPartita(game);
        }
    }
}
