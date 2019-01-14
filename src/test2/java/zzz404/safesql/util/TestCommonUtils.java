package zzz404.safesql.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class TestCommonUtils {

    @Test
    void test_isEquals_differentClass_return_false() {
        assertFalse(CommonUtils.isEquals("", 1, null));
    }

    @Test
    void test_isEquals_sameObject_return_true() {
        String s = "";
        assertTrue(CommonUtils.isEquals(s, s, null));
    }

    @Test
    void test_isEquals_compareAttributes() {
        class Zzz {
            public int value;

            public Zzz(int value) {
                this.value = value;
            }
        }
        assertTrue(CommonUtils.isEquals(new Zzz(1), new Zzz(1), o -> new Object[] { o.value }));
        assertFalse(CommonUtils.isEquals(new Zzz(1), new Zzz(2), o -> new Object[] { o.value }));
    }

    @Test
    void test_isEquals_noField_return_true() {
        assertTrue(CommonUtils.isEquals("a", "", o -> null));
    }

    @Test
    void test_wrapToRuntime_checkedException_wrap() {
        Exception e = new Exception();
        RuntimeException e2 = CommonUtils.wrapToRuntime(e);
        assertEquals(e, e2.getCause());
    }

    @Test
    void test_wrapToRuntime_runtimeException_throw() {
        RuntimeException e = new RuntimeException();
        RuntimeException e2 = CommonUtils.wrapToRuntime(e);
        assertEquals(e, e2);
    }

    @Test
    void test__iter_to_stream_empty() {
        Stream<Object> stream = CommonUtils.iter_to_stream(Collections.emptyList().iterator());
        assertEquals(0, stream.count());
    }

    @Test
    void test__iter_to_stream_notEmpty() {
        List<String> list = Arrays.asList("1", "2");
        Stream<String> stream = CommonUtils.iter_to_stream(list.iterator());
        assertEquals(list, stream.collect(Collectors.toList()));
    }

    @Test
    void test_join() {
        String text = CommonUtils.join(Arrays.asList("a", "b"), ",", s -> s + "z");
        assertEquals("az,bz", text);
    }

    @ParameterizedTest
    @CsvSource({ "a, a", "aB, a_b", "aaBbb, aa_bbb", "A, a", "AB, a_b", "AaBbb, aa_bbb", "AaBbC, aa_bb_c" })
    void test__camelForm_to_snakeForm(String camelForm, String snakeForm) {
        assertEquals(snakeForm, CommonUtils.camelForm_to_snakeForm(camelForm));
    }

    @ParameterizedTest
    @CsvSource({ "a, a", "aaa, aaa", "a_b, aB", "a_bbb_cc, aBbbCc", "A, a", "AAA, aaa", "AaA_bBb_c, aaaBbbC" })
    void test__snakeForm_to_camelForm(String snakeForm, String camelForm) {
        assertEquals(camelForm, CommonUtils.snakeForm_to_camelForm(snakeForm));
    }

}
