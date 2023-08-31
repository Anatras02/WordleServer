package it.unipi.lab3.abalderi1.permissions.exceptions;

/**
 * Eccezione lanciata quando una vista non ha il permesso richiesto per essere eseguita.
 */
public class NoPermissionException extends Exception {
    /**
     * Costruttore che inizializza l'eccezione con un messaggio specifico relativo al permesso mancante.
     *
     * @param permission Il nome del permesso che manca.
     */
    public NoPermissionException(String permission) {
        super("La view richiede il permesso " + permission + " per poter essere eseguita");
    }
}
