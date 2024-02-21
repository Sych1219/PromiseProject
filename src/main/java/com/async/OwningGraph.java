package com.async;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class OwningGraph {
    private List<AbstractPromise> promiseList = new CopyOnWriteArrayList<>();
    Queue<Promise> readyToProcessPromiseQueue = new LinkedList<>();
    Set<Promise> pendingToProcessPromise = new HashSet<>();

    public void offerPromise(AbstractPromise promise) {
        readyToProcessPromiseQueue.offer(promise);
    }

    //poll the promise from the queue
    public Promise pollPromise() {
        return readyToProcessPromiseQueue.poll();
    }

    public boolean isDone() {
        return readyToProcessPromiseQueue.isEmpty()&&pendingToProcessPromise.isEmpty();
    }

    public AbstractPromise getNextPromise(AbstractPromise currentPromise) {
        int index = promiseList.indexOf(currentPromise);
        if (index == -1) {
            throw new RuntimeException("Promise not found");
        }
        if (index == promiseList.size() - 1) {
            return null;
        }
        return promiseList.get(index + 1);
    }
}
