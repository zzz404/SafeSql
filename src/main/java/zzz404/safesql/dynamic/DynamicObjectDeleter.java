package zzz404.safesql.dynamic;

import java.util.stream.Collectors;

import zzz404.safesql.sql.DbSourceImpl;

public class DynamicObjectDeleter extends DynamicObjectExecuter {

    public DynamicObjectDeleter(Object entity, DbSourceImpl dbSource) {
        super(entity, dbSource);
    }

    @Override
    protected String sql() {
        return "DELETE FROM " + tableSchema.getTableName() + " WHERE "
                + getPkGetters().stream().map(ma -> ma.getPropertyName() + "=?").collect(Collectors.joining(" AND "));
    }

    @Override
    protected Object[] paramValues() {
        return getPkGetters().stream().map(ma -> ma.getValue(entity)).toArray();
    }

}
