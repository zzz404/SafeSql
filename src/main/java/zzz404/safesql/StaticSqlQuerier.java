package zzz404.safesql;

import org.apache.commons.lang3.Validate;

import zzz404.safesql.sql.QuietPreparedStatement;

public class StaticSqlQuerier extends SqlQuerier {

    private String sql;
    private Object[] paramValues;

    public StaticSqlQuerier(String sql) {
        this.sql = sql;
    }

    public StaticSqlQuerier paramValues(Object... paramValues) {
        this.paramValues = paramValues;
        return this;
    }

    @Override
    public StaticSqlQuerier offset(int offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public StaticSqlQuerier limit(int limit) {
        this.limit = limit;
        return this;
    }

    protected String buildSql() {
        return sql;
    };

    protected String buildSql_for_queryCount() {
        String lowerSql = sql.toLowerCase();
        Validate.isTrue(lowerSql.startsWith("select "));

        String resultSql = sql;
        int pos = lowerSql.lastIndexOf(" order ");
        if (pos > 0) {
            resultSql = resultSql.substring(0, pos);
        }
        pos = lowerSql.indexOf(" from ");
        resultSql = "SELECT COUNT(*) FROM " + resultSql.substring(pos + 6);
        return resultSql;
    };

    @Override
    protected void setCondValueToPstmt(QuietPreparedStatement pstmt) {
        int i = 1;
        for (Object paramValue : paramValues) {
            pstmt.setObject(i++, paramValue);
        }
    }

}
