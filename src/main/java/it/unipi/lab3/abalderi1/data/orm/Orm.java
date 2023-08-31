package it.unipi.lab3.abalderi1.data.orm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unipi.lab3.abalderi1.utils.LastTaskExecutor;
import it.unipi.lab3.abalderi1.utils.LocalDateTimeAdapter;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Classe astratta {@code Orm} rappresenta un Object-Relational Mapping (ORM) per manipolare dati.
 * Fornisce metodi per caricare dati da file e salvarli.
 */
public abstract class Orm implements AutoCloseable {
    // È stata usata una classe LastTaskExecutor per evitare che il salvataggio su file
    // venga eseguito più volte in contemporanea, quello che succede è che viene eseguito
    // solo l'ultimo task che viene passato all' executor, gli altri vengono scartati.
    // Questo perchè l'ultimo task è quello che contiene i dati più aggiornati.
    private final LastTaskExecutor executor = new LastTaskExecutor();

    protected String filePath;
    protected Gson gson;

    /**
     * Costruttore che inizializza un'istanza di {@link Gson} con le impostazioni desiderate.
     */
    Orm() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();
    }

    /**
     * Carica il modello di dati da un file.
     */
    protected abstract void caricaDaFile();

    /**
     * Restituisce il modello sotto forma di oggetto JSON da salvare.
     *
     * @return un oggetto rappresentante il modello da salvare in formato JSON.
     */
    protected abstract Object modelToSaveToJson();

    /**
     * Salva il modello sincronamente su file.
     */
    private void salvaSuFileSync() {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(modelToSaveToJson(), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Salva il modello su file in modo asincrono. Se più task di salvataggio vengono schedulati in successione,
     * solo l'ultimo verrà effettivamente eseguito, garantendo che i dati più recenti siano quelli che vengono
     * salvati.
     */
    public void salvaSuFile() {
        executor.execute(this::salvaSuFileSync);
    }

    /**
     * Chiude le risorse associate a questo ORM.
     */
    @Override
    public void close() {
        executor.shutdown();
    }
}
