package zzz404.safesql;

import java.lang.reflect.Method;

class MethodAnalyzer {

    private Method method;
    private boolean isGetter;
    private String propertyName;

    public MethodAnalyzer(Method method) {
        this.method = method;
        this.isGetter = analyze_isGetter();
    }

    public boolean isGetter() {
        return isGetter;
    }

    private boolean analyze_isGetter() {
        if (hasParameters() || hasNoReturnType()) {
            return false;
        }
        if (method.getReturnType() == boolean.class) {
            return matchGetterName_and_evaluatePropertyName("is");
        }
        else {
            return matchGetterName_and_evaluatePropertyName("get");
        }
    }

    private boolean matchGetterName_and_evaluatePropertyName(String prefix) {
        String methodName = method.getName();
        if (!methodName.startsWith(prefix)
                || methodName.length() <= prefix.length()) {
            return false;
        }
        char firstLetterAfterPrefix = methodName.toCharArray()[prefix.length()];

        boolean isGetter = Character.isUpperCase(firstLetterAfterPrefix);
        if (isGetter) {
            this.propertyName = Character.toLowerCase(firstLetterAfterPrefix)
                    + methodName.substring(prefix.length() + 1);
        }
        return isGetter;
    }

    private boolean hasNoReturnType() {
        return method.getReturnType() == void.class;
    }

    private boolean hasParameters() {
        return method.getParameterTypes().length > 0;
    }

    public String getPropertyName() {
        return propertyName;
    }

}
