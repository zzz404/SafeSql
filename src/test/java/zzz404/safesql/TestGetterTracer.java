package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.sf.cglib.proxy.Enhancer;
import zzz404.safesql.helper.UtilsForTest;

public class TestGetterTracer {

    @Test
    void testWrapException() {
        Enhancer en = new Enhancer();
        en.setSuperclass(Foo.class);
        GetterTracer<Foo> getterTracer = new GetterTracer<>(Foo.class);
        en.setCallback(getterTracer);

        Foo foo = (Foo) en.create();
        assertThrows(RuntimeException.class, () -> {
            UtilsForTest.underQueryContext(() -> foo.getFoo());
        });
    }

    public static class Foo {
        public String getFoo() {
            throw new RuntimeException();
        }
    }
}
