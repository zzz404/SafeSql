package zzz404.safesql.dynamic;

import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;

import zzz404.safesql.reflection.OneObjectPlayer;
import zzz404.safesql.sql.DbSourceImpl;

public class DynamicUpdater<T> extends DynamicExecuter<T> {

    public DynamicUpdater(DbSourceImpl dbSource, Class<T> clazz) {
        super(dbSource, clazz);
    }

    public DynamicUpdater<T> set(OneObjectPlayer<T> columnsCollector) {
        super.collectFields(columnsCollector);
        return this;
    }

    public DynamicUpdater<T> where(OneObjectPlayer<T> conditionsCollector) {
        super.where(conditionsCollector);
        return this;
    }

    @Override
    protected String sql() {
        Validate.isTrue(CollectionUtils.isNotEmpty(fields));
        String sql = "UPDATE " + tableName + " SET "
                + fields.stream().map(field -> field.getColumnName() + "=?").collect(Collectors.joining(", "));
        if (CollectionUtils.isNotEmpty(conditions)) {
            sql += " WHERE "
                    + this.conditions.stream().map(AbstractCondition::toClause).collect(Collectors.joining(" AND "));
        }
        return sql;
    }

}
