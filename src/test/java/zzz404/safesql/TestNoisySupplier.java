package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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
