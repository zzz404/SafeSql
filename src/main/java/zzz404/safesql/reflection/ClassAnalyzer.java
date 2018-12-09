package zzz404.safesql.reflection;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ClassAnalyzer<T> {
    @SuppressWarnings("rawtypes")
    private static Map<Class, ClassAnalyzer> classMap = new HashMap<>();

    private Map<Method, MethodAnalyzer> methodMap = new HashMap<>();
    Map<String, MethodAnalyzer> prop_setterAnalyzer_map = new HashMap<>();

    private ClassAnalyzer(Class<T> clazz) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            MethodAnalyzer analyzer = new MethodAnalyzer(method);
            methodMap.put(method, analyzer);
            if (analyzer.isSetter()) {
                prop_setterAnalyzer_map.put(analyzer.getPropertyName(), analyzer);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <E> ClassAnalyzer<E> get(Class<E> clazz) {
        ClassAnalyzer<E> analyzer = classMap.get(clazz);
        if (analyzer == null) {
            analyzer = new ClassAnalyzer<E>(clazz);
            classMap.put(clazz, analyzer);
        }
        return analyzer;
    }

    public MethodAnalyzer getMethodAnalyzer(Method method) {
        return methodMap.get(method);
    }

    public MethodAnalyzer find_setter_by_propertyName(String propName) {
        return prop_setterAnalyzer_map.get(propName);
    }
}
