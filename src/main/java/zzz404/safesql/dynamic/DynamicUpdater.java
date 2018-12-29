package zzz404.safesql.dynamic;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import zzz404.safesql.AbstractCondition;
import zzz404.safesql.Field;
import zzz404.safesql.QueryContext;
import zzz404.safesql.reflection.OneObjectPlayer;
import zzz404.safesql.sql.DbSourceImpl;
import zzz404.safesql.sql.OrMapper;
import zzz404.safesql.sql.type.TypedValue;
import zzz404.safesql.util.NoisyRunnable;

public class DynamicUpdater<T> extends DynamicDeleter<T> {

    private T o;
    private List<Field<?>> fields = Collections.emptyList();

    private OrMapper<T> orMapper;

    private Collection<String> realColumnNames;

    @SuppressWarnings("unchecked")
    public DynamicUpdater(DbSourceImpl dbSource, T o) {
        super(dbSource, (Class<T>) o.getClass());
        this.o = o;
        this.orMapper = OrMapper.get((Class<T>) o.getClass(), dbSource);
    }

    public DynamicUpdater<T> set(OneObjectPlayer<T> columnsCollector) {
        QueryContext.underQueryContext(ctx -> {
            NoisyRunnable.runQuietly(() -> columnsCollector.play(entity.getMockedObject()));
            this.fields = ctx.takeAllTableFieldsUniquely();
        });
        return this;
    }

    @Override
    protected String sql() {
        dbSource.revise(entity);
        if (fields.isEmpty()) {
            this.realColumnNames = orMapper.get_realColumnName_of_all_getters();
        }
        else {
            Set<String> columns = fields.stream().map(f -> f.realColumnName).collect(Collectors.toSet());
            columns.retainAll(orMapper.get_realColumnName_of_all_getters());
            this.realColumnNames = columns;
        }
        String tableName = orMapper.getRealTableName();
        String sql = "UPDATE " + tableName + " SET "
                + realColumnNames.stream().map(columnName -> columnName + "=?").collect(Collectors.joining(", "));
        if (!this.conditions.isEmpty()) {
            sql += " WHERE "
                    + this.conditions.stream().map(AbstractCondition::toClause).collect(Collectors.joining(" AND "));
        }
        return sql;
    }

    protected List<TypedValue<?>> paramValues() {
        List<TypedValue<?>> paramValues = realColumnNames.stream()
                .map(realColumnName -> orMapper.getValue(o, realColumnName)).collect(Collectors.toList());
        paramValues.addAll(super.paramValues());
        return paramValues;
    }

}
