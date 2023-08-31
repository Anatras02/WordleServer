package it.unipi.lab3.abalderi1.views;

import it.unipi.lab3.abalderi1.data.DailyWord;
import it.unipi.lab3.abalderi1.data.Game;
import it.unipi.lab3.abalderi1.data.User;
import it.unipi.lab3.abalderi1.data.orm.DailyWordOrm;
import it.unipi.lab3.abalderi1.permissions.*;
import it.unipi.lab3.abalderi1.protocol.Request;
import it.unipi.lab3.abalderi1.protocol.Response;

import java.util.List;

/**
 * La classe {@code SendWordView} gestisce le richieste di invio delle parole da parte degli utenti.
 */
public class SendWordView extends View {
    /**
     * Ottiene i permessi necessari per inviare una parola.
     *
     * @return Una lista dei permessi necessari.
     */
    @Override
    protected List<Permission> getPermissions() {
        return List.of(new IsUserLoggedPermission(), new IsGameStartedPermission(), new IsGameNotFinishedPermission(), new IsGameValidPermission());
    }

    /**
     * Ottiene il consiglio per l'utente in base alla parola inviata e alla parola da indovinare.
     *
     * @param parolaInviata La parola inviata dall'utente.
     * @param parolaDaIndovinare La parola da indovinare.
     * @return Un consiglio per l'utente.
     */
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


    /**
     * Elabora la richiesta di invio di una parola.
     *
     * @param request La richiesta inviata dal client.
     * @param user L'utente associato alla richiesta.
     * @return Una risposta che contiene il risultato dell'invio della parola.
     */
    @Override
    public Response handleRequestWithPermissions(Request request, User user) {
        String parola = request.getParam("word");

        if (parola == null) {
            return new Response("MISSING", "Parametro mancante: word");
        }

        DailyWordOrm dailyWordOrm = DailyWordOrm.getInstance();

        if (!dailyWordOrm.isParolaInLista(parola)) {
            return new Response("INVALID_WORD", "La parola non è presente nella lista");
        }

        Game lastGame = user.getLastGame();
        lastGame.incrementTentativi();

        if (lastGame.getTentativi() >= 12) {
            lastGame.finisci();
            lastGame.setVittoria(false);
            user.editLastGame(lastGame);

            user.salva();

            return new Response("GAMEOVER", dailyWordOrm.getDailyWord().getWord());
        }


        String consiglio = getConsiglio(parola, dailyWordOrm.getDailyWord().getWord());

        DailyWord dailyWord = dailyWordOrm.getDailyWord();
        lastGame.addParola(consiglio);
        user.editLastGame(lastGame);


        if (dailyWord.getWord().equalsIgnoreCase(parola)) {
            lastGame.finisci();
            lastGame.setVittoria(true);

            user.editLastGame(lastGame);
            user.salva();

            return new Response("SUCCESS", "Hai indovinato la parola");
        } else {
            user.salva();

            return new Response("ADVISE", consiglio);
        }

    }
}
