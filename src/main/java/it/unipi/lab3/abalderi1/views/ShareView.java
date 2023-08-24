package it.unipi.lab3.abalderi1.views;

import com.google.gson.JsonObject;
import it.unipi.lab3.abalderi1.data.Game;
import it.unipi.lab3.abalderi1.data.User;
import it.unipi.lab3.abalderi1.permissions.IsUserLoggedPermission;
import it.unipi.lab3.abalderi1.permissions.Permission;
import it.unipi.lab3.abalderi1.permissions.exceptions.IsGameFinishedPermission;
import it.unipi.lab3.abalderi1.protocol.Request;
import it.unipi.lab3.abalderi1.protocol.Response;
import it.unipi.lab3.abalderi1.utils.MulticastSocketUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShareView extends View {
    @Override
    protected List<Permission> getPermissions() {
        return List.of(new IsUserLoggedPermission(), new IsGameFinishedPermission());
    }

    private String getPartitaJson(User user, Game game) {
        JsonObject body = new JsonObject();
        body.addProperty("username", user.getUsername());
        body.addProperty("tentativi", game.getTentativi());
        body.addProperty("partita", game.getParole().toString());

        return body.toString();
    }


    @Override
    protected Response handleRequestWithPermissions(Request request, User user) {

        try {
            MulticastSocketUtil.share(getPartitaJson(user, user.getLastGame()));
        } catch (IOException e) {
            return new Response("500", getMessageJson("Errore nella condivisione della partita"));
        }

        return new Response("200", getMessageJson("Partita condivisa"));
    }
}
