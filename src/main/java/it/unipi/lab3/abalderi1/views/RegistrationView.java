package it.unipi.lab3.abalderi1.views;

import it.unipi.lab3.abalderi1.data.User;
import it.unipi.lab3.abalderi1.data.exceptions.AlreadyExistException;
import it.unipi.lab3.abalderi1.protocol.Request;
import it.unipi.lab3.abalderi1.protocol.Response;

/**
 * La classe {@code RegistrationView} gestisce le richieste di registrazione degli utenti.
 */
public class RegistrationView extends View {

    /**
     * Elabora la richiesta di registrazione.
     *
     * @param request La richiesta inviata dal client.
     * @param user    L'utente associato alla richiesta.
     * @return Una risposta che contiene il risultato del tentativo di registrazione.
     */
    public Response handleRequestWithPermissions(Request request, User user) {
        String username = request.getParam("username");
        String password = request.getParam("password");

        if (username == null || password == null) {
            return new Response("MISSING", "Username o password mancanti", user);
        }

        try {
            user = User.register(username, password);

            return new Response("SUCCESS", user.toJson(), user);
        } catch (AlreadyExistException e) {
            return new Response("ALREADY_REGISTERED", "Utente già registrato", user);
        }


    }
}

