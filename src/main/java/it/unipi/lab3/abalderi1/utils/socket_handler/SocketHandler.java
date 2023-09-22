package it.unipi.lab3.abalderi1.utils.socket_handler;

import it.unipi.lab3.abalderi1.protocol.Request;
import it.unipi.lab3.abalderi1.protocol.Response;

public interface SocketHandler {
    void accept();

    void close();

    Request read();

    void write(Response response);
}
