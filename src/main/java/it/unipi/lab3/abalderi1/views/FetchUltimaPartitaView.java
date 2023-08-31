package it.unipi.lab3.abalderi1.views;

import com.google.gson.Gson;
import it.unipi.lab3.abalderi1.data.User;
import it.unipi.lab3.abalderi1.protocol.Request;
import it.unipi.lab3.abalderi1.protocol.Response;

import java.util.List;

/**
 * La classe {@code FetchUltimaPartitaView} gestisce le richieste per recuperare l'ultima partita giocata dall'utente.
 */
public class FetchUltimaPartitaView extends View {
    /**
     * Elabora la richiesta per recuperare l'ultima partita dell'utente.
     *
     * @param request La richiesta inviata dal client.
     * @param user L'utente che effettua la richiesta.
     * @return Una risposta che contiene l'ultima partita o un messaggio d'errore se l'utente non ha ancora giocato.
     */
    @Override
    protected Response handleRequestWithPermissions(Request request, User user) {
        Gson gson = new Gson();

        if (user.getLastGame() == null) {
            return new Response("404", gson.toJson("Non hai ancora giocato"));
        }

        return new Response("SUCCESS", gson.toJson(user.getLastGame()));
    }
}
