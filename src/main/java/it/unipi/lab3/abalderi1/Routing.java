package it.unipi.lab3.abalderi1;

import it.unipi.lab3.abalderi1.protocol.Request;
import it.unipi.lab3.abalderi1.views.View;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class Routing {
    public static HashMap<String, Class<? extends View>> routes = new HashMap<>();

    static {
        routes = new HashMap<>();

        routes.put("registration", it.unipi.lab3.abalderi1.views.RegistrationView.class);
        routes.put("login", it.unipi.lab3.abalderi1.views.LoginView.class);
        routes.put("play", it.unipi.lab3.abalderi1.views.PlayView.class);
        routes.put("send_word", it.unipi.lab3.abalderi1.views.SendWordView.class);
        routes.put("statistic", it.unipi.lab3.abalderi1.views.StatisticView.class);
        routes.put("share", it.unipi.lab3.abalderi1.views.ShareView.class);
        routes.put("is_partita_gia_iniziata", it.unipi.lab3.abalderi1.views.IsPartitaGiàIniziataView.class);
    }

    public static View createEndpointView(String endpoint, Request request) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<? extends View> viewClass = routes.get(endpoint);
        if (viewClass == null) {
            throw new IllegalArgumentException("Invalid endpoint: " + endpoint);
        }

        return viewClass.getDeclaredConstructor().newInstance();
    }
}
