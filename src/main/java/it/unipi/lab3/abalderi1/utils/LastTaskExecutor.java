package it.unipi.lab3.abalderi1.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Un executor che garantisce che solo l'ultimo task inviato venga eseguito.
 * Se un task è già in esecuzione, qualsiasi task in coda precedentemente verrà sostituito dal più recente.
 */
public class LastTaskExecutor implements Executor {
    /**
     * Una coda di blocchi per gestire i task in attesa di esecuzione.
     * Essendo di dimensione 1, può contenere al massimo un task alla volta.
     */
    private final BlockingQueue<Runnable> taskQueue = new ArrayBlockingQueue<>(1);
    private final AtomicBoolean isExecuting = new AtomicBoolean(false);
    private final AtomicBoolean isShutdown = new AtomicBoolean(false);

    /**
     * Invia un task per l'esecuzione. Se un task è attualmente in esecuzione,
     * il task inviato sostituirà qualsiasi altro task in attesa nella coda.
     *
     * @param command Il task da eseguire.
     * @throws IllegalStateException se l'executor è stato arrestato.
     */
    @Override
    public void execute(Runnable command) {
        if (isShutdown.get()) {
            throw new IllegalStateException("Executor has been shut down");
        }

        if (isExecuting.get()) {
            taskQueue.clear();
            taskQueue.offer(command);
        } else {
            executeTask(command);
        }
    }

    /**
     * Esegue effettivamente il task fornito. Questo metodo si assicura che solo un task
     * venga eseguito alla volta e gestisce la logica di sostituzione dei task in coda.
     *
     * @param command Il task da eseguire.
     */
    private void executeTask(Runnable command) {
        if (isExecuting.compareAndSet(false, true)) {
            new Thread(() -> {
                try {
                    command.run();
                } finally {
                    isExecuting.set(false);
                    Runnable nextTask = taskQueue.poll();
                    if (nextTask != null) {
                        executeTask(nextTask);
                    }
                }
            }).start();
        }
    }

    /**
     * Chiude l'executor, impedendo l'invio di ulteriori task,
     * e svuota la coda di qualsiasi task in attesa.
     */
    public void shutdown() {
        isShutdown.set(true);
        taskQueue.clear();
    }
}


