package com.async;

public class ImmediatePromise<T extends Object> extends AbstractPromise<T> {


    //constructor
    public ImmediatePromise(T result) {
        super(result);
        this.owningGraph = new OwningGraph();
        this.firstPromise = this;
        markReadyToProcess();
//        this.completed = true;
    }

    @Override
    PromiseResult<T> buildResultFromUpstream() {
        //immediate promise don't have upstream
      return result;
    }

    @Override
    void notifyUpstreamCancellation() {
        throw new RuntimeException("Immediate promise should not have upstream");
    }


}
