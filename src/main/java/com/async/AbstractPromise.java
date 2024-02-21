package com.async;

import com.async.FunctionInterface.*;


public abstract class AbstractPromise<T extends Object> extends Promise<T> {
    protected Promise firstPromise;
    //queue that save the ready promise
    protected OwningGraph owningGraph;
    protected boolean completed = false;
    protected boolean cancelled = false;
    private boolean readyToProcess = false;
    protected PromiseResult<T> result;
    protected CompleteListener completeListener;

    //constructor for upstream promise
    public AbstractPromise() {

    }

    //constructor for result
    protected AbstractPromise(T result) {
        this.result = PromiseResult.fromSuccess(result);
//        this.completed = true;
//        markReadyToProcess();
    }

    public OwningGraph getOwningGraph() {
        return owningGraph;
    }

    T get() {
        if (!completed || hasFailure()) {
            throw new RuntimeException("Promise not completed");
        }
        return result.getResult();
    }

    Throwable getFailure() {
        if (!hasFailure()) {
            throw new RuntimeException("Promise not completed or no failure");
        }
        return result.getThrowable();
    }

    abstract PromiseResult<T> buildResultFromUpstream() throws Throwable;

    public boolean hasFailure() {
        return result.getThrowable() != null;
    }


    @Override
    public Promise complete() throws Throwable {
        //cancelled or not ready to process will throw exception
        if (!readyToProcess) {
            throw new PromiseException("Promise already cancelled");
        }
        if (completed) {
            return this;
        }
        if (cancelled) {
            this.result = PromiseResult.fromFailure(new CancelException("Promise already cancelled"));
            //notify upstream promise cancellation
            notifyUpstreamCancellation();
        }
        this.result = buildResultFromUpstream(); //how to do it?
        completed = true;
        //notify downstream promise
        // completeListener == null means it is the last promise
        if(completeListener != null){
            completeListener.onComplete(this);
        }

        return this;
    }

    @Override
    <T2> ThenPromise<T, T2> then(ThrowingFunction<? super T, ? extends T2> dataTransform, ThrowingFunction<? super Throwable, ? extends T2> failureTransform) {
        ThenPromise<T, T2> thenPromise = ThenPromise.create(this, dataTransform, failureTransform);
        this.completeListener = thenPromise;
        return thenPromise;
    }

    @Override
    <T2> Promise<T2> thenFlat(ThrowingFunction<? super T, ? extends Promise<T2>> dataTransform, ThrowingFunction<? super Throwable, ? extends Promise<T2>> failureTransform) {
        return null;
    }

    @Override
    <T2> Promise<T2> thenSupply(ThrowingSupplier<? extends T2> dataSupplier) {
        return null;
    }

    @Override
    <T2> Promise<T2> thenSupplyFlat(ThrowingSupplier<? extends Promise<T2>> dataSupplier) {
        return null;
    }

    @Override
    Promise<Void> thenReturnVoid() {
        return null;
    }

    @Override
    <T2> Promise<T2> handleFailure(ThrowingFunction<? extends Throwable, ? extends T2> throwHandler) {
        return null;
    }

    @Override
    Promise<T> peek(ThrowingConsume<? super T> dataConsume, ThrowingConsume<? super Throwable> failureConsume) {
        return null;
    }

    @Override
    Promise<T> peekFailure(ThrowingConsume<? super Throwable> failureConsume) {
        return null;
    }

    @Override
    Promise<T> peekFailureFlat(ThrowingFunction<? super Throwable, ? extends Promise<?>> failureConsume) {
        return null;
    }

    @Override
    AbstractPromise<T> cancel(String message) throws PromiseException {
        if (completed) {
            throw new PromiseException("Promise already completed");
        }
        if (cancelled) {
            throw new PromiseException("Promise already cancelled");
        }
        cancelled = true;
        // set failure result to the promise
        this.result = PromiseResult.fromFailure(new CancelException(message));
        //failure result will impact the downstream promise
        //how impact?
        // downstream promise will be cancelled
        markReadyToProcess();
        return this;
    }

    abstract void notifyUpstreamCancellation();

    public void notifyOnDownstreamCancellation() {
        cancelled = true;
        markReadyToProcess();
    }

//    public void notifyFromUpStream() {
//        markReadyToProcess();
//    }


//    public void notifyToDownStream() {
//        AbstractPromise nextPromise = owningGraph.getNextPromise(this);
//        nextPromise.notifyFromUpStream();
//    }

    public void markReadyToProcess() {
        if (readyToProcess) {
            return;
        }
        this.readyToProcess = true;
        owningGraph.offerPromise(this); //not here to add promise, when promise created will add the promise to queue
        //notify downstream promise
        owningGraph.pendingToProcessPromise.remove(this);

    }

//    @Override
//    Promise then(ThrowingFunction dataTransform, ThrowingFunction failureTransform) {
//        return null;
//    }
//
//    @Override
//    Promise thenFlat(ThrowingFunction dataTransform, ThrowingFunction failureTransform) {
//        return null;
//    }
//
//    @Override
//    Promise thenSupply(ThrowingSupplier dataSupplier) {
//        return null;
//    }
//
//    @Override
//    Promise thenSupplyFlat(ThrowingSupplier dataSupplier) {
//        return null;
//    }
//
//
//    @Override
//    Promise<Void> thenReturnVoid() {
//        return null;
//    }
//
//    @Override
//    Promise peekFailureFlat(ThrowingFunction failureConsume) {
//        return null;
//    }
//
//    @Override
//    Promise peekFailure(ThrowingConsume failureConsume) {
//        return null;
//    }
//
//
//
//    @Override
//    Promise peek(ThrowingConsume dataConsume, ThrowingConsume failureConsume) {
//        return null;
//    }
//
//    @Override
//    Promise handleFailure(ThrowingFunction throwHandler) {
//        return null;
//    }
}
