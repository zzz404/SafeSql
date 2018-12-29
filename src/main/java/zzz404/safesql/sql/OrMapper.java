package zzz404.safesql.sql;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import zzz404.safesql.reflection.ClassAnalyzer;
import zzz404.safesql.reflection.MethodAnalyzer;
import zzz404.safesql.sql.type.TypedValue;
import zzz404.safesql.util.NoisyRunnable;
import zzz404.safesql.util.NoisySupplier;

public class OrMapper<T> {
    private static final Map<String, OrMapper<?>> instanceMap = Collections.synchronizedMap(new HashMap<>());

    private Class<T> clazz;
    private TableSchema tableSchema;
    private Map<String, MethodAnalyzer> realColumn_getter_map = new HashMap<>();
    private Map<String, MethodAnalyzer> realColumn_setter_map = new HashMap<>();

    public static <T> OrMapper<T> get(Class<T> clazz, DbSourceImpl dbSource) {
        String key = dbSource.getName() + "__" + clazz.getName();
        @SuppressWarnings("unchecked")
        OrMapper<T> instance = (OrMapper<T>) instanceMap.get(key);
        if (instance == null) {
            instance = new OrMapper<>(clazz, dbSource);
            instanceMap.put(key, instance);
        }
        return instance;
    }

    OrMapper(Class<T> clazz, DbSourceImpl dbSource) {
        this.tableSchema = dbSource.getSchema(clazz.getSimpleName());
        ClassAnalyzer classAnalyzer = ClassAnalyzer.get(clazz);
        Collection<MethodAnalyzer> getterAnalyzers = classAnalyzer.get_all_getterAnalyzers();
        for (MethodAnalyzer methodAnalyzer : getterAnalyzers) {
            if (TypedValue.supportType(methodAnalyzer.getType())) {
                String realColumnName = tableSchema.getRealColumnName(methodAnalyzer.getPropertyName());
                realColumn_getter_map.put(realColumnName, methodAnalyzer);
            }
        }
        Collection<MethodAnalyzer> setterAnalyzers = classAnalyzer.get_all_setterAnalyzers();
        for (MethodAnalyzer methodAnalyzer : setterAnalyzers) {
            if (TypedValue.supportType(methodAnalyzer.getType())) {
                String realColumnName = tableSchema.getRealColumnName(methodAnalyzer.getPropertyName());
                realColumn_getter_map.put(realColumnName, methodAnalyzer);
            }
        }
    }

    public String getRealTableName() {
        return tableSchema.getRealTableName();
    }

    public Collection<String> get_realColumnName_of_all_getters() {
        return realColumn_getter_map.keySet();
    }

    public TypedValue<?> getValue(T o, String realColumnName) {
        MethodAnalyzer analyzer = realColumn_getter_map.get(realColumnName);
        Object value = NoisySupplier.getQuietly(() -> analyzer.getMethod().invoke(o));
        return TypedValue.valueOf(value);
    }

    public boolean setValueToPstmt(QuietPreparedStatement pstmt, int i, T o, String realColumnName) {
        MethodAnalyzer analyzer = realColumn_getter_map.get(realColumnName);
        if (analyzer != null) {
            Object value = NoisySupplier.getQuietly(() -> analyzer.getMethod().invoke(o));
            TypedValue.valueOf(value).setToPstmt(pstmt, i);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean setValueToObject(T o, String realColumnName, QuietResultSet rs, int i) {
        MethodAnalyzer analyzer = realColumn_setter_map.get(realColumnName);
        if (analyzer != null) {
            TypedValue<?> tv = TypedValue.valueOf(analyzer.getType()).readFromRs(rs, i);
            NoisyRunnable.runQuietly(() -> analyzer.getMethod().invoke(tv.getValue()));
            return true;
        }
        else {
            return false;
        }
    }

    public T mapToObject(QuietResultSet rs, Set<String> selectedColumns, Map<String, String> realColumn_prop_map) {
        return NoisySupplier.getQuietly(() -> {
            T o = clazz.newInstance();
            ClassAnalyzer classAnalyzer = ClassAnalyzer.get(clazz);
            Set<String> realColumnNames = CollectionUtils.isNotEmpty(selectedColumns) ? selectedColumns
                    : TableSchema.getColumns(rs.getMetaData());
            for (String columnName : realColumnNames) {
                String propertyName = columnName;
                if (realColumn_prop_map != null) {
                    propertyName = realColumn_prop_map.containsKey(columnName) ? realColumn_prop_map.get(columnName)
                            : columnName;
                }
                MethodAnalyzer analyzerOfSetter = classAnalyzer.find_setter_by_propertyName(propertyName);
                if (analyzerOfSetter != null) {
                    Class<?> type = analyzerOfSetter.getType();
                    TypedValue<?> tv = TypedValue.valueOf(type);
                    if (tv != null) {
                        tv.readFromRs(rs, columnName);
                        analyzerOfSetter.getMethod().invoke(o, tv.getValue());
                    }
                }
            }
            return o;
        });
    }

    public TableSchema getTableSchema() {
        return tableSchema;
    }

}
