

package it.unipi.lab3.abalderi1.data.orm;

import com.google.gson.reflect.TypeToken;
import it.unipi.lab3.abalderi1.data.DailyWord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Random;

/**
 * Classe ORM per gestire l'operatività legata alla parola del giorno.
 * Si occupa del caricamento, salvataggio e generazione delle parole giornaliere.
 *
 * @see Orm per ulteriori dettagli sulla superclasse e la gestione generale ORM.
 */
public class DailyWordOrm extends Orm {
    /**
     * Singleton instance di DailyWordOrm.
     */
    private static final DailyWordOrm instance = new DailyWordOrm();

    private DailyWord dailyWord;

    /**
     * Costruttore privato per implementazione Singleton.
     */
    private DailyWordOrm() {
        super();
        this.filePath = "files/generated_word.json";
    }

    public static DailyWordOrm getInstance() {
        return instance;
    }

    /**
     * Restituisce la parola del giorno corrente. Se non è già stata caricata, la carica dal file.
     *
     * @return La parola del giorno come oggetto DailyWord.
     */
    public synchronized DailyWord getDailyWord() {
        if (dailyWord == null) {
            this.caricaDaFile();
        }

        return dailyWord.clone();
    }

    /**
     * Aggiorna la parola del giorno con la parola fornita e salva le modifiche su file.
     *
     * @param word La nuova parola del giorno.
     */
    public synchronized void updateWord(String word) {
        this.dailyWord = new DailyWord(word);
        this.salvaSuFile();
    }

    /**
     * Carica la parola del giorno dal file. Questa operazione è thread-safe.
     */
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

    /**
     * Restituisce il modello DailyWord attuale per essere salvato in formato JSON.
     *
     * @return L'oggetto DailyWord corrente.
     */
    @Override
    protected synchronized Object modelToSaveToJson() {
        return dailyWord;
    }

    /**
     * Verifica se una parola specificata è presente nel file delle parole.
     *
     * @param parola La parola da verificare.
     * @return true se la parola è presente, false altrimenti.
     */
    public boolean isParolaInLista(String parola) {
        String filePath = "files/word_list.txt";

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

    /**
     * Genera una nuova parola casuale, la imposta come parola del giorno e la salva su file.
     *
     * @return La nuova parola del giorno come oggetto DailyWord.
     */
    public synchronized DailyWord generaESalvaNuovaParola() {
        String word = getParolaRandomDaFile();
        this.dailyWord = new DailyWord(word);
        this.salvaSuFile();
        return dailyWord;
    }

    /**
     * Restituisce un indice di riga casuale da un file di testo.
     *
     * @param filePath Il percorso del file da cui estrarre la riga.
     * @return L'indice della riga casuale.
     */
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
     *
     * @return La parola casuale estratta dal file.
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
