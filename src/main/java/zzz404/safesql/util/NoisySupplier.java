package zzz404.safesql.util;

import java.util.function.Supplier;

@FunctionalInterface
public interface NoisySupplier<T> {
    public T get() throws Throwable;

    public static <T> T getQuietly(NoisySupplier<T> supplier) {
        try {
            return supplier.get();
        }
        catch (Throwable e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public static <T> Supplier<T> shutUp(NoisySupplier<T> supplier) {
        return (() -> {
            try {
                return supplier.get();
            }
            catch (Throwable e) {
                throw CommonUtils.wrapToRuntime(e);
            }
        });
    }
}
