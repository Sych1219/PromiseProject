package com.async;

public class AbstractPromise extends Promise{

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
    Promise peekFlat(FunctionInterface.ThrowingConsume dataConsume, FunctionInterface.ThrowingFunction failureConsumer) {
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
