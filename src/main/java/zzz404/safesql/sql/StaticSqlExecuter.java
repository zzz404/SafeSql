package zzz404.safesql.sql;

import javax.swing.tree.VariableHeightLayoutCache;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;

import zzz404.safesql.type.ValueType;

public class StaticSqlExecuter extends SqlQuerier {

    private String sql;
    private Object[] paramValues;

    public StaticSqlExecuter(DbSourceImpl dbSource) {
        super(dbSource);
    }

    public StaticSqlExecuter sql(String sql) {
        this.sql = sql;
        return this;
    }

    public StaticSqlExecuter paramValues(Object... paramValues) {
        this.paramValues = paramValues;
        return this;
    }

    @Override
    public Object[] paramValues() {
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
        dbSource.withConnection(conn -> {
            if (ArrayUtils.isEmpty(paramValues)) {
                QuietStatement stmt = conn.createStatement();
                return stmt.executeQuery(sql);
            }
            else {
                QuietPreparedStatement pstmt = conn.prepareStatement(sql);
                int i = 1;
                for (Object o : paramValues) {
                    ValueType.get(o.getClass()).setToPstmt(pstmt, i++, o);
                }
                return pstmt.executeQuery();
            }
        });
    };

}
