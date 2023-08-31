package it.unipi.lab3.abalderi1.permissions;

import it.unipi.lab3.abalderi1.data.Game;
import it.unipi.lab3.abalderi1.data.User;

/**
 * Permesso che verifica se una partita è valida.
 */
public class IsGameValidPermission implements Permission {
    @Override
    public String getPermissionName() {
        return "PartitaNonValida";
    }

    @Override
    public boolean hasPermission(User user, Game lastGame) {
        return lastGame != null && lastGame.isValid();
    }
}
