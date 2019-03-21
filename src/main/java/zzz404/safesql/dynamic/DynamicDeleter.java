package zzz404.safesql.dynamic;

import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import zzz404.safesql.reflection.OneObjectPlayer;
import zzz404.safesql.sql.DbSourceImpl;

public class DynamicDeleter<T> extends DynamicExecuter<T> {

    public DynamicDeleter(Class<T> clazz, DbSourceImpl dbSource) {
        super(clazz, dbSource);
    }

    public DynamicDeleter<T> where(OneObjectPlayer<T> conditionsCollector) {
        super.where(conditionsCollector);
        return this;
    }

    protected String sql() {
        dbSource.revise(entity);
        String sql = "DELETE FROM " + tableName;
        if (CollectionUtils.isNotEmpty(conditions)) {
            sql += " WHERE "
                    + this.conditions.stream().map(AbstractCondition::toClause).collect(Collectors.joining(" AND "));
        }
        return sql;
    }

}
