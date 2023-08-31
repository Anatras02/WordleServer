package it.unipi.lab3.abalderi1.data.exceptions;

/**
 * Eccezione personalizzata che indica che una risorsa o un elemento esiste già.
 */
public class AlreadyExistException extends Exception {
    public AlreadyExistException(String message) {
        super(message);
    }
}
