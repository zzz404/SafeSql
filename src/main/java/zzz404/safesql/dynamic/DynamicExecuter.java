package zzz404.safesql.dynamic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import zzz404.safesql.reflection.OneObjectPlayer;
import zzz404.safesql.sql.DbSourceImpl;
import zzz404.safesql.sql.type.TypedValue;
import zzz404.safesql.util.NoisyRunnable;

public abstract class DynamicExecuter<T> {

    protected DbSourceImpl dbSource;
    protected Entity<T> entity;
    protected List<AbstractCondition> conditions = null;
    protected String tableName;

    public DynamicExecuter(DbSourceImpl dbSource, Class<T> clazz) {
        this.dbSource = dbSource;
        this.entity = new Entity<>(0, clazz);
        this.tableName = dbSource.getSchema(entity.getName()).getTableName();
    }

    protected DynamicExecuter<T> where(OneObjectPlayer<T> conditionsCollector) {
        QueryContext.underQueryContext(ctx -> {
            ctx.setScope(Scope.where);
            NoisyRunnable.runQuietly(() -> ((NoisyRunnable) () -> {
                conditionsCollector.play(entity.getMockedObject());
            }).run());
            this.conditions = ctx.getConditions();
        });
        return this;
    }

    protected abstract String sql();

    protected List<TypedValue<?>> paramValues() {
        List<TypedValue<?>> paramValues = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(conditions)) {
            conditions.forEach(cond -> {
                cond.appendValuesTo(paramValues);
            });
        }
        return paramValues;
    }

    public int execute() {
        dbSource.revise(entity);
        return dbSource.update(sql(), paramValues());
    }

}
