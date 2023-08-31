package it.unipi.lab3.abalderi1.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unipi.lab3.abalderi1.data.exceptions.AlreadyExistException;
import it.unipi.lab3.abalderi1.data.exceptions.InvalidLoginException;
import it.unipi.lab3.abalderi1.data.orm.Orm;
import it.unipi.lab3.abalderi1.data.orm.UserOrm;
import it.unipi.lab3.abalderi1.utils.HashUtil;
import it.unipi.lab3.abalderi1.utils.LocalDateTimeAdapter;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Classe che rappresenta un utente con sessioni di gioco associate.
 * <p>
 * Per informazioni sull'autenticazione e la registrazione, si consiglia di leggere la documentazione correlata.
 *
 * @see UserOrm per dettagli sull'ORM associato.
 */

public class User extends Model {
    private static final UserOrm userOrm = UserOrm.getInstance();
    private final String username;
    private final String password;

    private final PriorityQueue<Game> games = new PriorityQueue<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    protected Orm getOrm() {
        return userOrm;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    /**
     * Registra un nuovo utente nel sistema.
     *
     * @param username L'username dell'utente da registrare.
     * @param password La password dell'utente da registrare.
     * @return L'oggetto User appena creato.
     * @throws AlreadyExistException Se l'username specificato è già presente nel sistema.
     * @see UserOrm#addUser(User) per dettagli sull'implementazione della registrazione sul file.
     */
    public static User register(String username, String password) throws AlreadyExistException {
        if (userOrm.getUser(username) != null) {
            throw new AlreadyExistException("Username " + username + " già esistente");
        }

        String hashedPassword = HashUtil.hashWithSHA256(password);

        User user = new User(username, hashedPassword);
        userOrm.addUser(user);

        return user;
    }

    /**
     * Autentica un utente esistente nel sistema.
     *
     * @param username L'username dell'utente da autenticare.
     * @param password La password dell'utente da autenticare.
     * @return L'oggetto User se l'autenticazione ha successo.
     * @throws InvalidLoginException Se le credenziali fornite sono errate.
     * @see UserOrm#getUser(String) per dettagli sull'implementazione dell'ottenimento dei dati dell'utente dal file.
     */
    public static User login(String username, String password) throws InvalidLoginException {
        User user = userOrm.getUser(username);

        if (user == null) {
            throw new InvalidLoginException("Utente " + username + " non esistente");
        }

        if (!user.password.equals(HashUtil.hashWithSHA256(password))) {
            throw new InvalidLoginException("Password errata");
        }

        return user;
    }

    /**
     * Aggiorna l'ultima sessione di gioco dell'utente.
     *
     * @param game L'oggetto Game che rappresenta la sessione aggiornata.
     */
    public void editLastGame(Game game) {
        games.poll();
        games.offer(game);
    }

    /**
     * Aggiunge una nuova sessione di gioco all'utente.
     *
     * @param game L'oggetto Game che rappresenta la sessione di gioco da aggiungere.
     */
    public void addGame(Game game) {
        this.games.add(game);
    }

    /**
     * Restituisce tutte le sessioni di gioco associate all'utente.
     *
     * @return Una PriorityQueue contenente tutte le sessioni di gioco dell'utente.
     */
    public PriorityQueue<Game> getGames() {
        return this.games;
    }

    /**
     * Restituisce l'ultima sessione di gioco effettuata dall'utente.
     *
     * @return L'oggetto Game che rappresenta l'ultima sessione di gioco. Null se non sono presenti sessioni.
     */
    public Game getLastGame() {
        if (this.games.isEmpty())
            return null;

        return this.games.peek();
    }

    /**
     * Calcola e restituisce le statistiche dell'utente basate sulle sessioni di gioco effettuate.
     *
     * @return L'oggetto Statistica contenente tutte le informazioni statistiche dell'utente.
     * @see Statistica per ulteriori dettagli sulle informazioni statistiche.
     */
    public Statistica getStatistiche() {
        Statistica statistiche = new Statistica();

        statistiche.calcola(this.games);

        return statistiche;
    }

    /**
     * Converte l'oggetto User in formato JSON.
     * Utilizza Gson per gestire la serializzazione.
     *
     * @return Una stringa in formato JSON che rappresenta l'oggetto User.
     */
    public String toJson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        return gson.toJson(this);
    }

    /**
     * Invalida tutte le sessioni di gioco associate all'utente.
     * <p>
     * Questo metodo può essere utilizzato in situazioni in cui si desidera marcare tutte le partite
     * dell'utente come non più giocabili (anche se non ancora terminate).
     */
    public void invalidateGames() {
        for (Game game : games) {
            game.setValida(false);
            game.finisci();
        }
    }
}


