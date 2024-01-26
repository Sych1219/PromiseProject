package com.async;

public class PromiseResult<T extends Object> {
    private T result;
    private Throwable throwable;

    public PromiseResult(T result, Throwable throwable) {
        this.result = result;
        this.throwable = throwable;
    }

    public static PromiseResult fromFailure(Throwable throwable){
        return new PromiseResult(null, throwable);
    }

    public static <T>  PromiseResult<T>  fromSuccess(T result){
        return new PromiseResult(result, null);
    }

    public T getResult() {
        return result;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
