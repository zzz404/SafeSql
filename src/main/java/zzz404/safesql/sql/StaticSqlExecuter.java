package zzz404.safesql.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.Validate;

import zzz404.safesql.sql.type.TypedValue;

public class StaticSqlExecuter extends SqlQuerier {

    private String sql;
    private List<TypedValue<?>> paramValues = new ArrayList<>();

    public StaticSqlExecuter(DbSourceImpl dbSource) {
        super(dbSource);
    }

    public StaticSqlExecuter sql(String sql) {
        this.sql = sql;
        return this;
    }

    public StaticSqlExecuter paramValues(Object... paramValues) {
        Arrays.stream(paramValues).map(TypedValue::valueOf).forEach(this.paramValues::add);
        return this;
    }

    @Override
    public List<TypedValue<?>> paramValues() {
        return this.paramValues;
    }

    @Override
    public StaticSqlExecuter offset(int offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public StaticSqlExecuter limit(int limit) {
        this.limit = limit;
        return this;
    }

    public String sql() {
        return sql;
    }

    protected String sql_for_queryCount() {
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
    }

    public int update() {
        return dbSource.update(sql, paramValues);
    }

}
