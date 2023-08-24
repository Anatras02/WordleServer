package it.unipi.lab3.abalderi1.views;

import it.unipi.lab3.abalderi1.data.User;
import it.unipi.lab3.abalderi1.data.exceptions.AlreadyExistException;
import it.unipi.lab3.abalderi1.protocol.Request;
import it.unipi.lab3.abalderi1.protocol.Response;

public class RegistrationView extends View {
    public Response handleRequestWithPermissions(Request request, User user) {
        String username = request.getParam("username");
        String password = request.getParam("password");

        if (username == null || password == null) {
            return new Response("400", getMessageJson("Username o password mancanti"), user);
        }

        try {
            user = User.register(username, password);

            return new Response("200", user.toJson(), user);
        } catch (AlreadyExistException e) {
            return new Response("400", getMessageJson("Utente già registrato"), user);
        }


    }
}

