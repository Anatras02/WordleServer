package it.unipi.lab3.abalderi1;

import it.unipi.lab3.abalderi1.config.ConfigHandler;
import it.unipi.lab3.abalderi1.data.DailyWord;
import it.unipi.lab3.abalderi1.data.orm.DailyWordOrm;

import java.io.IOException;
import java.net.ServerSocket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static boolean èOggi(LocalDateTime dateTime) {
        LocalDate today = LocalDate.now();

        return dateTime.toLocalDate().equals(today);
    }

    public static void schedulerGenerazioneNuovaParola() {
        DailyWordOrm dailyWordOrm = DailyWordOrm.getInstance();
        DailyWord dailyWord = dailyWordOrm.getDailyWord();

        if (!èOggi(dailyWord.getDateTime())) {
            generaNuovaParola();
        }

        long initialDelay = getRitardoIniziale();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(Main::generaNuovaParola, initialDelay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);

        // Hook di shutdown per chiudere il scheduler quando l'applicazione si arresta
        Runtime.getRuntime().addShutdownHook(new Thread(scheduler::shutdown));
    }

    private static long getRitardoIniziale() {
        long currentTimeMillis = System.currentTimeMillis();
        long oneDayMillis = TimeUnit.DAYS.toMillis(1);
        return oneDayMillis - (currentTimeMillis % oneDayMillis);
    }

    private static void generaNuovaParola() {
        DailyWordOrm dailyWordOrm = DailyWordOrm.getInstance();

        DailyWord dailyWord = dailyWordOrm.generaESalvaNuovaParola();

        System.out.println("Nuova parola del giorno generata: " + dailyWord.getWord());
    }


    public static void main(String[] args) {
        ConfigHandler configHandler;

        try {
            configHandler = new ConfigHandler("config.properties");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        schedulerGenerazioneNuovaParola();

        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(configHandler.getProperty("port")))) {
            while (true) {
                try (ExecutorService threadPool = Executors.newFixedThreadPool(Integer.parseInt(configHandler.getProperty("poolSize")))) {
                    threadPool.execute(new ClientHandler(serverSocket.accept()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}