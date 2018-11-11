package zzz404.safesql;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ClassAnalyzer<T> {
    @SuppressWarnings("rawtypes")
    private static Map<Class, ClassAnalyzer> classMap = new HashMap<>();

    private Class<T> clazz;
    private Map<Method, MethodAnalyzer> methodMap = new HashMap<>();

    private ClassAnalyzer(Class<T> clazz) {
        this.clazz = clazz;
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
        MethodAnalyzer analyzer = methodMap.get(method);
        if (analyzer == null) {
            analyzer = new MethodAnalyzer(method);
            methodMap.put(method, analyzer);
        }
        return analyzer;
    }
}
