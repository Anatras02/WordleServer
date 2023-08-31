package it.unipi.lab3.abalderi1.views;

import it.unipi.lab3.abalderi1.data.User;
import it.unipi.lab3.abalderi1.data.exceptions.InvalidLoginException;
import it.unipi.lab3.abalderi1.protocol.Request;
import it.unipi.lab3.abalderi1.protocol.Response;

/**
 * La classe {@code LoginView} gestisce le richieste di login degli utenti.
 */
public class LoginView extends View {
    /**
     * Elabora la richiesta di login.
     *
     * @param request La richiesta inviata dal client.
     * @param user L'utente associato alla richiesta (null in questo caso).
     * @return Una risposta che contiene il risultato del tentativo di login.
     */
    public Response handleRequestWithPermissions(Request request, User user) {
        String username = request.getParam("username");
        String password = request.getParam("password");

        if (username == null || password == null) {
            return new Response("MISSING", "Username o password mancanti", user);
        }

        try {
            user = User.login(username, password);

            return new Response("SUCCESS", user.toJson(), user);
        } catch (InvalidLoginException e) {
            return new Response("INVALID_LOGIN", e.getMessage(), user);
        }
    }
}
