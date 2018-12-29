package zzz404.safesql.sql;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;

import zzz404.safesql.sql.type.TypedValue;

public class StaticSqlExecuterImpl extends SqlQuerier {

    private String sql;
    private List<TypedValue<?>> paramValues;

    public StaticSqlExecuterImpl(DbSourceImpl dbSource) {
        super(dbSource);
    }

    public StaticSqlExecuterImpl sql(String sql) {
        this.sql = sql;
        return this;
    }

    public StaticSqlExecuterImpl paramValues(Object... paramValues) {
        this.paramValues = Arrays.stream(paramValues).map(TypedValue::valueOf).collect(Collectors.toList());
        return this;
    }

    public StaticSqlExecuterImpl paramValues(List<TypedValue<?>> paramValues) {
        this.paramValues = paramValues;
        return this;
    }

    @Override
    public List<TypedValue<?>> paramValues() {
        return this.paramValues;
    }

    @Override
    public StaticSqlExecuterImpl offset(int offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public StaticSqlExecuterImpl limit(int limit) {
        this.limit = limit;
        return this;
    }

    protected String sql() {
        return sql;
    };

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
    };

    public int update() {
        return dbSource.withConnection(conn -> {
            if (CollectionUtils.isEmpty(paramValues)) {
                QuietStatement stmt = conn.createStatement();
                return stmt.executeUpdate(sql);
            }
            else {
                QuietPreparedStatement pstmt = conn.prepareStatement(sql);
                int i = 1;
                for (TypedValue<?> tv : paramValues) {
                    tv.setToPstmt(pstmt, i++);
                }
                return pstmt.executeUpdate();
            }
        });
    };

}
