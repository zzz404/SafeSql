package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestEqualsSupport {
    @Test
    void test_isEquals_differentClass_return_false() {
        MayEquals o = new MayEquals(1, "");
        assertFalse(EqualsSupport.isEquals(o, ""));
    }

    @Test
    void test_isEquals_differentValue_return_false() {
        MayEquals o1 = new MayEquals(1, "aaa");
        MayEquals o2 = new MayEquals(1, "bbb");
        MayEquals o3 = new MayEquals(2, "aaa");
        assertFalse(EqualsSupport.isEquals(o1, o2));
        assertFalse(EqualsSupport.isEquals(o1, o3));
    }

    @Test
    void test_isEquals_sameValue_return_true() {
        MayEquals o1 = new MayEquals(1, "aaa");
        MayEquals o2 = new MayEquals(1, "aaa");
        assertTrue(EqualsSupport.isEquals(o1, o2));
    }

    public static class MayEquals implements EqualsSupport {
        public Integer id;
        public String title;

        public MayEquals(Integer id, String title) {
            this.id = id;
            this.title = title;
        }

        @Override
        public Object[] equalsByValues() {
            return new Object[] { id, title };
        }
    }
}
