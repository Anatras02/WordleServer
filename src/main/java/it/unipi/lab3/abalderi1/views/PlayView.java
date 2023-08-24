package it.unipi.lab3.abalderi1.views;

import it.unipi.lab3.abalderi1.data.Game;
import it.unipi.lab3.abalderi1.data.User;
import it.unipi.lab3.abalderi1.permissions.IsUserLoggedPermission;
import it.unipi.lab3.abalderi1.permissions.Permission;
import it.unipi.lab3.abalderi1.protocol.Request;
import it.unipi.lab3.abalderi1.protocol.Response;

import java.util.ArrayList;
import java.util.List;

public class PlayView extends View {
    @Override
    protected List<Permission> getPermissions() {
        return List.of(new IsUserLoggedPermission());
    }

    @Override
    public Response handleRequestWithPermissions(Request request, User user) {
        Game lastGame = user.getLastGame();

        if (lastGame == null || !lastGame.isDiOggi()) {
            lastGame = new Game();

            user.addGame(lastGame);
            user.salva();
        }


        if (lastGame.isPartitaFinita() && lastGame.isDiOggi()) {
            return new Response("400", getMessageJson("Hai già giocato oggi"));
        }

        if(lastGame.isDiOggi()) {
            return new Response("400", getMessageJson("Partita già iniziata"));
        }

        return new Response("200", getMessageJson("Partita inizializzata"));
    }
}
