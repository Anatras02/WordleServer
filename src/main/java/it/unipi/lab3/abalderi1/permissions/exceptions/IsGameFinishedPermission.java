package it.unipi.lab3.abalderi1.permissions.exceptions;

import it.unipi.lab3.abalderi1.data.Game;
import it.unipi.lab3.abalderi1.data.User;
import it.unipi.lab3.abalderi1.permissions.Permission;


public class IsGameFinishedPermission implements Permission {
    @Override
    public String getPermissionName() {
        return "PartitaNonFinita";
    }

    @Override
    public boolean hasPermission(User user, Game lastGame) {
        return lastGame == null || lastGame.isPartitaFinita();
    }

}
