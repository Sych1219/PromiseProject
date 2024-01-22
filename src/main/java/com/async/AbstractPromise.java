package com.async;

public class AbstractPromise extends Promise{

    //queue that save the ready promise
    private OwningGraph owningGraph;
    private  boolean isCompleted = false;
    private boolean isCancelled = false;

    @Override
    void cancel(String message) {
        if(isCompleted){
            return;
        }

    }

    @Override
    Promise then(FunctionInterface.ThrowingFunction dataTransform, FunctionInterface.ThrowingFunction failureTransform) {
        return null;
    }

    @Override
    Promise thenFlat(FunctionInterface.ThrowingFunction dataTransform, FunctionInterface.ThrowingFunction failureTransform) {
        return null;
    }

    @Override
    Promise thenSupply(FunctionInterface.ThrowingSupplier dataSupplier) {
        return null;
    }

    @Override
    Promise thenSupplyFlat(FunctionInterface.ThrowingSupplier dataSupplier) {
        return null;
    }


    @Override
    Promise<Void> thenReturnVoid() {
        return null;
    }

    @Override
    Promise peekFailureFlat(FunctionInterface.ThrowingFunction failureConsume) {
        return null;
    }

    @Override
    Promise peekFailure(FunctionInterface.ThrowingConsume failureConsume) {
        return null;
    }



    @Override
    Promise peek(FunctionInterface.ThrowingConsume dataConsume, FunctionInterface.ThrowingConsume failureConsume) {
        return null;
    }

    @Override
    Promise handleFailure(FunctionInterface.ThrowingFunction throwHandler) {
        return null;
    }
}
