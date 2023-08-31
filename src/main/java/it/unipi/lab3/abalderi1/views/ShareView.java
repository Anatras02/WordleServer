package it.unipi.lab3.abalderi1.views;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.unipi.lab3.abalderi1.data.Game;
import it.unipi.lab3.abalderi1.data.User;
import it.unipi.lab3.abalderi1.permissions.IsUserLoggedPermission;
import it.unipi.lab3.abalderi1.permissions.Permission;
import it.unipi.lab3.abalderi1.permissions.IsGameFinishedPermission;
import it.unipi.lab3.abalderi1.protocol.Request;
import it.unipi.lab3.abalderi1.protocol.Response;
import it.unipi.lab3.abalderi1.utils.MulticastSocketUtil;

import java.io.IOException;
import java.util.List;

/**
 * La classe {@code ShareView} gestisce le richieste di condivisione delle partite da parte degli utenti.
 */
public class ShareView extends View {
    /**
     * Ottiene i permessi necessari per condividere una partita.
     *
     * @return Una lista dei permessi necessari.
     */
    @Override
    protected List<Permission> getPermissions() {
        return List.of(new IsUserLoggedPermission(), new IsGameFinishedPermission());
    }

    /**
     * Ottiene una rappresentazione JSON della partita per la condivisione.
     *
     * @param user L'utente associato alla partita.
     * @param game La partita da condividere.
     * @return Una rappresentazione JSON della partita.
     */
    private String getPartitaJson(User user, Game game) {
        JsonObject body = new JsonObject();
        body.addProperty("username", user.getUsername());
        body.addProperty("tentativi", game.getTentativi());

        JsonArray partitaArray = new Gson().toJsonTree(game.getParole()).getAsJsonArray();
        body.add("partita", partitaArray);

        return body.toString();
    }

    /**
     * Elabora la richiesta di condivisione di una partita.
     *
     * @param request La richiesta inviata dal client.
     * @param user L'utente associato alla richiesta.
     * @return Una risposta che indica il successo o l'errore della condivisione.
     */
    @Override
    protected Response handleRequestWithPermissions(Request request, User user) {

        try {
            MulticastSocketUtil.share(getPartitaJson(user, user.getLastGame()));
        } catch (IOException e) {
            return new Response("GENERIC_ERROR", "Errore nella condivisione della partita");
        }

        return new Response("SUCCESS", "Partita condivisa");
    }
}
