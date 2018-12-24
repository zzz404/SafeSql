package zzz404.safesql.querier;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import zzz404.safesql.AbstractCondition;
import zzz404.safesql.Entity;
import zzz404.safesql.QueryContext;
import zzz404.safesql.Scope;
import zzz404.safesql.reflection.OneObjectPlayer;
import zzz404.safesql.sql.DbSourceImpl;
import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietStatement;
import zzz404.safesql.type.ValueType;
import zzz404.safesql.util.NoisyRunnable;

public class SqlDeleter<T> {

    protected DbSourceImpl dbSource;
    protected Entity<T> entity;
    protected List<AbstractCondition> conditions;

    public SqlDeleter(DbSourceImpl dbSource, Class<T> clazz) {
        this.dbSource = dbSource;
        this.entity = new Entity<>(0, clazz);
    }

    public SqlDeleter<T> where(OneObjectPlayer<T> conditionsCollector) {
        QueryContext.underQueryContext(ctx -> {
            ctx.setScope(Scope.where);
            NoisyRunnable.runQuietly(() -> ((NoisyRunnable) () -> {
                conditionsCollector.play(entity.getMockedObject());
            }).run());
            this.conditions = ctx.getConditions();
        });
        return this;
    }

    protected String sql() {
        dbSource.revise(entity);
        String tableName = dbSource.getRealTableName(entity.getVirtualTableName());
        String sql = "DELETE FROM " + tableName;
        if (!this.conditions.isEmpty()) {
            sql += " WHERE "
                    + this.conditions.stream().map(AbstractCondition::toClause).collect(Collectors.joining(" AND "));
        }
        return sql;
    }

    protected List<Object> paramValues() {
        ArrayList<Object> paramValues = new ArrayList<>();
        conditions.forEach(cond -> {
            cond.appendValuesTo(paramValues);
        });
        return paramValues;
    }

    public int execute() {
        return dbSource.withConnection(conn -> {
            List<Object> paramValues = paramValues();
            if (paramValues.isEmpty()) {
                QuietStatement stmt = conn.createStatement();
                return stmt.executeUpdate(sql());
            }
            else {
                QuietPreparedStatement pstmt = conn.prepareStatement(sql());
                int i = 1;
                for (Object paramValue : paramValues()) {
                    ValueType.setValueToPstmt(pstmt, i++, paramValue);
                }
                return pstmt.executeUpdate();
            }
        });
    }

}
