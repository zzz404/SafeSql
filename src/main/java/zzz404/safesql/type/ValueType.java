package zzz404.safesql.type;

import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import zzz404.safesql.reflection.ClassAnalyzer;
import zzz404.safesql.reflection.MethodAnalyzer;
import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;
import zzz404.safesql.util.NoisySupplier;

public abstract class ValueType<T> {
    @SuppressWarnings("rawtypes")
    private static final Map<Class, ValueType> map = new HashMap<>();

    static {
        // most common
        map.put(Integer.class, new IntegerType());
        map.put(int.class, new IntegerType());
        map.put(String.class, new StringType());

        // common
        map.put(Boolean.class, new BooleanType());
        map.put(boolean.class, new BooleanType());
        map.put(Long.class, new LongType());
        map.put(long.class, new LongType());
        map.put(Date.class, new SqlDateType());
        map.put(Double.class, new DoubleType());
        map.put(double.class, new DoubleType());
        map.put(Float.class, new FloatType());
        map.put(float.class, new FloatType());
        map.put(Short.class, new ShortType());
        map.put(short.class, new ShortType());
        map.put(Time.class, new TimeType());
        map.put(Timestamp.class, new TimestampType());

        // additional
        map.put(java.util.Date.class, new UtilDateType());
        map.put(Instant.class, new InstantType());
    }

    @SuppressWarnings({ "unchecked" })
    public static <E> ValueType<E> get(Class<E> clazz) {
        if (map.containsKey(clazz)) {
            return map.get(clazz);
        }
        else {
            return null;
        }
    }

    public abstract T readFromRs(QuietResultSet rs, String columnName);

    public abstract T readFromRs(QuietResultSet rs, int index);

    public T readFirstFromRs(QuietResultSet rs) {
        return readFromRs(rs, 1);
    }

    public abstract void setToPstmt(QuietPreparedStatement pstmt, int index, T value);

    public String toString(T value) {
        return value.toString();
    }

    public static <T> String valueToString(T value) {
        if (value == null) {
            return "null";
        }
        @SuppressWarnings("unchecked")
        ValueType<T> valueType = (ValueType<T>) get(value.getClass());
        if (valueType == null) {
            return value.toString();
        }
        else {
            return valueType.toString(value);
        }
    }

    protected T primitiveToObject(T value, QuietResultSet rs) {
        if (rs.wasNull()) {
            return null;
        }
        else {
            return value;
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void setValueToPstmt(QuietPreparedStatement pstmt, int index, Object value) {
        if (value == null) {
            pstmt.setObject(index, null);
            return;
        }
        ValueType valueType = ValueType.get(value.getClass());
        if (valueType != null) {
            valueType.setToPstmt(pstmt, index, value);
        }
        else {
            pstmt.setObject(index, value);
        }
    }

    public static <R> R mapRsRowToObject(QuietResultSet rs, Class<R> clazz, String... columnNames) {
        ValueType<R> valueType = ValueType.get(clazz);
        if (valueType != null) {
            return valueType.readFirstFromRs(rs);
        }
        else {
            return toObject(rs, clazz, columnNames);
        }
    }

    private static <R> R toObject(QuietResultSet rs, Class<R> clazz, String... columnNames) {
        return NoisySupplier.getQuietly(() -> {
            R o = clazz.newInstance();
            ClassAnalyzer<R> classAnalyzer = ClassAnalyzer.get(clazz);
            for (String columnName : columnNames) {
                MethodAnalyzer analyzerOfSetter = classAnalyzer.find_setter_by_columnName();
                if (analyzerOfSetter != null) {
                    Class<?> type = analyzerOfSetter.getType();
                    ValueType<?> valueType = ValueType.get(type);
                    if (valueType != null) {
                        Object value = valueType.readFromRs(rs, columnName);
                        Method setter = analyzerOfSetter.getMethod();
                        setter.invoke(o, value);
                    }
                }
            }
            return o;
        });
    }

}
