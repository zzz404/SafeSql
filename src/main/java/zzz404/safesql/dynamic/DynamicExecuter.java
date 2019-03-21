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

    protected String tableName;
    protected List<FieldImpl> fields;
    protected List<AbstractCondition> conditions = null;

    public DynamicExecuter(Class<T> clazz, DbSourceImpl dbSource) {
        this.dbSource = dbSource;
        this.entity = new Entity<>(0, clazz);
        this.tableName = dbSource.getSchema(entity.getName()).getTableName();
    }

    protected void collectFields(OneObjectPlayer<T> columnsCollector) {
        QueryContext.underQueryContext(ctx -> {
            NoisyRunnable.runQuietly(() -> columnsCollector.play(entity.getMockedObject_for_traceSetter()));
            fields = ctx.takeAllTableFieldsUniquely();
            fields.forEach(FieldImpl::checkType);
        });
    }

    protected DynamicExecuter<T> where(OneObjectPlayer<T> conditionsCollector) {
        QueryContext.underQueryContext(ctx -> {
            ctx.setScope(Scope.where);
            NoisyRunnable.runQuietly(() -> ((NoisyRunnable) () -> {
                conditionsCollector.play(entity.getMockedObject_for_traceGetter());
            }).run());
            this.conditions = ctx.getConditions();
        });
        return this;
    }

    protected abstract String sql();

    protected List<TypedValue<?>> paramValues() {
        List<TypedValue<?>> paramValues = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(fields)) {
            fields.forEach(field -> {
                Object o = field.getValue();
                TypedValue<?> value = (o != null) ? TypedValue.valueOf(o) : TypedValue.valueOf(field.getValueClass());
                paramValues.add(value);
            });
        }
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
