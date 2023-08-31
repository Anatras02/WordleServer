package it.unipi.lab3.abalderi1.data.orm;

import com.google.gson.reflect.TypeToken;
import it.unipi.lab3.abalderi1.data.User;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Classe ORM (Object-Relational Mapping) per la gestione degli utenti.
 * Fornisce funzionalità per caricare, salvare, aggiungere e recuperare informazioni sugli utenti.
 */
public class UserOrm extends Orm {
    private static UserOrm instance;

    private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    /**
     * Costruttore privato per implementare il pattern Singleton.
     * Inizializza il percorso del file e carica i dati degli utenti.
     */
    private UserOrm() {
        super();

        this.filePath = "files/user_data.json";

        caricaDaFile();
    }

    /**
     * Carica gli utenti dal file JSON specificato in 'filePath' e li inserisce nella mappa concorrente.
     */
    protected void caricaDaFile() {
        try (FileReader reader = new FileReader(filePath)) {
            Type userListType = new TypeToken<List<User>>() {
            }.getType();

            List<User> users = gson.fromJson(reader, userListType);

            synchronized (this) {
                for (User user : users) {
                    this.users.put(user.getUsername(), user);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Aggiunge un nuovo utente alla mappa e salva le modifiche nel file.
     *
     * @param user L'utente da aggiungere.
     */
    public synchronized void addUser(User user) {
        this.users.put(user.getUsername(), user);

        salvaSuFile();
    }

    /**
     * Aggiorna un utente specificato nel sistema e salva le modifiche nel file.
     *
     * @param username    L'username dell'utente da aggiornare.
     * @param updatedUser L'utente con le informazioni aggiornate.
     */
    protected synchronized void updateUser(String username, User updatedUser) {
        this.users.put(username, updatedUser);

        salvaSuFile();
    }

    /**
     * Restituisce le informazioni di un utente basandosi sul suo username.
     * La richiesta è efficiente grazie all'utilizzo di una mappa concorrente
     * che permette l'accesso in O(1).
     *
     * @param username L'username dell'utente da recuperare.
     * @return Un oggetto User che rappresenta l'utente o null se l'utente non esiste.
     */
    public User getUser(String username) {
        return users.get(username);
    }

    /**
     * Invalida le partite di tutti gli utenti e salva le modifiche nel file.
     */
    public synchronized void invalidateGames() {
        for (User user : users.values()) {
            user.invalidateGames();
        }

        salvaSuFile();
    }

    /**
     * Fornisce l'istanza unica (Singleton) di UserOrm, creando una nuova istanza se non esiste ancora.
     *
     * @return L'istanza unica di UserOrm.
     */
    public static synchronized UserOrm getInstance() {
        if (instance == null) {
            instance = new UserOrm();
        }
        return instance;
    }

    /**
     * Prepara la collezione di utenti per essere salvata nel file in formato JSON.
     *
     * @return Una collezione di oggetti User.
     */
    @Override
    protected Object modelToSaveToJson() {
        return users.values();
    }
}