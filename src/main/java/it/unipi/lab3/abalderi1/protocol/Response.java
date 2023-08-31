package it.unipi.lab3.abalderi1.protocol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import it.unipi.lab3.abalderi1.data.User;
import it.unipi.lab3.abalderi1.utils.LocalDateTimeAdapter;
import netscape.javascript.JSObject;

import java.time.LocalDateTime;

/**
 * La classe {@code Response} rappresenta una risposta nel protocollo.
 * Fornisce funzionalità per gestire lo stato della risposta, il corpo e l'utente associato.
 */
public class Response {
    private final String status;
    private JsonObject body;
    private User user;

    /**
     * Costruttore per inizializzare la risposta con stato e corpo.
     *
     * @param status Stato della risposta.
     * @param body Corpo della risposta.
     */
    public Response(String status, String body) {
        this.status = status;
        setBody(body);
    }

    /**
     * Costruttore per inizializzare la risposta con stato, corpo e utente.
     *
     * @param status Stato della risposta.
     * @param body Corpo della risposta.
     * @param user Utente associato.
     */
    public Response(String status, String body, User user) {
        this.status = status;
        this.user = user;
        setBody(body);
    }

    /**
     * Imposta il corpo della risposta.
     *
     * @param body Corpo della risposta.
     */
    private void setBody(String body) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", body);

        this.body = jsonObject;
    }

    /**
     * Restituisce l'utente associato.
     *
     * @return L'utente.
     */
    public User getUser() {
        return user;
    }

    /**
     * Restituisce il corpo della risposta.
     *
     * @return Un JsonObject che contiene il corpo della risposta.
     */
    public JsonObject getBody() {
        return body;
    }

    /**
     * Converte questo oggetto di risposta nella sua rappresentazione JSON.
     *
     * @return Una stringa in formato JSON.
     */
    public String toString() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        return gson.toJson(this);
    }
}
