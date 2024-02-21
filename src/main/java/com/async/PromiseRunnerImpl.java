package com.async;

import java.util.concurrent.*;

public class PromiseRunnerImpl implements PromiseRunner {

    //executor
    private Executor executor;

    //constructor
    public PromiseRunnerImpl(Executor executor) {
        this.executor = executor;
    }

    //no parameter constructor
    public PromiseRunnerImpl() {
        this.executor = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(1000));
    }

    //constructor with corePoolSize and maxPoolSize keepAliveTime and workQueue
    public PromiseRunnerImpl(int corePoolSize, int maxPoolSize, long keepAliveTime, ArrayBlockingQueue<Runnable> workQueue) {
        this.executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, workQueue);
    }

    @Override
    public <T extends Object> CompletableFuture<T> submit(Promise<T> lastPromise) {
        OwningGraph owningGraph = lastPromise.getOwningGraph();

        CompletableFuture<T> tCompletableFuture = CompletableFuture.supplyAsync(() -> {
            while (!owningGraph.isDone()) {
                Promise nextPromise = owningGraph.pollPromise();
                if (nextPromise != null) {
                    try {
                        nextPromise.complete();
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return lastPromise.get();
        }, executor);
        return tCompletableFuture;
    }
}
