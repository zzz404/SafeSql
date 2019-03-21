package zzz404.safesql.dynamic;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import zzz404.safesql.reflection.MethodAnalyzer;
import zzz404.safesql.sql.DbSourceImpl;

public class DynamicObjectInserter extends DynamicObjectExecuter {

    private boolean assignPkValue;

    public DynamicObjectInserter(Object entity, DbSourceImpl dbSource, boolean assignPkValue) {
        super(entity, dbSource);
        this.assignPkValue = assignPkValue;
    }

    @Override
    protected String sql() {
        return "INSERT INTO " + tableSchema.getTableName() + " SET "
                + getDataGetters().stream().map(ma -> ma.getPropertyName() + "=?").collect(Collectors.joining(", "))
                + " VALUES (" + StringUtils.join(Collections.nCopies(getDataGetters().size(), '?'), ", ");
    }

    @Override
    protected Object[] paramValues() {
        return getDataGetters().stream().map(ma -> ma.getValue(entity)).toArray();
    }

    @Override
    protected Collection<MethodAnalyzer> getDataGetters() {
        return assignPkValue ? super.getValidGetters() : super.getDataGetters();
    }

}
