package zzz404.safesql.reflection;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TestMethodAnalyzer_Setter {

    @ParameterizedTest
    @ValueSource(strings = { "title", "settitle", "set", "setTitle_noParam", "setTitle_mutipleParam" })
    void test_isGetter_return_false(String methodName) {
        Method method = getMyMethod(methodName);
        assertFalse(new MethodAnalyzer(method).isSetter());
    }

    @ParameterizedTest
    @ValueSource(strings = { "setTitle_returnVoid", "setTitle_returnSomething" })
    void test_isGetter_return_true(String methodName) {
        Method method = getMyMethod(methodName);
        assertTrue(new MethodAnalyzer(method).isSetter());
    }

    private Method getMyMethod(String methodName) {
        Method[] methods = this.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }

    @Test
    void test_getType() {
        Method method = getMyMethod("setTitle_returnVoid");
        MethodAnalyzer methodAnalyzer = new MethodAnalyzer(method);

        assertEquals(method, methodAnalyzer.getMethod());

        assertEquals(String.class, methodAnalyzer.getType());
    }

    /**
     * NOT setter
     */
    public void title(String title) {
    }

    /**
     * NOT setter
     */
    public void settitle(String title) {
    }

    /**
     * NOT setter
     */
    public void set(String title) {
    }

    /**
     * NOT setter
     */
    public void setTitle_noParam() {
    }

    /**
     * NOT setter
     */
    public void setTitle_mutipleParam(int index, String title) {
    }

    /**
     * IS setter
     */
    public void setTitle_returnVoid(String title) {
    }

    /**
     * IS setter
     */
    public TestMethodAnalyzer_Setter setTitle_returnSomething(String title) {
        return this;
    }

}
