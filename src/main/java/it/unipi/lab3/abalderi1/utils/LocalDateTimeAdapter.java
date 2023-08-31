package it.unipi.lab3.abalderi1.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Un adapter GSON per serializzare e deserializzare oggetti {@link LocalDateTime}.
 */
public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    /**
     * Il formattatore di date e orari utilizzato per la serializzazione e deserializzazione.
     */
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Scrive l'oggetto {@link LocalDateTime} nel {@link JsonWriter} fornito.
     *
     * @param out Il writer JSON.
     * @param value L'oggetto {@link LocalDateTime} da serializzare.
     * @throws IOException se si verifica un errore di I/O.
     */
    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        out.value(value.format(formatter));
    }

    /**
     * Legge un oggetto {@link LocalDateTime} dal {@link JsonReader} fornito.
     *
     * @param in Il reader JSON.
     * @return L'oggetto {@link LocalDateTime} deserializzato.
     * @throws IOException se si verifica un errore di I/O.
     */
    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        return LocalDateTime.parse(in.nextString(), formatter);
    }
}
