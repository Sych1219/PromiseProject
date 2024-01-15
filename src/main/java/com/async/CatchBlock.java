package com.async;

import com.async.FunctionInterface.ThrowingFunction;

import javax.validation.constraints.NotNull;
import java.util.Set;

//public abstract class CatchBlock<T extends Throwable, R extends @NotNull Object>
//        implements ThrowingFunction<Throwable,R> {
//    private final Set<Class<? extends T>> classesToCatch;
//    private final Predicate<? super T> filter;
//
//    public CatchBlock(Set<Class<? extends T>> classesToCatch, Predicate<? super T> filter) {
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
//
//}

public class CatchBlock<T extends Throwable, R extends @NotNull Object>
        implements ThrowingFunction<Throwable, R> {
    private final Set<Class<? extends Throwable>> classesToCatch; //here need to change to set, and extend T
    private final ThrowingFunction<T, R> handler;

    public boolean isMatchClassToCatch(Throwable input){
        return classesToCatch.stream().filter(tempClass-> tempClass.isInstance(input)).findAny().isPresent();
    }

    public CatchBlock(Set<Class<? extends Throwable>> classesToCatch, ThrowingFunction<T, R> handler) {
        this.classesToCatch = classesToCatch;
        this.handler = handler;
    }

    @Override
    public R apply(Throwable t) throws Throwable {
        if (!isMatchClassToCatch(t)) {
            throw t;
        }
        return handler.apply((T)t);
    }


}

