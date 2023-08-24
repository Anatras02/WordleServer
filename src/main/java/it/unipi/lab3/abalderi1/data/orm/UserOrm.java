package it.unipi.lab3.abalderi1.data.orm;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unipi.lab3.abalderi1.data.User;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class UserOrm extends Orm {
    private static final UserOrm instance = new UserOrm();

    private ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    private UserOrm() {
        super();

        this.filePath = "files/user_data.json";

        caricaDaFile();
    }

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

    public synchronized void addUser(User user) {
        this.users.put(user.getUsername(), user);

        salvaSuFile();
    }

    protected synchronized void updateUser(String username, User updatedUser) {
        this.users.put(username, updatedUser);

        salvaSuFile();
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public static UserOrm getInstance() {
        return instance;
    }

    @Override
    protected Object modelToSaveToJson() {
        return users.values();
    }
}