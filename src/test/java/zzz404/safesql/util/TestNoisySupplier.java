package zzz404.safesql.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import zzz404.safesql.util.NoisySupplier;

public class TestNoisySupplier {

    @Test
    void test_getQuiet() {
        assertThrows(RuntimeException.class, () -> {
            NoisySupplier.getQuiet(() -> {
                throw new Exception();
            });
        });
    }

}