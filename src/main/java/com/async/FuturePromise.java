package com.async;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FuturePromise<T extends Object> extends AbstractPromise<T> {

    private CompletableFuture<T> upstream;

    protected FuturePromise(T result) {
        super(result);
        this.firstPromise = this;
    }

    @Override
    PromiseResult<T> buildResultFromUpstream() {
        try {
            T t = upstream.get();
            return PromiseResult.fromSuccess(t);
        } catch (InterruptedException e) {
            return PromiseResult.fromFailure(e);
        } catch (ExecutionException e) {
            return PromiseResult.fromFailure(e.getCause());
        }

    }

    public FuturePromise(CompletableFuture<T> upstream) {
        super(null);
        this.upstream = upstream;
        this.owningGraph = new OwningGraph();

        FuturePromise<T> promise = new FuturePromise<>(null);
        upstream.whenComplete((result, throwable) -> {
            if (throwable != null) {
                cancelled = true;
            } else {
                if (!cancelled) {
                    markReadyToProcess();
                }
            }
        });

    }

    @Override
    void notifyUpstreamCancellation() {

    }
}
