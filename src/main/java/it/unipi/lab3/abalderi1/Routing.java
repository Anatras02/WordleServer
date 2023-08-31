package it.unipi.lab3.abalderi1;

import it.unipi.lab3.abalderi1.protocol.Request;
import it.unipi.lab3.abalderi1.views.View;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * La classe {@code Routing} gestisce le rotte dell'applicazione, associando le stringhe
 * endpoint alle rispettive classi view. Questo permette di creare dinamicamente istanze delle
 * view basate sulle richieste dei client.
 */
public class Routing {
    /**
     * Mappa statica che associa gli endpoint alle rispettive classi view.
     */
    public static HashMap<String, Class<? extends View>> routes = new HashMap<>();

    static {
        routes = new HashMap<>();

        routes.put("registration", it.unipi.lab3.abalderi1.views.RegistrationView.class);
        routes.put("login", it.unipi.lab3.abalderi1.views.LoginView.class);
        routes.put("play", it.unipi.lab3.abalderi1.views.PlayView.class);
        routes.put("send_word", it.unipi.lab3.abalderi1.views.SendWordView.class);
        routes.put("statistic", it.unipi.lab3.abalderi1.views.StatisticView.class);
        routes.put("share", it.unipi.lab3.abalderi1.views.ShareView.class);
        routes.put("ultima_partita", it.unipi.lab3.abalderi1.views.FetchUltimaPartitaView.class);
    }

    /**
     * Crea e restituisce un'istanza della view associata all'endpoint specificato.
     * La mappa statica {@code routes} viene utilizzata per trovare la classe view corretta.
     *
     * @param endpoint stringa che rappresenta l'endpoint per cui creare la view.
     * @param request oggetto request associato alla richiesta (non utilizzato in questo metodo ma può essere utile in futuro).
     * @return un'istanza della classe {@code View} associata all'endpoint specificato.
     * @throws NoSuchMethodException se il costruttore senza argomenti della classe view non è stato trovato.
     * @throws InvocationTargetException se la chiamata al costruttore ha generato un'eccezione.
     * @throws InstantiationException se la classe non può essere istanziata (ad esempio, se è un'interfaccia o una classe astratta).
     * @throws IllegalAccessException se il costruttore non è accessibile.
     */
    public static View createEndpointView(String endpoint, Request request) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<? extends View> viewClass = routes.get(endpoint);
        if (viewClass == null) {
            throw new IllegalArgumentException("Invalid endpoint: " + endpoint);
        }

        return viewClass.getDeclaredConstructor().newInstance();
    }
}
