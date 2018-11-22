package zzz404.safesql.reflection;

import java.lang.reflect.Method;

public class MethodAnalyzer {

    private static final String SETTER_PREFIX = "set";
    private static final String GETTER_PREFIX = "get";
    private static final String GETTER_boolean_PREFIX = "is";

    private Method method;
    private String methodName;
    private boolean isGetter = false;
    private boolean isSetter = false;
    private String columnName;
    private Class<?> type;

    public MethodAnalyzer(Method method) {
        this.method = method;
        this.methodName = method.getName();
        analyze();
    }

    private void analyze() {
        if (checkPrefix(methodName, SETTER_PREFIX)) {
            analyzeSetter();
        }
        else if (checkPrefix(methodName, GETTER_PREFIX)) {
            analyzeGetter(false);
        }
        else if (checkPrefix(methodName, GETTER_boolean_PREFIX)) {
            analyzeGetter(true);
        }
    }

    private void analyzeGetter(boolean expectPrimitiveBoolean) {
        if (method.getParameterTypes().length == 0) {
            Class<?> returnType = method.getReturnType();
            if ((returnType == boolean.class) == expectPrimitiveBoolean) {
                isGetter = true;
                this.type = returnType;
                this.columnName = evaluateColumnName(SETTER_PREFIX.length());
            }
        }
    }

    private void analyzeSetter() {
        Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes.length == 1) {
            isSetter = true;
            this.type = paramTypes[0];
            this.columnName = evaluateColumnName(SETTER_PREFIX.length());
        }
    }

    private String evaluateColumnName(int prefixLength) {
        char firstLetterAfterPrefix = methodName.toCharArray()[prefixLength];
        return Character.toLowerCase(firstLetterAfterPrefix) + methodName.substring(prefixLength + 1);
    }

    private boolean checkPrefix(String methodName, String prefix) {
        if (!methodName.startsWith(prefix)) {
            return false;
        }
        if (methodName.length() <= prefix.length()) {
            return false;
        }
        return Character.isUpperCase(methodName.charAt(prefix.length()));
    }

    public Class<?> getType() {
        return type;
    }

    public boolean isGetter() {
        return isGetter;
    }

    public boolean isSetter() {
        return isSetter;
    }

    public String getColumnName() {
        return columnName;
    }

    public Method getMethod() {
        return method;
    }

}