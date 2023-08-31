package it.unipi.lab3.abalderi1.permissions;

import it.unipi.lab3.abalderi1.data.Game;
import it.unipi.lab3.abalderi1.data.User;
import it.unipi.lab3.abalderi1.permissions.Permission;

/**
 * Permesso che verifica se una partita è finita.
 */
public class IsGameFinishedPermission implements Permission {
    @Override
    public String getPermissionName() {
        return "PartitaDeveEssereFinita";
    }

    @Override
    public boolean hasPermission(User user, Game lastGame) {
        return lastGame == null || lastGame.isPartitaFinita();
    }

}
