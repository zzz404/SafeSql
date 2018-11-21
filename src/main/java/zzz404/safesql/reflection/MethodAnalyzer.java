package zzz404.safesql.reflection;

import java.lang.reflect.Method;

public class MethodAnalyzer {

    private Method method;
    private boolean isGetter;
    private String columnName;
    private Class<?> type;
    private boolean isSetter;

    public MethodAnalyzer(Method method) {
        this.method = method;
        this.type = method.getReturnType();
        this.isGetter = analyze_isGetter();
        this.isSetter = analyze_isSetter();
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

    private boolean analyze_isGetter() {
        if (hasParameters() || hasNoReturnType()) {
            return false;
        }
        if (type == boolean.class) {
            return matchGetterName_and_evaluateColumnName("is");
        }
        else {
            return matchGetterName_and_evaluateColumnName("get");
        }
    }

    private boolean analyze_isSetter() {
        if (method.getParameterTypes().length != 1) {
            return false;
        }
        return matchSetterName_and_evaluateColumnName("set");
    }

    private boolean matchGetterName_and_evaluateColumnName(String prefix) {
        String methodName = method.getName();
        if (!methodName.startsWith(prefix) || methodName.length() <= prefix.length()) {
            return false;
        }
        char firstLetterAfterPrefix = methodName.toCharArray()[prefix.length()];

        boolean isGetter = Character.isUpperCase(firstLetterAfterPrefix);
        if (isGetter) {
            // TODO support JPA annotation
            this.columnName = Character.toLowerCase(firstLetterAfterPrefix) + methodName.substring(prefix.length() + 1);
        }
        return isGetter;
    }

    private boolean matchSetterName_and_evaluateColumnName(String prefix) {
        String methodName = method.getName();
        if (!methodName.startsWith(prefix) || methodName.length() <= prefix.length()) {
            return false;
        }
        char firstLetterAfterPrefix = methodName.toCharArray()[prefix.length()];

        boolean isSetter = Character.isUpperCase(firstLetterAfterPrefix);
        if (isSetter) {
            this.columnName = Character.toLowerCase(firstLetterAfterPrefix) + methodName.substring(prefix.length() + 1);
        }
        return isSetter;
    }

    private boolean hasNoReturnType() {
        return type == void.class;
    }

    private boolean hasParameters() {
        return method.getParameterTypes().length > 0;
    }

    public String getColumnName() {
        return columnName;
    }

    public Method getMethod() {
        return method;
    }

}
