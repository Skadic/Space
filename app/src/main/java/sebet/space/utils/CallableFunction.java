package sebet.space.utils;

import java.util.concurrent.Callable;

/**
 * Created by eti22 on 29.06.2017.
 */

public abstract class CallableFunction<PassType, ReturnType>{

    protected abstract ReturnType operate(PassType type);

    public final Callable<ReturnType> pass(final PassType type){
        return new Callable<ReturnType>() {
            @Override
            public ReturnType call() throws Exception {
                return operate(type);
            }
        };
    }
}
