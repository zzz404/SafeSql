package zzz404.safesql.sql.type;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import zzz404.safesql.sql.proxy.QuietPreparedStatement;
import zzz404.safesql.sql.proxy.QuietResultSet;
import zzz404.safesql.util.CommonUtils;

public abstract class TypedValue<T> {
    @SuppressWarnings("rawtypes")
    private static final Map<Class, Supplier<TypedValue>> map = new HashMap<>();

    static {
        // most common
        map.put(Integer.class, () -> new IntegerValue());
        map.put(int.class, () -> new IntegerValue());
        map.put(String.class, () -> new StringValue());

        // common
        map.put(Boolean.class, () -> new BooleanValue());
        map.put(boolean.class, () -> new BooleanValue());
        map.put(Long.class, () -> new LongValue());
        map.put(long.class, () -> new LongValue());
        map.put(Date.class, () -> new SqlDateValue());
        map.put(Double.class, () -> new DoubleValue());
        map.put(double.class, () -> new DoubleValue());
        map.put(Float.class, () -> new FloatValue());
        map.put(float.class, () -> new FloatValue());
        map.put(Short.class, () -> new ShortValue());
        map.put(short.class, () -> new ShortValue());
        map.put(Time.class, () -> new TimeValue());
        map.put(Timestamp.class, () -> new TimestampValue());

        // additional
        map.put(java.util.Date.class, () -> new UtilDateValue());
        map.put(Instant.class, () -> new InstantValue());
    }

    public static boolean supportType(Class<?> clazz) {
        return map.containsKey(clazz);
    }

    @SuppressWarnings({ "unchecked" })
    public static <E> TypedValue<E> valueOf(Class<E> clazz) {
        if (map.containsKey(clazz)) {
            return map.get(clazz).get();
        }
        else {
            throw new UnsupportedValueTypeException(clazz);
        }
    }

    @SuppressWarnings({ "unchecked" })
    public static <E> TypedValue<E> valueOf(E o) {
        TypedValue<E> tv = valueOf((Class<E>) o.getClass());
        tv.value = o;
        return tv;
    }

    protected T value;

    public abstract TypedValue<T> setToPstmt(QuietPreparedStatement pstmt, int index);

    public abstract TypedValue<T> readFromRs(QuietResultSet rs, int index);

    public abstract TypedValue<T> readFromRs(QuietResultSet rs, String columnName);

    public String toValueString() {
        return value.toString();
    }

    public String toString() {
        return toValueString();
    }

    public TypedValue<T> readFirstFromRs(QuietResultSet rs) {
        readFromRs(rs, 1);
        return this;
    }

    public static <T> String valueToString(T value) {
        if (value == null) {
            return "null";
        }
        @SuppressWarnings("unchecked")
        TypedValue<T> tv = (TypedValue<T>) valueOf(value.getClass());
        if (tv == null) {
            return value.toString();
        }
        else {
            return tv.toValueString();
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

    public static <T> T mapRsRowToObject(QuietResultSet rs, Class<T> clazz) {
        TypedValue<T> tv = TypedValue.valueOf(clazz);
        if (tv != null) {
            tv.readFirstFromRs(rs);
        }
        return tv.value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object that) {
        return CommonUtils.isEquals(this, that, tv -> new Object[] { tv.value });
    }

}
