package zzz404.safesql.value;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public abstract class TypedValue<T> {
    @SuppressWarnings("rawtypes")
    private static final Map<Class, Supplier<TypedValue>> map = new HashMap<>();

    static {
        // most common
        map.put(Integer.class, () -> new IntegerValue());
        map.put(int.class, () -> new IntegerValue());
        map.put(String.class,()-> new StringValue());

        // common
        map.put(Boolean.class,()-> new BooleanValue());
        map.put(boolean.class,()-> new BooleanValue());
        map.put(Long.class,()-> new LongValue());
        map.put(long.class,()-> new LongValue());
        map.put(Date.class,()-> new SqlDateValue());
        map.put(Double.class,()-> new DoubleValue());
        map.put(double.class,()-> new DoubleValue());
        map.put(Float.class,()-> new FloatValue());
        map.put(float.class,()-> new FloatValue());
        map.put(Short.class,()-> new ShortValue());
        map.put(short.class,()-> new ShortValue());
        map.put(Time.class,()-> new TimeValue());
        map.put(Timestamp.class,()-> new TimestampValue());

        // additional
        map.put(java.util.Date.class,()-> new UtilDateValue());
        map.put(Instant.class,()-> new InstantValue());
    }

    @SuppressWarnings({ "unchecked" })
    public static <E> TypedValue<E> get(Class<? extends E> clazz) {
        if (map.containsKey(clazz)) {
            return map.get(clazz).get();
        }
        else {
            return null;
        }
    }

    protected T value;

    public abstract void setToPstmt(QuietPreparedStatement pstmt, int index);

    public abstract void readFromRs(QuietResultSet rs, int index);

    public abstract void readFromRs(QuietResultSet rs, String columnName);

    public String toValueString() {
        return value.toString();
    }

    public void readFirstFromRs(QuietResultSet rs) {
        readFromRs(rs, 1);
    }

    public static <T> String valueToString(T value) {
        if (value == null) {
            return "null";
        }
        @SuppressWarnings("unchecked")
        TypedValue<T> tv = (TypedValue<T>) get(value.getClass());
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
        TypedValue<T> tv = TypedValue.get(clazz);
        if (tv != null) {
            tv.readFirstFromRs(rs);
        }
        return tv.value;
    }

}
