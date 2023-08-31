package it.unipi.lab3.abalderi1.views;

import it.unipi.lab3.abalderi1.data.Game;
import it.unipi.lab3.abalderi1.data.User;
import it.unipi.lab3.abalderi1.permissions.IsUserLoggedPermission;
import it.unipi.lab3.abalderi1.permissions.Permission;
import it.unipi.lab3.abalderi1.protocol.Request;
import it.unipi.lab3.abalderi1.protocol.Response;

import java.util.List;

/**
 * La classe {@code PlayView} gestisce le richieste di gioco degli utenti.
 */
public class PlayView extends View {
    /**
     * Ottiene i permessi necessari per giocare.
     *
     * @return Una lista dei permessi necessari.
     */
    @Override
    protected List<Permission> getPermissions() {
        return List.of(new IsUserLoggedPermission());
    }

    /**
     * Elabora la richiesta di gioco.
     *
     * @param request La richiesta inviata dal client.
     * @param user L'utente associato alla richiesta.
     * @return Una risposta che contiene il risultato del tentativo di gioco.
     */
    @Override
    public Response handleRequestWithPermissions(Request request, User user) {
        Game lastGame = user.getLastGame();

        if (lastGame == null || !lastGame.isValid()) {
            lastGame = new Game();

            user.addGame(lastGame);
            user.salva();
        } else {
            if (lastGame.isPartitaFinita()) return new Response("ALREADY_PLAYED", "Hai già giocato oggi");
            else return new Response("ALREADY_STARTED", "Partita già iniziata");
        }


        return new Response("SUCCESS", "Partita inizializzata");
    }
}
