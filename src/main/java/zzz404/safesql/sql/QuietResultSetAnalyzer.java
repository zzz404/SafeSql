package zzz404.safesql.sql;

import zzz404.safesql.type.ValueType;

public class QuietResultSetAnalyzer {
    private QuietResultSet rs;

    public QuietResultSetAnalyzer(QuietResultSet rs) {
        this.rs = rs;
    }

    public <T> T mapRsToObject(Class<T> clazz) {
        ValueType<T> valueType = ValueType.get(clazz);
        if (valueType != null) {
            return valueType.readFirstFromRs(rs);
        }
        else {
            // TODO
            return null;
        }
    }
}
