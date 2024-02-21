package com.async;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


class PromiseProjectApplicationTests {

    @Test
    void startFromImmediatePromise_return3() throws ExecutionException, InterruptedException {
        PromiseRunner promiseRunner = new PromiseRunnerImpl();
        Promise<Integer> firstPromise = new ImmediatePromise<>(1);

        Promise<String> lasPromise = firstPromise.then(intP -> {
            Thread.sleep(1000);
            return intP + 1;
        }).then(intP -> {
            int resInt = intP + 1;
            return resInt + "";
        });
        CompletableFuture<String> res = promiseRunner.submit(lasPromise);
        Assertions.assertEquals("3", res.get());
    }

    @Test
    void startFromFuturePromise_return11() throws ExecutionException, InterruptedException {
        PromiseRunner promiseRunner = new PromiseRunnerImpl();
        CompletableFuture<String> completedFutureWithValue =
                CompletableFuture.completedFuture("Hello,");

        Promise<String> firstPromise = new FuturePromise<>(completedFutureWithValue);

        Promise<String> lasPromise = firstPromise.then(tempStr -> {
            Thread.sleep(1000);
            return tempStr + " World";
        });
        CompletableFuture<String> res = promiseRunner.submit(lasPromise);
        Assertions.assertEquals("Hello, World", res.get());
    }

    @Test
    void startFromFuturePromise_withDelay_return11() throws ExecutionException, InterruptedException {
        PromiseRunner promiseRunner = new PromiseRunnerImpl();
        // Example with a delayed completion
        CompletableFuture<String> delayedCompletionFuture = new CompletableFuture<>();

        // Simulating an asynchronous task with a delay
        CompletableFuture.delayedExecutor(2, TimeUnit.SECONDS).execute(() -> {
            delayedCompletionFuture.complete("Completed after 10 seconds");
        });

        Promise<String> firstPromise = new FuturePromise<>(delayedCompletionFuture);
        Promise<String> lasPromise = firstPromise.then(tempStr -> tempStr + " World");
        CompletableFuture<String> res = promiseRunner.submit(lasPromise);
        Assertions.assertEquals("Completed after 10 seconds World", res.get());
    }

}
