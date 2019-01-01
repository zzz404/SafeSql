package zzz404.safesql.dynamic;

import java.util.List;
import java.util.stream.Collectors;

import zzz404.safesql.AbstractCondition;
import zzz404.safesql.Entity;
import zzz404.safesql.reflection.OneObjectPlayer;
import zzz404.safesql.sql.DbSourceImpl;

public class DynamicDeleter<T> extends DynamicExecuter<T> {
    protected DbSourceImpl dbSource;
    protected Entity<T> entity;
    protected List<AbstractCondition> conditions;

    public DynamicDeleter(DbSourceImpl dbSource, Class<T> clazz) {
        super(dbSource, clazz);
    }

    public DynamicDeleter<T> where(OneObjectPlayer<T> conditionsCollector) {
        super.where(conditionsCollector);
        return this;
    }

    protected String sql() {
        dbSource.revise(entity);
        String sql = "DELETE FROM " + tableName;
        if (!this.conditions.isEmpty()) {
            sql += " WHERE "
                    + this.conditions.stream().map(AbstractCondition::toClause).collect(Collectors.joining(" AND "));
        }
        return sql;
    }

}
