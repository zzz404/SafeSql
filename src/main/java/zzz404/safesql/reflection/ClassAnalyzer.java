package zzz404.safesql.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ClassAnalyzer<T> {
    @SuppressWarnings("rawtypes")
    private static Map<Class, ClassAnalyzer> classMap = new HashMap<>();

    private Map<Method, MethodAnalyzer> methodMap = new HashMap<>();
    private Map<String, MethodAnalyzer> columnName_analyzer_map = new HashMap<>();

    private ClassAnalyzer(Class<T> clazz) {
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
            if (analyzer.isSetter()) {
                columnName_analyzer_map.put(analyzer.getColumnName(), analyzer);
            }
        }
        return analyzer;
    }

    public static Field[] getAllField(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        return fields;
    }

    public MethodAnalyzer find_setter_by_columnName(String columnName) {
        return columnName_analyzer_map.get(columnName);
    }
}
