package it.unipi.lab3.abalderi1;

import it.unipi.lab3.abalderi1.data.User;
import it.unipi.lab3.abalderi1.permissions.exceptions.NoPermissionException;
import it.unipi.lab3.abalderi1.protocol.Request;
import it.unipi.lab3.abalderi1.protocol.Response;
import it.unipi.lab3.abalderi1.views.View;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public class ClientHandler implements Runnable {
    Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

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

    private void writeToSocket(OutputStreamWriter outputStreamWriter, String message) throws IOException {
        outputStreamWriter.write(message);
        outputStreamWriter.write('\n');
        outputStreamWriter.flush();
    }

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
                    response = new Response("403", e.getMessage());
                }


                writeToSocket(writer, response.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
