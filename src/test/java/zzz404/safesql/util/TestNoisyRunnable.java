package zzz404.safesql.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestNoisyRunnable {

    @Test
    void test_runQuiet() {
        assertThrows(RuntimeException.class, () -> {
            NoisyRunnable.runQuietly(() -> {
                throw new Exception();
            });
        });
    }

    @Test
    void test_runIgnoreException() {
        NoisyRunnable.runIgnoreException(() -> {
            throw new Exception();
        });
    }

}
