package zzz404.safesql.reflection;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ClassAnalyzer {
    @SuppressWarnings("rawtypes")
    private static Map<Class, ClassAnalyzer> classMap = new HashMap<>();

    private Map<Method, MethodAnalyzer> methodMap = new HashMap<>();
    Map<String, MethodAnalyzer> prop_setterAnalyzer_map = new HashMap<>();
    Map<String, MethodAnalyzer> prop_getterAnalyzer_map = new HashMap<>();

    private ClassAnalyzer(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            MethodAnalyzer analyzer = new MethodAnalyzer(method);
            methodMap.put(method, analyzer);
            if (analyzer.isSetter()) {
                prop_setterAnalyzer_map.put(analyzer.getPropertyName(), analyzer);
            }
            else if (analyzer.isGetter()) {
                prop_getterAnalyzer_map.put(analyzer.getPropertyName(), analyzer);
            }
        }
    }

    public static ClassAnalyzer get(Class<?> clazz) {
        ClassAnalyzer analyzer = classMap.get(clazz);
        if (analyzer == null) {
            analyzer = new ClassAnalyzer(clazz);
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

    public MethodAnalyzer find_getter_by_propertyName(String propName) {
        return prop_getterAnalyzer_map.get(propName);
    }

    public Collection<MethodAnalyzer> get_all_getterAnalyzers() {
        return prop_getterAnalyzer_map.values();
    }

    public Collection<MethodAnalyzer> get_all_setterAnalyzers() {
        return prop_setterAnalyzer_map.values();
    }

}
