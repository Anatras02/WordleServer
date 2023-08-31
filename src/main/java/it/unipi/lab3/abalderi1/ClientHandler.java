package it.unipi.lab3.abalderi1;

import it.unipi.lab3.abalderi1.data.User;
import it.unipi.lab3.abalderi1.permissions.exceptions.NoPermissionException;
import it.unipi.lab3.abalderi1.protocol.Request;
import it.unipi.lab3.abalderi1.protocol.Response;
import it.unipi.lab3.abalderi1.views.View;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

/**
 * La classe {@code ClientHandler} gestisce le comunicazioni individuali con i client.
 * Ogni client connesso viene servito da un'istanza separata di questa classe.
 */
public class ClientHandler implements Runnable {
    /**
     * Il socket attraverso il quale la comunicazione con il client avviene.
     */
    Socket socket;

    /**
     * Costruttore per creare un'istanza del gestore del client.
     *
     * @param socket il socket associato al client connesso.
     */
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    /**
     * Legge una stringa dal socket finché non incontra una nuova riga o la fine dello stream.
     *
     * @param inputStreamReader l'input stream da cui leggere i dati.
     * @return la stringa letta dal socket.
     * @throws IOException in caso di errori di I/O durante la lettura.
     */
    private String readFromSocket(InputStreamReader inputStreamReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int c;

        while ((c = inputStreamReader.read()) != -1 && c != '\n') {
            stringBuilder.append((char) c);
        }

        if (c == -1 && stringBuilder.isEmpty()) {
            throw new EOFException("Client has closed the connection.");
        }


        return stringBuilder.toString();
    }

    /**
     * Scrive una stringa sul socket seguita da una nuova riga.
     *
     * @param outputStreamWriter l'output stream su cui scrivere i dati.
     * @param message il messaggio da scrivere sul socket.
     * @throws IOException in caso di errori di I/O durante la scrittura.
     */
    private void writeToSocket(OutputStreamWriter outputStreamWriter, String message) throws IOException {
        outputStreamWriter.write(message);
        outputStreamWriter.write('\n');
        outputStreamWriter.flush();
    }

    /**
     * La logica principale per gestire la comunicazione con il client.
     * Legge i comandi dal client, li elabora e invia le risposte appropriate.
     */
    public void run() {
        InputStream input;
        OutputStream output;

        try {
            input = socket.getInputStream();
            output = socket.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        InputStreamReader reader = new InputStreamReader(input);
        OutputStreamWriter writer = new OutputStreamWriter(output);

        User user = null;

        while (true) {
            String command;

            try {
                try {
                    command = readFromSocket(reader);
                } catch (EOFException e) {
                    System.out.println("Client has closed the connection. Stopping the handler.");
                    break;
                }

                Request request = new Request(command);
                String endpoint = request.getEndpoint();

                Response response;

                try {
                    View View = Routing.createEndpointView(endpoint, request);

                    response = View.handle(request, user);
                    if (response.getUser() != null) {
                        user = response.getUser();
                    }


                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    response = new Response("500", "Internal server error");
                } catch (NoPermissionException e) {
                    response = new Response("INVALID_PERMISSION", e.getMessage());
                }


                writeToSocket(writer, response.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
