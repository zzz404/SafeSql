package zzz404.safesql.type;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

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

    public abstract void setToPstmt(QuietPreparedStatement pstmt, int index, T value);

    public abstract T readFromRs(QuietResultSet rs, int index);

    public abstract T readFromRs(QuietResultSet rs, String columnName);

    public String toString(T value) {
        return value.toString();
    }

    public T readFirstFromRs(QuietResultSet rs) {
        return readFromRs(rs, 1);
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

    public static <T> T mapRsRowToObject(QuietResultSet rs, Class<T> clazz) {
        ValueType<T> valueType = ValueType.get(clazz);
        if (valueType != null) {
            return valueType.readFirstFromRs(rs);
        }
        return null;
    }

}
