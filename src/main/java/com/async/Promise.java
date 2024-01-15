package com.async;

import com.async.FunctionInterface.*;
import com.async.FunctionInterface.ThrowingConsume;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public abstract class Promise<T extends @NotNull Object> {


    abstract <T2 extends @NotNull Object> Promise<T2> then(ThrowingFunction<? super T, ? extends T2> dataTransform,
                                                           ThrowingFunction<? super Throwable, ? extends T2> failureTransform);

    public <T2 extends @NotNull Object, X extends Throwable> Promise<T2> then(ThrowingFunction<? super T, ? extends T2> dataTransform,
                                                                              Class<X> exceptionClass,
                                                                              ThrowingFunction<? super X, ? extends T2> exceptionHandler) {
        CatchBlock<X, T2> catchBlock = new CatchBlock(Set.of(exceptionClass), exceptionHandler);
        return then(dataTransform, catchBlock);
    }

    public <T2 extends @NotNull Object, X extends Throwable> Promise<T2> then(ThrowingFunction<? super T, ? extends T2> dataTransform,
                                                                              Class<X> exceptionClass,
                                                                              CatchBlock<? super X, ? extends T2> catchBlock) {
        return then(dataTransform, exceptionClass, (ThrowingFunction<? super X, ? extends T2>) catchBlock);
    }

    public <T2 extends @NotNull Object> Promise<T2> then(ThrowingFunction<? super T, ? extends T2> dataTransform,
                                                         CatchBlock<? extends Throwable, ? extends T2> catchBlock1,
                                                         CatchBlock<? extends Throwable, ? extends T2> catchBlock2,
                                                         CatchBlock<? extends Throwable, ? extends T2> catchBlock3) {
        List<CatchBlock<? extends Throwable, ? extends T2>> catchBlocks = Arrays.asList(catchBlock1, catchBlock2, catchBlock3);
        return then(dataTransform, catchBlocks);
    }

    public <T2 extends @NotNull Object> Promise<T2> then(ThrowingFunction<? super T, ? extends T2> dataTransform,
                                                         List<CatchBlock<? extends Throwable, ? extends T2>> catchBlocks) {
        ThrowingFunction<? super Throwable, ? extends T2> failureTransform = ex -> {
            for (CatchBlock<? extends Throwable, ? extends T2> catchBlock : catchBlocks) {
                if (catchBlock.isMatchClassToCatch(ex)) {
                    return catchBlock.apply(ex);
                }
            }
            throw ex;
        };
        return then(dataTransform, failureTransform);
    }

    public final <T2 extends @NotNull Object> Promise<T2> then(ThrowingFunction<? super T, ? extends T2> dataTransform,
                                                               CatchBlock<? extends Throwable, ? extends T2> catchBlock) {
        return then(dataTransform, (ThrowingFunction<Throwable, ? extends T2>) catchBlock);
    }

    public <T2 extends @NotNull Object> Promise<T2> then(ThrowingFunction<? super T, ? extends T2> dataTransform) {
        return then(dataTransform, e -> {
            throw e;
        });
    }

    //thenFlat
    abstract <T2 extends @NotNull Object> Promise<T2> thenFlat(ThrowingFunction<? super T, ? extends Promise<T2>> dataTransform,
                                                               ThrowingFunction<? super Throwable, ? extends Promise<T2>> failureTransform);

    public <T2 extends @NotNull Object, X extends Throwable> Promise<T2> thenFlat(ThrowingFunction<? super T, ? extends Promise<T2>> dataTransform,
                                                                                  Class<X> exceptionClass,
                                                                                  ThrowingFunction<? super X, ? extends Promise<T2>> exceptionHandler) {
        CatchBlock<X, Promise<T2>> xPromiseCatchBlock =
                new CatchBlock(Set.of(exceptionClass), exceptionHandler);
        return thenFlat(dataTransform, xPromiseCatchBlock);
    }

    public <T2 extends @NotNull Object, X extends Throwable> Promise<T2> thenFlat(ThrowingFunction<? super T, ? extends Promise<T2>> dataTransform,
                                                                                  Class<X> exceptionClass,
                                                                                  CatchBlock<? super X, ? extends Promise<T2>> catchBlock) {
        return thenFlat(dataTransform, exceptionClass, (ThrowingFunction<? super X, ? extends Promise<T2>>) catchBlock);
    }

    public <T2 extends @NotNull Object> Promise<T2> thenFlat(ThrowingFunction<? super T, ? extends Promise<T2>> dataTransform,
                                                             CatchBlock<? extends Throwable, ? extends Promise<T2>> catchBlock1,
                                                             CatchBlock<? extends Throwable, ? extends Promise<T2>> catchBlock2,
                                                             CatchBlock<? extends Throwable, ? extends Promise<T2>> catchBlock3) {
        List<CatchBlock<? extends Throwable, ? extends Promise<T2>>> catchBlocks = Arrays.asList(catchBlock1, catchBlock2, catchBlock3);
        return thenFlat(dataTransform, catchBlocks);
    }

    public <T2 extends @NotNull Object> Promise<T2> thenFlat(ThrowingFunction<? super T, ? extends Promise<T2>> dataTransform,
                                                             List<CatchBlock<? extends Throwable, ? extends Promise<T2>>> catchBlocks) {
        ThrowingFunction<? super Throwable, ? extends Promise<T2>> failureTransform = ex -> {
            for (CatchBlock<? extends Throwable, ? extends Promise<T2>> catchBlock : catchBlocks) {
                if (catchBlock.isMatchClassToCatch(ex)) {
                    return catchBlock.apply(ex);
                }
            }
            throw ex;
        };
        return thenFlat(dataTransform, failureTransform);
    }

    public final <T2 extends @NotNull Object> Promise<T2> thenFlat(ThrowingFunction<? super T, ? extends Promise<T2>> dataTransform,
                                                                   CatchBlock<? extends Throwable, ? extends Promise<T2>> catchBlock) {
        return thenFlat(dataTransform, (ThrowingFunction<Throwable, ? extends Promise<T2>>) catchBlock);
    }

    public <T2 extends @NotNull Object> Promise<T2> thenFlat(ThrowingFunction<? super T, ? extends Promise<T2>> dataTransform) {
        return thenFlat(dataTransform, e -> {
            throw e;
        });
    }

    //thenConsume
    public void thenConsume(ThrowingConsume<? super T> dataConsume, ThrowingConsume<? super Throwable> failureConsume) {
        then(data -> {
            dataConsume.accept(data);
            return null;
        }, e -> {
            failureConsume.accept(e);
            return null;
        });
    }

    public <X extends Throwable> void thenConsume(ThrowingConsume<? super T> dataConsume, Class<X> exceptionClass,
                                                  ThrowingConsume<? super X> failureConsumer) {
        then(data -> {
            dataConsume.accept(data);
            return null;
        }, exceptionClass, e -> {
            failureConsumer.accept(e);
            return null;
        });
    }

    public <X extends Throwable> void thenConsume(ThrowingConsume<? super T> dataConsume, Class<X> exceptionClass,
                                                  CatchBlockConsumer<? super X> catchBlockConsumer) {
        then(data -> dataConsume.accept(data), exceptionClass, e -> catchBlockConsumer.accept(e));
    }

    public void thenConsume(ThrowingConsume<? super T> dataConsume,
                            CatchBlockConsumer<? extends Throwable> catchBlockConsumer1,
                            CatchBlockConsumer<? extends Throwable> catchBlockConsumer2,
                            CatchBlockConsumer<? extends Throwable> catchBlockConsumer3) {
        //1. add three catchBlockConsumer into the list
        List<CatchBlockConsumer> catchBlockConsumers = Arrays.asList(catchBlockConsumer1, catchBlockConsumer2, catchBlockConsumer3);
        //2. for the list
        //3. if the instance of exception match the cathe block consume stop and return

        then(data -> dataConsume.accept(data),
                exInput -> {
                    for (CatchBlockConsumer catchBlockConsumer : catchBlockConsumers) {
                        if (catchBlockConsumer.isMatchClassToCatch(exInput)) {
                            return catchBlockConsumer.accept(exInput);
                        }
                    }
                    throw exInput;
                }
        );
    }

    public void thenConsume(ThrowingConsume<? super T> dataConsume,
                            List<CatchBlockConsumer<? extends Throwable>> catchBlockConsumers) {
        then(data -> dataConsume.accept(data), ex -> {
            for (CatchBlockConsumer catchBlockConsumer : catchBlockConsumers) {
                if (catchBlockConsumer.isMatchClassToCatch(ex)) {
                    return catchBlockConsumer.accept(ex);
                }
            }
            throw ex;
        });
    }

    public final void thenConsume(ThrowingConsume<? super T> dataConsume,
                                  CatchBlockConsumer<? extends Throwable> catchBlockConsumer) {
        List<CatchBlockConsumer<? extends Throwable>> catchBlockConsumers = new ArrayList<>();
        catchBlockConsumers.add(catchBlockConsumer);
        thenConsume(dataConsume, catchBlockConsumers);
    }

    public void thenConsume(ThrowingConsume<? super T> dataConsume) {
        thenConsume(dataConsume, e -> {
            throw e;
        });
    }

    abstract <T2 extends @NotNull Object> Promise<T2> thenSupply(ThrowingSupplier<? extends T2> dataSupplier);

    abstract <T2 extends @NotNull Object> Promise<T2> thenSupplyFlat(ThrowingSupplier<? extends Promise<T2>> dataSupplier);

    abstract Promise<Void> thenReturnVoid();

    abstract <T2> Promise<T2> handleFailure(ThrowingFunction<? extends Throwable, ? extends T2> throwHandler);


    public <T2> Promise<T2> handleFailure(List<CatchBlock<? extends Throwable, ? extends T2>> catchBlocks) {
        ThrowingFunction<? extends Throwable, ? extends T2> failureTransform = ex -> {
            for (CatchBlock<? extends Throwable, ? extends T2> catchBlock : catchBlocks) {
                if (catchBlock.isMatchClassToCatch(ex)) {
                    return catchBlock.apply(ex);
                }
            }
            throw ex;
        };
        return handleFailure(failureTransform);
    }

    public <T2> Promise<T2> handleFailure(CatchBlock<? extends Throwable, ? extends T2> catchBlock1,
                                          CatchBlock<? extends Throwable, ? extends T2>... otherCatchBlocks) {
        List<CatchBlock<? extends Throwable, ? extends T2>> catchBlocks = new ArrayList<>();
        catchBlocks.add(catchBlock1);
        catchBlocks.addAll(Arrays.asList(otherCatchBlocks));
        return handleFailure(catchBlocks);
    }

    public <T2, X extends Throwable> Promise<T2> handleFailure(Class<X> exceptionClass, ThrowingFunction<? super X, ? extends T2> failureTransform) {
        CatchBlock<X, T2> catchBlock = new CatchBlock(Set.of(exceptionClass), failureTransform);
        return handleFailure(catchBlock);
    }

    public <T2, X extends Throwable> Promise<T2> handleFailure(Class<X> exceptionClass, CatchBlock<? super X, ? extends T2> catchBlock) {
        return handleFailure(exceptionClass, (ThrowingFunction<? super X, ? extends T2>) catchBlock);
    }

    //peek
    abstract Promise<T> peek(ThrowingConsume<? super T> dataConsume);

    abstract Promise<T> peek(ThrowingConsume<? super T> dataConsume, ThrowingConsume<? super Throwable> failureConsume);

    abstract Promise<T> peek(ThrowingConsume<? super T> dataConsume, CatchBlockConsumer<? extends Throwable> catchBlock1);

    abstract Promise<T> peek(ThrowingConsume<? super T> dataConsume, CatchBlockConsumer<? extends Throwable> catchBlock1, CatchBlockConsumer<? extends Throwable> catchBlock2);

    abstract Promise<T> peek(ThrowingConsume<? super T> dataConsume, CatchBlockConsumer<? extends Throwable> catchBlock1, CatchBlockConsumer<? extends Throwable> catchBlock2, CatchBlockConsumer<? extends Throwable>... otherCatchBlocks);

    abstract Promise<T> peek(ThrowingConsume<? super T> dataConsume, List<CatchBlockConsumer<? extends Throwable>> catchBlocks);

    abstract <X extends Throwable> Promise<T> peek(ThrowingConsume<? super T> dataConsume, Class<X> exception, ThrowingConsume<? super X> failureConsumer);

    abstract <X extends Throwable> Promise<T> peek(ThrowingConsume<? super T> dataConsume, Class<X> exception, CatchBlockConsumer<? extends Throwable> catchBlock);

    //peekFlat
    abstract Promise<T> peekFlat(ThrowingConsume<? super Promise<T>> dataConsume);

    abstract Promise<T> peekFlat(ThrowingConsume<? super Promise<T>> dataConsume, ThrowingFunction<? super Throwable, ? extends Promise<T>> failureConsumer);

    abstract Promise<T> peekFlat(ThrowingConsume<? super Promise<T>> dataConsume, CatchBlock<? extends Throwable, ? extends Promise<T>> catchBlock1);

    abstract Promise<T> peekFlat(ThrowingConsume<? super Promise<T>> dataConsume, CatchBlock<? extends Throwable, ? extends Promise<T>> catchBlock1, CatchBlock<? extends Throwable, ? extends Promise<T>> catchBlock2);

    abstract Promise<T> peekFlat(ThrowingConsume<? super Promise<T>> dataConsume, CatchBlock<? extends Throwable, ? extends Promise<T>> catchBlock1, CatchBlock<? extends Throwable, ? extends Promise<T>> catchBlock2, CatchBlock<? extends Throwable, ? extends Promise<T>>... otherCatchBlocks);

    abstract Promise<T> peekFlat(ThrowingConsume<? super Promise<T>> dataConsume, List<CatchBlock<? extends Throwable, ? extends Promise<T>>> catchBlocks);

    abstract <X extends Throwable> Promise<T> peekFlat(ThrowingConsume<? super Promise<T>> dataConsume, Class<X> exception, ThrowingFunction<? super Throwable, ? extends Promise<T>> failureConsumer);

    abstract <X extends Throwable> Promise<T> peekFlat(ThrowingConsume<? super Promise<T>> dataConsume, Class<X> exception, CatchBlock<? extends Throwable, ? extends Promise<T>> catchBlock);

    //peekFailure
    abstract Promise<T> peekFailure(ThrowingConsume<? super Throwable> failureConsume);
    abstract Promise<T> peekFailure(CatchBlockConsumer<? extends Throwable> catchBlock1);
    abstract Promise<T> peekFailure(CatchBlockConsumer<? extends Throwable> catchBlock1, CatchBlockConsumer<? extends Throwable> catchBlock2);
    abstract Promise<T> peekFailure(CatchBlockConsumer<? extends Throwable> catchBlock1, CatchBlockConsumer<? extends Throwable> catchBlock2, CatchBlockConsumer<? extends Throwable>... otherCatchBlocks);
    abstract Promise<T> peekFailure(List<CatchBlockConsumer<? extends Throwable>> catchBlocks);
    abstract <X extends Throwable> Promise<T> peekFailure(Class<X> exception, ThrowingConsume<? super X> failureConsumer);
    abstract <X extends Throwable> Promise<T> peekFailure(Class<X> exception, CatchBlockConsumer<? extends Throwable> catchBlock);

    //peekFailureFlat
    abstract Promise<T> peekFailureFlat(ThrowingFunction<? super Throwable, ? extends Promise<?>> failureConsume);
    abstract Promise<T> peekFailureFlat(CatchBlock<? extends Throwable,? extends Promise<?>> catchBlock1);
    abstract Promise<T> peekFailureFlat(CatchBlock<? extends Throwable,? extends Promise<?>> catchBlock1, CatchBlockConsumer<? extends Throwable> catchBlock2);
    abstract Promise<T> peekFailureFlat(CatchBlock<? extends Throwable,? extends Promise<?>> catchBlock1, CatchBlockConsumer<? extends Throwable> catchBlock2, CatchBlockConsumer<? extends Throwable>... otherCatchBlocks);
    abstract Promise<T> peekFailureFlat(List<CatchBlock<? extends Throwable,? extends Promise<?>>> catchBlocks);
    abstract <X extends Throwable> Promise<T> peekFailureFlat(Class<X> exception, ThrowingFunction<? super X,? extends Promise<?>> failureConsumer);
    abstract <X extends Throwable> Promise<T> peekFailureFlat(Class<X> exception, CatchBlock<? extends Throwable,? extends Promise<?>> catchBlock);

}
