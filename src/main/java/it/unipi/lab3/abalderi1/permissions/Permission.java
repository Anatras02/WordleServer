package it.unipi.lab3.abalderi1.permissions;

import it.unipi.lab3.abalderi1.data.Game;
import it.unipi.lab3.abalderi1.data.User;

public interface Permission {
    String getPermissionName();

    boolean hasPermission(User user, Game lastGame);
}
