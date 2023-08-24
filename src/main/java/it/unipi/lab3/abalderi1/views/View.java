package it.unipi.lab3.abalderi1.views;

import com.google.gson.JsonObject;
import it.unipi.lab3.abalderi1.data.User;
import it.unipi.lab3.abalderi1.permissions.Permission;
import it.unipi.lab3.abalderi1.permissions.exceptions.NoPermissionException;
import it.unipi.lab3.abalderi1.protocol.Request;
import it.unipi.lab3.abalderi1.protocol.Response;

import java.util.ArrayList;
import java.util.List;

public abstract class View {
    protected List<Permission> getPermissions() {
        return new ArrayList<>();
    }

    static String getMessageJson(String message) {
        JsonObject body = new JsonObject();
        body.addProperty("message", message);

        return body.toString();
    }


    private boolean checkPermissions(User user) throws NoPermissionException {
        for (Permission permission : getPermissions()) {
            if (!permission.hasPermission(user, user.getLastGame())) {
                throw new NoPermissionException(permission.getPermissionName());
            }
        }

        return true;
    }

    public Response handle(Request request, User user) throws NoPermissionException {
        if (checkPermissions(user)) {
            return handleRequestWithPermissions(request, user);
        }

        return null;
    }

    protected abstract Response handleRequestWithPermissions(Request request, User user);
}
