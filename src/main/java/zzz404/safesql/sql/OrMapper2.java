package zzz404.safesql.sql;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import zzz404.safesql.reflection.ClassAnalyzer;
import zzz404.safesql.reflection.MethodAnalyzer;
import zzz404.safesql.type.ValueType;
import zzz404.safesql.util.NoisySupplier;

public class OrMapper2<T> {
    private Class<T> clazz;
    private QuietResultSet rs;
    private Set<String> selectedColumns = null;
    private Set<String> columnsOfResultSet = null;

    public OrMapper2(Class<T> clazz, QuietResultSet rs) {
        this.clazz = clazz;
        this.rs = rs;
    }

    public OrMapper2<T> selectColumns(Set<String> selectedColumns) {
        this.selectedColumns = selectedColumns;
        return this;
    }

    public T mapToObject() {
        return mapToObject(null);
    }

    public T mapToObject(Map<String, String> realColumn_prop_map) {
        return NoisySupplier.getQuietly(() -> {
            T o = ValueType.mapRsRowToObject(rs, clazz);
            if (o != null) {
                return o;
            }
            o = clazz.newInstance();
            ClassAnalyzer classAnalyzer = ClassAnalyzer.get(clazz);
            Set<String> columnNames = CollectionUtils.isNotEmpty(selectedColumns) ? selectedColumns
                    : getColumnsOfResultSet();
            for (String columnName : columnNames) {
                String propertyName = columnName;
                if (realColumn_prop_map != null) {
                    propertyName = realColumn_prop_map.containsKey(columnName) ? realColumn_prop_map.get(columnName) : columnName;
                }
                MethodAnalyzer analyzerOfSetter = classAnalyzer.find_setter_by_propertyName(propertyName);
                if (analyzerOfSetter != null) {
                    Class<?> type = analyzerOfSetter.getType();
                    ValueType<?> vType = ValueType.get(type);
                    if (vType != null) {
                        Object value = vType.readFromRs(rs, columnName);
                        Method setter = analyzerOfSetter.getMethod();
                        setter.invoke(o, value);
                    }
                }
            }
            return o;
        });
    }

    Set<String> getColumnsOfResultSet() {
        if (CollectionUtils.isEmpty(columnsOfResultSet)) {
            columnsOfResultSet = TableSchema.getColumns(rs.getMetaData());
        }
        return columnsOfResultSet;
    }
}
