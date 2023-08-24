package it.unipi.lab3.abalderi1.views;

import it.unipi.lab3.abalderi1.data.User;
import it.unipi.lab3.abalderi1.data.exceptions.InvalidLoginException;
import it.unipi.lab3.abalderi1.protocol.Request;
import it.unipi.lab3.abalderi1.protocol.Response;

public class LoginView extends View {
    public Response handleRequestWithPermissions(Request request, User user) {
        String username = request.getParam("username");
        String password = request.getParam("password");

        if (username == null || password == null) {
            return new Response("400", getMessageJson("Username o password mancanti"), user);
        }

        try {
            user = User.login(username, password);

            return new Response("200", user.toJson(), user);
        } catch (InvalidLoginException e) {
            return new Response("400", getMessageJson(e.getMessage()), user);
        }
    }
}
