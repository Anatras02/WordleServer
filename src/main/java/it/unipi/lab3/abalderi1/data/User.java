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

public class User extends Model {
    private static UserOrm userOrm = UserOrm.getInstance();
    private String username;
    private String password;

    private PriorityQueue<Game> games = new PriorityQueue<>();

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


    public static User register(String username, String password) throws AlreadyExistException {
        if (userOrm.getUser(username) != null) {
            throw new AlreadyExistException("Username " + username + " già esistente");
        }

        String hashedPassword = HashUtil.hashWithSHA256(password);

        User user = new User(username, hashedPassword);
        userOrm.addUser(user);

        return user;
    }

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

    public void editLastGame(Game game) {
        games.poll();
        games.offer(game);
    }

    public void addGame(Game game) {
        this.games.add(game);
    }

    public PriorityQueue<Game> getGames() {
        return this.games;
    }

    public Game getLastGame() {
        if (this.games.isEmpty())
            return null;

        return this.games.peek();
    }

    public Statistica getStatistiche() {
        Statistica statistiche = new Statistica();

        statistiche.calcola(this.games);

        return statistiche;
    }


    public String toJson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        return gson.toJson(this);
    }
}


