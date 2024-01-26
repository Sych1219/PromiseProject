package com.async;

public class CancelException extends PromiseException{
    public CancelException(String message) {
        super(message);
    }
}
