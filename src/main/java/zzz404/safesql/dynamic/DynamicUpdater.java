package zzz404.safesql.dynamic;

import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import zzz404.safesql.AbstractCondition;
import zzz404.safesql.reflection.OneObjectPlayer;
import zzz404.safesql.sql.DbSourceImpl;

public class DynamicUpdater<T> extends DynamicObjectExecuter<T> {

    public DynamicUpdater(DbSourceImpl dbSource, T o) {
        super(dbSource, o);
    }

    public DynamicUpdater<T> set(OneObjectPlayer<T> columnsCollector) {
        super.set(columnsCollector);
        return this;
    }

    public DynamicUpdater<T> where(OneObjectPlayer<T> conditionsCollector) {
        super.where(conditionsCollector);
        return this;
    }

    @Override
    protected String sql() {
        String sql = "UPDATE " + tableName + " SET "
                + fields.stream().map(field -> field.getColumnName() + "=?").collect(Collectors.joining(", "));
        if (CollectionUtils.isNotEmpty(conditions)) {
            sql += " WHERE "
                    + this.conditions.stream().map(AbstractCondition::toClause).collect(Collectors.joining(" AND "));
        }
        return sql;
    }

}
