package it.unipi.lab3.abalderi1.permissions;

import it.unipi.lab3.abalderi1.data.Game;
import it.unipi.lab3.abalderi1.data.User;

public class IsGameNotFinishedPermission implements Permission {
    @Override
    public String getPermissionName() {
        return "GiocoNonFinito";
    }

    @Override
    public boolean hasPermission(User user, Game lastGame) {
        return lastGame != null && !lastGame.isPartitaFinita();
    }
}
