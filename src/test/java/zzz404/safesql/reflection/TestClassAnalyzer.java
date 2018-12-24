package zzz404.safesql.reflection;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

import zzz404.safesql.util.NoisySupplier;

public class TestClassAnalyzer {

    @Test
    void test_getMethodAnalyzer() {
        Method setter = NoisySupplier.getQuietly(() -> this.getClass().getMethod("setAbc", String.class));

        ClassAnalyzer classAnalyzer = ClassAnalyzer.get(this.getClass());
        MethodAnalyzer methodAnalyzer = classAnalyzer.getMethodAnalyzer(setter);
        assertEquals(methodAnalyzer, classAnalyzer.find_setter_by_propertyName("abc"));
    }

    public void setAbc(String s) {
    }

}
