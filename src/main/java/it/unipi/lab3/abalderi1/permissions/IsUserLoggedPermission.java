package it.unipi.lab3.abalderi1.permissions;

import it.unipi.lab3.abalderi1.data.Game;
import it.unipi.lab3.abalderi1.data.User;

public class IsUserLoggedPermission implements Permission {
    public String getPermissionName() {
        return "UtenteLoggato";
    }

    @Override
    public boolean hasPermission(User user, Game lastGame) {
        return user != null;
    }
}
