package zzz404.safesql.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestTuple3 {
    @Test
    void test_get() {
        Tuple3<String, String, String> tuple = new Tuple3<>("aa", "bb", "cc");
        assertEquals("aa", tuple.first());
        assertEquals("bb", tuple.second());
        assertEquals("cc", tuple.third());
    }
}
