package zzz404.safesql.sql;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class QuietResultSetAnalyzer {
    private static final Map<Class<?>, QuietResultSetValueExtractor<?>> map = new HashMap<>();

    static {
        // most common
        map.put(Integer.class, (rs, i) -> rs.getInt(i));
        map.put(int.class, (rs, i) -> rs.getInt(i));
        map.put(String.class, (rs, i) -> rs.getString(i));
        
        // common
        map.put(Boolean.class, (rs, i) -> rs.getBoolean(i));
        map.put(boolean.class, (rs, i) -> rs.getBoolean(i));
        map.put(Long.class, (rs, i) -> rs.getLong(i));
        map.put(long.class, (rs, i) -> rs.getLong(i));
        map.put(Date.class, (rs, i) -> rs.getDate(i));
        map.put(Double.class, (rs, i) -> rs.getDouble(i));
        map.put(double.class, (rs, i) -> rs.getDouble(i));
        map.put(Float.class, (rs, i) -> rs.getFloat(i));
        map.put(float.class, (rs, i) -> rs.getFloat(i));
        map.put(Short.class, (rs, i) -> rs.getShort(i));
        map.put(short.class, (rs, i) -> rs.getShort(i));
        map.put(Time.class, (rs, i) -> rs.getTime(i));
        map.put(Timestamp.class, (rs, i) -> rs.getTimestamp(i));
        
        // additional
        map.put(java.util.Date.class, (rs, i) -> rs.getTimestamp(i));
        map.put(Instant.class, (rs, i) -> rs.getTimestamp(i).toInstant());
    }

    private QuietResultSet rs;

    public QuietResultSetAnalyzer(QuietResultSet rs) {
        this.rs = rs;
    }

    @SuppressWarnings("unchecked")
    public <T> T mapRsToObject(Class<T> clazz) {
        QuietResultSetValueExtractor<T> extractor = (QuietResultSetValueExtractor<T>) map.get(clazz);
        if (extractor != null) {
            return extractor.extractFirst(rs);
        }
        return null;
    }
}
