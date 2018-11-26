package zzz404.safesql.reflection;


import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;

import org.junit.jupiter.params.provider.ValueSource;

class TestMethodAnalyzer {

    //@ParameterizedTest
    @ValueSource(strings = { "title", "gettitle", "get", "getTitle_returnVoid", "getTitle_withParameter", "isTitle",
            "isEnabled_returnObjectBoolean", "getDisabled_returnPrimitiveBooolean" })
    void test_isGetter_return_false(String methodName) {
        Method method = getMyMethod(methodName);
        assertFalse(new MethodAnalyzer(method).isGetter());
    }

    // @ParameterizedTest
    @ValueSource(strings = { "getTitle", "isEnabled", "getDisabled" })
    void test_isGetter_return_true(String methodName) {
        Method method = getMyMethod(methodName);
        assertTrue(new MethodAnalyzer(method).isGetter());
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

    /**
     * NOT getter
     */
    public String title() {
        return null;
    }

    /**
     * NOT getter
     */
    public String gettitle() {
        return null;
    }

    /**
     * NOT getter
     */
    public String get() {
        return null;
    }

    /**
     * NOT getter
     */
    public void getTitle_returnVoid() {
    }

    /**
     * NOT getter
     */
    public String getTitle_withParameter(String s) {
        return s;
    }

    /**
     * IS getter
     */
    public String getTitle() {
        return null;
    }

    /**
     * NOT getter
     */
    public String isTitle() {
        return null;
    }

    /**
     * IS getter
     */
    public boolean isEnabled() {
        return true;
    }

    /**
     * NOT getter
     */
    public Boolean isEnabled_returnObjectBoolean() {
        return true;
    }

    /**
     * IS getter
     */
    public Boolean getDisabled() {
        return true;
    }

    /**
     * NOT getter
     */
    public boolean getDisabled_returnPrimitiveBooolean() {
        return true;
    }

}
