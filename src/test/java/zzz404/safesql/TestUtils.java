package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestUtils {
    @Test
    void test_isEquals_differentClass_return_false() {
        assertFalse(Utils.isEquals("", 1, null));
    }

}
