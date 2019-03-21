package zzz404.safesql.dynamic;

import java.util.ArrayList;
import java.util.stream.Collectors;

import zzz404.safesql.sql.DbSourceImpl;

public class DynamicObjectUpdater extends DynamicObjectExecuter {

    public DynamicObjectUpdater(Object entity, DbSourceImpl dbSource) {
        super(entity, dbSource);
    }

    @Override
    protected String sql() {
        return "UPDATE " + tableSchema.getTableName() + " SET "
                + getDataGetters().stream().map(ma -> ma.getPropertyName() + "=?").collect(Collectors.joining(", "))
                + " WHERE "
                + getPkGetters().stream().map(ma -> ma.getPropertyName() + "=?").collect(Collectors.joining(" AND "));
    }

    @Override
    protected Object[] paramValues() {
        ArrayList<Object> paramValues = new ArrayList<>();
        getPkGetters().forEach(ma -> paramValues.add(ma.getValue(entity)));
        getDataGetters().forEach(ma -> paramValues.add(ma.getValue(entity)));
        return paramValues.toArray();
    }
}
