package it.unipi.lab3.abalderi1.protocol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unipi.lab3.abalderi1.data.User;
import it.unipi.lab3.abalderi1.utils.LocalDateTimeAdapter;
import netscape.javascript.JSObject;

import java.time.LocalDateTime;

public class Response {
    private final String status;
    private final String body;
    private User user;

    public Response(String status, String body) {
        this.status = status;
        this.body = body;
    }

    public Response(String status, String body, User user) {
        this.status = status;
        this.body = body;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String toString() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        return gson.toJson(this);
    }
}
