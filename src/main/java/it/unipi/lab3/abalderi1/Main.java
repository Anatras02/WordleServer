package it.unipi.lab3.abalderi1;

import it.unipi.lab3.abalderi1.config.ConfigHandler;
import it.unipi.lab3.abalderi1.data.DailyWord;
import it.unipi.lab3.abalderi1.data.orm.DailyWordOrm;
import it.unipi.lab3.abalderi1.data.orm.UserOrm;

import java.io.IOException;
import java.net.ServerSocket;
import java.time.ZoneId;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Appunti miei:
 * 1. Era meglio usare TypeToken invece che .class quando si fa la reflection per GSON
 * 2. Era meglio usare un ExectuorService con DiscardOldestPolicy invece di creare LastTaskExecutor
 * 3.
 */

/**
 * La classe {@code Main} gestisce l'avvio del server, l'ascolto dei client e la generazione
 * periodica di una nuova parola del giorno.
 */
public class Main {
    /**
     * Programma la generazione periodica di una nuova parola del giorno.
     * L'intervallo tra le generazioni è configurabile.
     * Se la parola del giorno non è stata ancora generata, viene generata immediatamente.
     *
     * @param configHandler gestore della configurazione per leggere l'intervallo di generazione.
     */
    public static void schedulerGenerazioneNuovaParola(ConfigHandler configHandler) {
        DailyWordOrm dailyWordOrm = DailyWordOrm.getInstance();
        DailyWord dailyWord = dailyWordOrm.getDailyWord();

        int intervalInSeconds = configHandler.getIntProperty("intervalInSeconds", 10);
        long initialDelay = getInitialDelay(dailyWord, intervalInSeconds);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(Main::generaNuovaParola, initialDelay, TimeUnit.SECONDS.toMillis(intervalInSeconds), TimeUnit.MILLISECONDS);

        // Hook di shutdown per chiudere il scheduler quando l'applicazione si arresta
        Runtime.getRuntime().addShutdownHook(new Thread(scheduler::shutdown));
    }


    /**
     * Calcola il ritardo iniziale prima della prossima generazione della parola del giorno.
     * Se la parola del giorno non è stata generata da un periodo maggiore o uguale all'intervallo fornito,
     * ritorna 0 per la generazione immediata.
     *
     * @param lastGeneratedWord ultima parola generata.
     * @param intervalInSeconds intervallo di generazione in secondi.
     * @return ritardo in millisecondi.
     */
    private static long getInitialDelay(DailyWord lastGeneratedWord, int intervalInSeconds) {
        long currentTimeMillis = System.currentTimeMillis();
        long lastGenerationMillis = lastGeneratedWord.getDateTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long timeSinceLastGeneration = currentTimeMillis - lastGenerationMillis;
        long intervalMillis = TimeUnit.SECONDS.toMillis(intervalInSeconds);

        if (timeSinceLastGeneration >= intervalMillis) {
            return 0; // genera immediatamente
        }
        return intervalMillis - timeSinceLastGeneration; // attendi il tempo rimanente
    }


    /**
     * Genera una nuova parola del giorno, la salva nel database, invalida i giochi degli utenti
     * e stampa a console la nuova parola generata.
     */
    private static void generaNuovaParola() {
        DailyWordOrm dailyWordOrm = DailyWordOrm.getInstance();

        DailyWord dailyWord = dailyWordOrm.generaESalvaNuovaParola();

        UserOrm userOrm = UserOrm.getInstance();
        userOrm.invalidateGames();

        System.out.println("Nuova parola del giorno generata: " + dailyWord.getWord());
    }


    /**
     * Metodo principale per avviare l'applicazione.
     * Configura e avvia la generazione periodica della parola del giorno e l'ascolto dei client
     * tramite un pool di thread fisso.
     *
     * @param args argomenti della riga di comando (non utilizzati).
     */
    public static void main(String[] args) {
        ConfigHandler configHandler;

        configHandler = ConfigHandler.getInstance();

        schedulerGenerazioneNuovaParola(configHandler);

        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(configHandler.getProperty("port")))) {
            try (ExecutorService threadPool = Executors.newFixedThreadPool(Integer.parseInt(configHandler.getProperty("poolSize")))) {
                while (true) {
                    threadPool.execute(new ClientHandler(serverSocket.accept()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}