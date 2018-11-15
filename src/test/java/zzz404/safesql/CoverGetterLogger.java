package zzz404.safesql;

import org.junit.jupiter.api.Test;

import net.sf.cglib.proxy.Enhancer;
import zzz404.safesql.helper.TestUtils;

public class CoverGetterLogger {
    
    @Test
    void coverRest() {
        Enhancer en = new Enhancer();
        en.setSuperclass(Foo.class);
        GetterLogger<Foo> getterLogger = new GetterLogger<>(Foo.class);
        en.setCallback(getterLogger);

        Foo foo = (Foo) en.create();
        TestUtils.pass(() -> foo.getFoo());
    }

    public static class Foo {
        public String getFoo() {
            throw new RuntimeException();
        }
    }
}
