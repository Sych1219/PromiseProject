package com.async;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class OwningGraph {
    private List<Promise> promiseList = new CopyOnWriteArrayList<>();

    public void addPromise(Promise promise){
        promiseList.add(promise);
    }
}
