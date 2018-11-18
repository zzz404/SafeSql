package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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
