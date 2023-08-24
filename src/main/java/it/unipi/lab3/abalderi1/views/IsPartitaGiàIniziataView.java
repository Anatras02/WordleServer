package it.unipi.lab3.abalderi1.views;

import it.unipi.lab3.abalderi1.data.Game;
import it.unipi.lab3.abalderi1.data.User;
import it.unipi.lab3.abalderi1.protocol.Request;
import it.unipi.lab3.abalderi1.protocol.Response;

public class IsPartitaGiàIniziataView extends View {

    @Override
    protected Response handleRequestWithPermissions(Request request, User user) {
        Game lastGame = user.getLastGame();

        if(lastGame != null) {
            if (lastGame.isDiOggi()) {
                return new Response("TRUE", getMessageJson("true"));
            }
        }

        return new Response("FALSE", getMessageJson("false"));
    }
}
