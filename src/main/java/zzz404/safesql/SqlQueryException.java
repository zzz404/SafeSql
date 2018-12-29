package zzz404.safesql;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import zzz404.safesql.sql.type.TypedValue;

public final class SqlQueryException extends RuntimeException {
    private static final long serialVersionUID = -1139039721950839787L;

    private String sql;
    private List<TypedValue<?>> paramValues;

    public SqlQueryException(String sql, List<TypedValue<?>> paramValues, Throwable cause) {
        super(cause);
        this.sql = sql;
        this.paramValues = paramValues != null ? paramValues : Collections.emptyList();
    }

    public String getSql() {
        return sql;
    }

    public List<TypedValue<?>> getParamValues() {
        return paramValues;
    }

    public String getValuedSql() {
        StringBuilder sb = new StringBuilder();
        Iterator<TypedValue<?>> iter = paramValues.iterator();
        for (char c : sql.toCharArray()) {
            if (c != '?' || !iter.hasNext()) {
                sb.append(c);
            }
            else {
                sb.append(iter.next().toValueString());
            }
        }
        return sb.toString();
    }

    @Override
    public String getMessage() {
        return "Error on query : " + getValuedSql();
    }
}
