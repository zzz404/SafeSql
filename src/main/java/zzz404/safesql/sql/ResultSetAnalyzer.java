package zzz404.safesql.sql;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class ResultSetAnalyzer {
    private static final Map<Class<?>, QuietResultSetValueExtractor<?>> map = new HashMap<>();

    static {
        map.put(Integer.class, (rs, i) -> rs.getInt(i));
        map.put(Long.class, (rs, i) -> rs.getLong(i));
        map.put(String.class, (rs, i) -> rs.getString(i));
    }

    private QuietResultSet rs;

    public ResultSetAnalyzer(QuietResultSet rs) {
        this.rs = rs;
    }

    public ResultSetAnalyzer(ResultSet rs) {
        this.rs = new QuietResultSet(rs);
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
