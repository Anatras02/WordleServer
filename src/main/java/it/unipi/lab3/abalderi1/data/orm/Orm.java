package it.unipi.lab3.abalderi1.data.orm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unipi.lab3.abalderi1.utils.LastTaskExecutor;
import it.unipi.lab3.abalderi1.utils.LocalDateTimeAdapter;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public abstract class Orm implements AutoCloseable {
    // È stata usata una classe LastTaskExecutor per evitare che il salvataggio su file
    // venga eseguito più volte in contemporanea, quello che succede è che viene eseguito
    // solo l'ultimo task che viene passato all' executor, gli altri vengono scartati.
    // Questo perchè l'ultimo task è quello che contiene i dati più aggiornati.
    private final LastTaskExecutor executor = new LastTaskExecutor();

    protected String filePath;
    protected Gson gson;

    Orm() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();
    }

    protected abstract void caricaDaFile();

    protected abstract Object modelToSaveToJson();

    private void salvaSuFileSync() {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(modelToSaveToJson(), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void salvaSuFile() {
        executor.execute(this::salvaSuFileSync);
    }

    @Override
    public void close() {
        executor.shutdown();
    }
}
