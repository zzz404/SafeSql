package zzz404.safesql.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import zzz404.safesql.util.NoisyRunnable;

public class TestNoisyRunnable {

    @Test
    void test_runQuiet() {
        assertThrows(RuntimeException.class, () -> {
            NoisyRunnable.runQuiet(() -> {
                throw new Exception();
            });
        });
    }

}
