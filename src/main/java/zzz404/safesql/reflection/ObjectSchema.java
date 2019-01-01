package zzz404.safesql.reflection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zzz404.safesql.sql.type.TypedValue;
import zzz404.safesql.util.CommonUtils;
import zzz404.safesql.util.NoisySupplier;

public class ObjectSchema {
    @SuppressWarnings("rawtypes")
    private static Map<Class, ObjectSchema> classMap = new HashMap<>();

    private Class<?> objClass;
    private ClassAnalyzer classAnalyzer;

    private List<MethodAnalyzer> allValidGetters = new ArrayList<>();
    private Map<String, MethodAnalyzer> getterMap = new HashMap<>();
    private Map<String, MethodAnalyzer> lcProp_setter_map = new HashMap<>();
    private Map<String, MethodAnalyzer> snakedProp_setter_map = new HashMap<>();

    private ObjectSchema(Class<?> objClass) {
        this.objClass = objClass;
        this.classAnalyzer = ClassAnalyzer.get(objClass);
        init();
    }

    public static ObjectSchema get(Class<?> clazz) {
        ObjectSchema analyzer = classMap.get(clazz);
        if (analyzer == null) {
            analyzer = new ObjectSchema(clazz);
            classMap.put(clazz, analyzer);
        }
        return analyzer;
    }

    private synchronized void init() {
        for (MethodAnalyzer ma : classAnalyzer.get_all_getterAnalyzers()) {
            if (TypedValue.supportType(ma.getType())) {
                allValidGetters.add(ma);
            }
            getterMap.put(ma.getPropertyName(), ma);
        }
        for (MethodAnalyzer methodAnalyzer : classAnalyzer.get_all_setterAnalyzers()) {
            String prop = methodAnalyzer.getPropertyName();
            lcProp_setter_map.put(prop.toLowerCase(), methodAnalyzer);
            lcProp_setter_map.put(CommonUtils.camelForm_to_snakeForm(prop), methodAnalyzer);
        }
    }

    public Collection<MethodAnalyzer> findAllValidGetters() {
        return allValidGetters;
    }

    public MethodAnalyzer findGetter_by_propName(String prop) {
        return getterMap.get(prop);
    }

    public MethodAnalyzer findSetter_by_lcPropName(String lcPropName) {
        return lcProp_setter_map.get(lcPropName);
    }

    public MethodAnalyzer findValidSetter_by_snakedPropName(String snakedPropName) {
        return snakedProp_setter_map.get(snakedPropName);
    }

    public Class<?> getObjClass() {
        return objClass;
    }

    public Object getValue(Object o, String propName) {
        return NoisySupplier.getQuietly(() -> getterMap.get(propName).getMethod().invoke(o));
    }

}
