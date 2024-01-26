package com.async;

import java.util.concurrent.CompletableFuture;

public interface PromiseRunner {

    public <T extends Object> CompletableFuture<T> submit(Promise<T> promise);
}
