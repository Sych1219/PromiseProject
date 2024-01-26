package com.async;

public interface CompleteListener {

    void onComplete(Promise promiseThatTrigger) throws PromiseException;

}
