package it.unipi.lab3.abalderi1.views;

import com.google.gson.Gson;
import it.unipi.lab3.abalderi1.data.User;
import it.unipi.lab3.abalderi1.permissions.IsUserLoggedPermission;
import it.unipi.lab3.abalderi1.permissions.Permission;
import it.unipi.lab3.abalderi1.protocol.Request;
import it.unipi.lab3.abalderi1.protocol.Response;

import java.util.List;

/**
 * La classe {@code StatisticView} gestisce le richieste di visualizzazione delle statistiche da parte degli utenti.
 */
public class StatisticView extends View {
    /**
     * Ottiene i permessi necessari per visualizzare le statistiche.
     *
     * @return Una lista dei permessi necessari.
     */
    @Override
    protected List<Permission> getPermissions() {
        return List.of(new IsUserLoggedPermission());
    }

    /**
     * Elabora la richiesta di visualizzazione delle statistiche.
     *
     * @param request La richiesta inviata dal client.
     * @param user    L'utente associato alla richiesta.
     * @return Una risposta che contiene le statistiche dell'utente.
     */
    @Override
    public Response handleRequestWithPermissions(Request request, User user) {
        user.getStatistiche();

        Gson gson = new Gson();
        String body = gson.toJson(user.getStatistiche());

        return new Response("SUCCESS", body);
    }
}
