package zzz404.safesql.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestTuple2 {
    @Test
    void test_get() {
        Tuple2<String, String> tuple = new Tuple2<>("aa", "bb");
        assertEquals("aa", tuple.first());
        assertEquals("bb", tuple.second());
    }
}
