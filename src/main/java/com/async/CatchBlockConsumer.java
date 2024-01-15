package com.async;

import java.util.Set;

import com.async.FunctionInterface.*;

//public abstract class CatchBlockConsumer<T extends Throwable> implements ThrowingConsume<T> {
//
//    private final Set<Class<? extends T>> classesToCatch;
//    private final Predicate<? super T> filter;
//
//    private CatchBlockConsumer(Set<Class<? extends T>> classesToCatch, Predicate<? super T> filter) {
//        this.classesToCatch = classesToCatch;
//        this.filter = filter;
//    }
//
//    public final Optional<T> appliesTo(Throwable input){
//        return classesToCatch.stream().filter(clazz-> clazz.isInstance(input))
//                .findAny()
//                .map(clazz-> (T)clazz.cast(input))
//                .filter(filter);
//    }
//}

public  class CatchBlockConsumer<T extends Throwable> implements ThrowingConsume<T> {

    private final Set<Class<? extends T>> classesToCatch;
    private final ThrowingConsume<T> handler;

    private CatchBlockConsumer(Set<Class<? extends T>> classesToCatch, ThrowingConsume<T> handler) {
        this.classesToCatch = classesToCatch;
        this.handler = handler;
    }

    public boolean isMatchClassToCatch(T input){
       return classesToCatch.stream().filter(tempClass-> tempClass.isInstance(input)).findAny().isPresent();
    }

    @Override
    public Void accept(T t) throws Throwable {
        if(!isMatchClassToCatch(t)){
            throw t;
        }
      return   handler.accept(t);
    }

//    @Override
//    public Void applyAndReturn(T t) throws Throwable {
//        return handler.applyAndReturn(t);
//    }
}
