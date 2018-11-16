package zzz404.safesql.sql;

import java.util.HashMap;
import java.util.Map;

public class QuietResultSetAnalyzer {

    private static final Map<Class<?>, QuietResultSetValueGetter> map = new HashMap<>();

    static {
        map.put(Integer.class, (rs, columnName) -> rs.getInt(columnName));
        map.put(Long.class, (rs, columnName) -> rs.getLong(columnName));
        map.put(String.class, (rs, columnName) -> rs.getString(columnName));
    }

    private QuietResultSet rs;

    public QuietResultSetAnalyzer(QuietResultSet rs) {
        this.rs = rs;
    }

    public <T> T readToObject(Class<T> clazz) {
        if(map.containsKey(clazz)) {
            map.get(clazz).getValue(rs, "");
        }
        return null;
    }

}
