package zzz404.safesql.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestCommonUtils {
    @Test
    void test_isEquals_differentClass_return_false() {
        assertFalse(CommonUtils.isEquals("", 1, null));
    }

    @Test
    void test_isEquals_noField_return_true() {
        assertTrue(CommonUtils.isEquals("", "", o -> null));
    }

    @Test
    void coverRest() {
        CommonUtils.wrapToRuntime(new Exception());
    }
}
