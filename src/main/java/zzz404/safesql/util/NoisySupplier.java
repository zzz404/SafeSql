package zzz404.safesql.util;

import java.util.function.Supplier;

public interface NoisySupplier<T> {
    public T get() throws Throwable;

    public static <T> T getQuiet(NoisySupplier<T> supplier) {
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
