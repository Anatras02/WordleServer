package it.unipi.lab3.abalderi1.permissions;

import it.unipi.lab3.abalderi1.data.Game;
import it.unipi.lab3.abalderi1.data.User;

/**
 * Interfaccia per rappresentare un permesso. Fornisce metodi per ottenere il nome del permesso e
 * verificare se un utente ha il permesso specifico.
 */
public interface Permission {
    /**
     * Restituisce il nome del permesso.
     *
     * @return Il nome del permesso.
     */
    String getPermissionName();

    /**
     * Verifica se l'utente ha il permesso.
     *
     * @param user L'utente da verificare.
     * @param lastGame L'ultima partita dell'utente.
     * @return {@code true} se l'utente ha il permesso, {@code false} altrimenti.
     */
    boolean hasPermission(User user, Game lastGame);
}
