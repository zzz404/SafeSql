package zzz404.safesql.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

public class TestNoisySupplier {

    @Test
    void test_getQuiet_hasException_throwRuntime() {
        assertThrows(RuntimeException.class, () -> {
            NoisySupplier.getQuietly(() -> {
                throw new Exception();
            });
        });
    }

    @Test
    void test_shutUp_hasException_throwRuntime() {
        Supplier<?> supplier = NoisySupplier.shutUp(() -> {
            throw new Exception();
        });
        assertThrows(RuntimeException.class, () -> supplier.get());
    }

    @Test
    void cover_rest() {
        NoisySupplier.getQuietly(() -> null);
        Supplier<?> supplier = NoisySupplier.shutUp(() -> null);
        supplier.get();
    }
}
