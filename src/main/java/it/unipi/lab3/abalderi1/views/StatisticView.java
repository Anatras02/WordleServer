package it.unipi.lab3.abalderi1.views;

import com.google.gson.Gson;
import it.unipi.lab3.abalderi1.data.User;
import it.unipi.lab3.abalderi1.permissions.IsUserLoggedPermission;
import it.unipi.lab3.abalderi1.permissions.Permission;
import it.unipi.lab3.abalderi1.protocol.Request;
import it.unipi.lab3.abalderi1.protocol.Response;

import java.util.List;

public class StatisticView extends View {
    @Override
    protected List<Permission> getPermissions() {
        return List.of(new IsUserLoggedPermission());
    }

    @Override
    public Response handleRequestWithPermissions(Request request, User user) {
        user.getStatistiche();

        Gson gson = new Gson();
        String body = gson.toJson(user.getStatistiche());

        return new Response("200", body);
    }
}
