package com.async;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


class PromiseProjectApplicationTests {

    @Test
    void startFromImmediatePromise_return11() throws ExecutionException, InterruptedException {
        PromiseRunner promiseRunner = new PromiseRunnerImpl();
        Promise<Integer> firstPromise = new ImmediatePromise<>(1);

        Promise<String> lasPromise = firstPromise.then(intP -> {
            Thread.sleep(1000);
            return intP + 1;
        }).then(intP -> {
            int resInt = intP + 1;
            return resInt+"";
        });
        CompletableFuture<String> res = promiseRunner.submit(lasPromise);
        Assertions.assertEquals("3", res.get());
    }

}
