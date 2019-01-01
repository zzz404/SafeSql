package zzz404.safesql.util;

import java.util.function.Function;

@FunctionalInterface
public interface NoisyFunction<T, R> {
    R apply(T t) throws Exception;

    public static <X, Y> Function<X, Y> shutUp(NoisyFunction<X, Y> func) {
        return (X x) -> {
            try {
                return func.apply(x);
            }
            catch (Exception e) {
                throw CommonUtils.wrapToRuntime(e);
            }
        };
    }
}
