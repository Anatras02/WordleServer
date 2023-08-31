package it.unipi.lab3.abalderi1.views;

import com.google.gson.JsonObject;
import it.unipi.lab3.abalderi1.data.User;
import it.unipi.lab3.abalderi1.permissions.Permission;
import it.unipi.lab3.abalderi1.permissions.exceptions.NoPermissionException;
import it.unipi.lab3.abalderi1.protocol.Request;
import it.unipi.lab3.abalderi1.protocol.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * La classe astratta {@code View} rappresenta una vista dell'applicazione.
 * Ogni vista ha il compito di gestire una particolare richiesta del client,
 * verificare i permessi dell'utente e generare una risposta appropriata.
 */
public abstract class View {
    /**
     * Ottiene una lista di permessi associati a questa vista. Di default, la lista è vuota,
     * ma le sottoclassi possono sovrascrivere questo metodo per specificare i propri permessi.
     *
     * @return una lista di permessi.
     */
    protected List<Permission> getPermissions() {
        return new ArrayList<>();
    }

    /**
     * Controlla se un utente ha tutti i permessi necessari per accedere a questa vista.
     *
     * @param user l'utente da verificare.
     * @return true se l'utente ha tutti i permessi necessari, false altrimenti.
     * @throws NoPermissionException se l'utente non ha un determinato permesso.
     */
    private boolean checkPermissions(User user) throws NoPermissionException {
        for (Permission permission : getPermissions()) {
            if (!permission.hasPermission(user, user.getLastGame())) {
                throw new NoPermissionException(permission.getPermissionName());
            }
        }

        return true;
    }

    /**
     * Gestisce la richiesta e verifica i permessi dell'utente prima di elaborare la richiesta.
     * Se l'utente ha i permessi appropriati, passa la richiesta al metodo {@code handleRequestWithPermissions}.
     *
     * @param request la richiesta del client.
     * @param user l'utente associato alla richiesta.
     * @return una risposta basata sulla richiesta e sull'utente.
     * @throws NoPermissionException se l'utente non ha i permessi necessari.
     */
    public Response handle(Request request, User user) throws NoPermissionException {
        if (checkPermissions(user)) {
            return handleRequestWithPermissions(request, user);
        }

        return null;
    }

    /**
     * Metodo astratto che gestisce la richiesta quando si è sicuri che l'utente abbia i permessi necessari.
     * Deve essere sovrascritto dalle sottoclassi per definire la logica specifica di ogni vista.
     *
     * @param request la richiesta del client.
     * @param user l'utente associato alla richiesta.
     * @return una risposta basata sulla richiesta e sull'utente.
     */
    protected abstract Response handleRequestWithPermissions(Request request, User user);
}
