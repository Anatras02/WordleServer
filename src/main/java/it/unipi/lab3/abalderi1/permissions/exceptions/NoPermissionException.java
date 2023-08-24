package it.unipi.lab3.abalderi1.permissions.exceptions;

public class NoPermissionException extends Exception {
    public NoPermissionException(String permission) {
        super("La view richiede il permesso " + permission + " per poter essere eseguita");
    }
}
