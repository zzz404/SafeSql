package zzz404.safesql.sql;

import java.util.Set;

import org.apache.commons.lang3.Validate;

import zzz404.safesql.type.ValueType;

public class QuietResultSetAnalyzer {
    private QuietResultSet rs;

    public QuietResultSetAnalyzer(QuietResultSet rs) {
        this.rs = rs;
    }

    public <T> T mapRsToObject(Class<T> clazz, Set<String> columns) {
        ValueType<T> valueType = ValueType.get(clazz);
        if (valueType != null) {
            Validate.isTrue(columns == null || columns.size() <= 1);
            return valueType.readFirstFromRs(rs);
        }
        if (columns == null) {
            // select all
            // TODO
            return null;
        }
        else {
            // select by columns
            // TODO
            return null;
        }
    }
}
