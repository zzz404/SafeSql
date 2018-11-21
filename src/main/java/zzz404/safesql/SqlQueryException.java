package zzz404.safesql;

import zzz404.safesql.type.ValueType;

public final class SqlQueryException extends RuntimeException {
    private static final long serialVersionUID = -1139039721950839787L;

    private String sql;
    private Object[] paramValues;

    public SqlQueryException(String sql, Object[] paramValues, Throwable cause) {
        super(cause);
        this.sql = sql;
        this.paramValues = paramValues;
    }

    public String getSql() {
        return sql;
    }

    public Object[] getParamValues() {
        return paramValues;
    }

    public String getValuedSql() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (char c : sql.toCharArray()) {
            if (c != '?' || i >= paramValues.length) {
                sb.append(c);
            }
            else {
                sb.append(ValueType.valueToString(paramValues[i++]));
            }
        }
        return sb.toString();
    }

    @Override
    public String getMessage() {
        return "Error on query : " + getValuedSql();
    }
}
