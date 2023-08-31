package it.unipi.lab3.abalderi1.data.exceptions;

/**
 * Eccezione personalizzata che indica un tentativo di login non valido.
 */
public class InvalidLoginException extends Exception {
    public InvalidLoginException(String message) {
        super(message);
    }
}
