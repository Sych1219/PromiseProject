package com.async;

import javax.validation.constraints.NotNull;

public final class FunctionInterface {
    @FunctionalInterface
    public interface ThrowingFunction<T1 extends @NotNull Object, T2 extends @NotNull Object> {
        T2 apply(T1 t) throws Throwable;
    }

    @FunctionalInterface
    public interface ThrowingConsume<T extends @NotNull Object> {
        Void accept(T t) throws Throwable;
    }

    @FunctionalInterface
    public interface ThrowingSupplier<T extends @NotNull Object> {
        T get() throws Throwable;
    }
}
