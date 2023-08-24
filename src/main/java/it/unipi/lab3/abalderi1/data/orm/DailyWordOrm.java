

package it.unipi.lab3.abalderi1.data.orm;

import com.google.gson.reflect.TypeToken;
import it.unipi.lab3.abalderi1.data.DailyWord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Random;

public class DailyWordOrm extends Orm {
    private static final DailyWordOrm instance = new DailyWordOrm();

    private DailyWord dailyWord;

    private DailyWordOrm() {
        super();
        this.filePath = "files/generated_word.json";
    }

    public static DailyWordOrm getInstance() {
        return instance;
    }

    public synchronized DailyWord getDailyWord() {
        if (dailyWord == null) {
            this.caricaDaFile();
        }
        try {
            return (DailyWord) dailyWord.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void updateWord(String word) {
        this.dailyWord = new DailyWord(word);
        this.salvaSuFile();
    }

    @Override
    protected synchronized void caricaDaFile() {
        try (FileReader reader = new FileReader(filePath)) {
            Type dailyWordType = new TypeToken<DailyWord>() {
            }.getType();
            this.dailyWord = gson.fromJson(reader, dailyWordType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected synchronized Object modelToSaveToJson() {
        return dailyWord;
    }

    public boolean isParolaInLista(String parola) {
        // This method only reads from a file and doesn't modify shared state, so no need for synchronization.
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(parola)) {
                    return true;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public synchronized DailyWord generaESalvaNuovaParola() {
        String word = getParolaRandomDaFile();
        this.dailyWord = new DailyWord(word);
        this.salvaSuFile();
        return dailyWord;
    }

    /**
     * OPERAZIONI PAROLE NEL FILE
     **/
    private static int getIndiceLineaRandom(String filePath) {
        int numeroLinee = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while (reader.readLine() != null) {
                numeroLinee++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Genera un numero casuale tra 0 (incluso) e lineCount (escluso)
        Random random = new Random();

        return random.nextInt(numeroLinee);
    }

    /**
     * Questo metodo legge un file di testo e restituisce una riga casuale.
     * È stato fatto per evitare di caricare in memoria l'intero file di testo.
     * Bisogna però leggere il file due volte: una per contare il numero di righe
     * e una per leggere la riga casuale.
     */
    private String getParolaRandomDaFile() {
        String filePath = "files/word_list.txt";

        // Conta il numero di righe nel file
        int indiceLineaRandom = getIndiceLineaRandom(filePath);

        // Leggi la riga casuale
        String lineaRandom;
        int indiceLineaCorrente = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while ((lineaRandom = reader.readLine()) != null) {
                if (indiceLineaCorrente == indiceLineaRandom) {
                    break;
                }
                indiceLineaCorrente++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return lineaRandom;
    }
}
