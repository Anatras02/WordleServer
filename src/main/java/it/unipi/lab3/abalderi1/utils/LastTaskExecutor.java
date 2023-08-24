package it.unipi.lab3.abalderi1.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

public class LastTaskExecutor implements Executor {
    private final BlockingQueue<Runnable> taskQueue = new ArrayBlockingQueue<>(1);
    private final AtomicBoolean isExecuting = new AtomicBoolean(false);
    private final AtomicBoolean isShutdown = new AtomicBoolean(false);


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

    public void shutdown() {
        isShutdown.set(true);
        taskQueue.clear();
    }
}


