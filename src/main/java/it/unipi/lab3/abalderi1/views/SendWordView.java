package it.unipi.lab3.abalderi1.views;

import it.unipi.lab3.abalderi1.data.DailyWord;
import it.unipi.lab3.abalderi1.data.Game;
import it.unipi.lab3.abalderi1.data.User;
import it.unipi.lab3.abalderi1.data.orm.DailyWordOrm;
import it.unipi.lab3.abalderi1.permissions.IsGameNotFinishedPermission;
import it.unipi.lab3.abalderi1.permissions.IsGameStartedPermission;
import it.unipi.lab3.abalderi1.permissions.IsUserLoggedPermission;
import it.unipi.lab3.abalderi1.permissions.Permission;
import it.unipi.lab3.abalderi1.protocol.Request;
import it.unipi.lab3.abalderi1.protocol.Response;

import java.util.List;

public class SendWordView extends View {
    @Override
    protected List<Permission> getPermissions() {
        return List.of(new IsUserLoggedPermission(), new IsGameStartedPermission(), new IsGameNotFinishedPermission());
    }

    public String getConsiglio(String parolaInviata, String parolaDaIndovinare) {
        StringBuilder consiglio = new StringBuilder();

        for (int i = 0; i < parolaInviata.length(); i++) {
            char letteraInviata = parolaInviata.charAt(i);

            if (i < parolaDaIndovinare.length() && letteraInviata == parolaDaIndovinare.charAt(i)) {
                consiglio.append('+');  // Lettera corretta nella posizione corretta
            } else if (parolaDaIndovinare.contains(String.valueOf(letteraInviata))) {
                consiglio.append('?');  // Lettera corretta ma in posizione sbagliata
            } else {
                consiglio.append('X');  // Lettera sbagliata
            }
        }

        return consiglio.toString();
    }

    @Override
    public Response handleRequestWithPermissions(Request request, User user) {
        String parola = request.getParam("word");

        if (parola == null) {
            return new Response("400", getMessageJson("Parametro mancante: word"));
        }

        Game lastGame = user.getLastGame();

        if (lastGame.getTentativi() >= 12) {
            lastGame.finisci();
            lastGame.setVittoria(false);

            user.salva();

            return new Response("400", getMessageJson("Hai superato il numero massimo di tentativi"));
        }


        DailyWordOrm dailyWordOrm = DailyWordOrm.getInstance();

        if (dailyWordOrm.isParolaInLista(parola)) {
            return new Response("404", getMessageJson("La parola non è presente nella lista"));
        }

        String consiglio = getConsiglio(parola, dailyWordOrm.getDailyWord().getWord());

        DailyWord dailyWord = dailyWordOrm.getDailyWord();
        lastGame.incrementTentativi();
        lastGame.addParola(consiglio);
        user.editLastGame(lastGame);

        if (dailyWord.getWord().equals(parola)) {
            lastGame.finisci();
            lastGame.setVittoria(true);

            user.editLastGame(lastGame);
            user.salva();

            return new Response("200", getMessageJson("Hai indovinato la parola"));
        } else {
            user.salva();

            return new Response("200", getMessageJson(consiglio));
        }

    }
}
