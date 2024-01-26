package com.async;

import com.async.FunctionInterface.*;

import javax.validation.constraints.NotNull;

public class ThenPromise<Input extends @NotNull Object, Output extends @NotNull Object> extends AbstractPromise<Output> implements CompleteListener {

    private AbstractPromise<Input> upstreamPromise;
    private ThrowingFunction<? super Input, ? extends Output> dataTransform;
    private ThrowingFunction<? super Throwable, ? extends Output> failureTransform;

    private ThenPromise(AbstractPromise<Input> upstreamPromise, ThrowingFunction<? super Input, ? extends Output> dataTransform, ThrowingFunction<? super Throwable, ? extends Output> failureTransform) {
        this.upstreamPromise = upstreamPromise;
        if (upstreamPromise.firstPromise!=null) {
            this.firstPromise = upstreamPromise.firstPromise;
        }
        this.dataTransform = dataTransform;
        this.failureTransform = failureTransform;
        this.owningGraph = upstreamPromise.getOwningGraph();
    }


    public static <T extends Object, T2> ThenPromise<T, T2> create(AbstractPromise<T> upperPromise, ThrowingFunction<? super T, ? extends T2> dataTransform, ThrowingFunction<? super Throwable, ? extends T2> failureTransform) {
        return new ThenPromise<T, T2>(upperPromise, dataTransform, failureTransform);
    }

    @Override
    void notifyUpstreamCancellation() {
        upstreamPromise.notifyOnDownstreamCancellation();
    }

    @Override
    public void onComplete(Promise promiseThatTrigger) throws PromiseException {
        if (promiseThatTrigger != upstreamPromise) {
            throw new PromiseException("Promise that trigger is not the upstream promise");
        }
        upstreamPromise.completeListener = null;
        //todo to check?
        markReadyToProcess();
    }


    @Override
    PromiseResult<Output> buildResultFromUpstream() throws Throwable {
        Output output = upstreamPromise.hasFailure() ? failureTransform.apply(upstreamPromise.getFailure())
                : dataTransform.apply(upstreamPromise.get());

        return PromiseResult.fromSuccess(output);
    }
}
