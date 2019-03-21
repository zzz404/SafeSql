package zzz404.safesql.dynamic;

import java.util.Collection;
import java.util.stream.Collectors;

import zzz404.safesql.reflection.MethodAnalyzer;
import zzz404.safesql.reflection.ObjectSchema;
import zzz404.safesql.reflection.ObjectSchemeException;
import zzz404.safesql.sql.DbSourceImpl;
import zzz404.safesql.sql.StaticSqlExecuter;
import zzz404.safesql.sql.TableSchema;

abstract class DynamicObjectExecuter {
    protected Object entity;
    protected DbSourceImpl dbSource;

    protected TableSchema tableSchema;
    private Collection<MethodAnalyzer> validGetters;
    private Collection<MethodAnalyzer> pkGetters = null;
    private Collection<MethodAnalyzer> dataGetters = null;

    public DynamicObjectExecuter(Object entity, DbSourceImpl dbSource) {
        this.entity = entity;
        this.dbSource = dbSource;

        this.tableSchema = dbSource.getSchema(entity.getClass().getSimpleName());
        this.validGetters = ObjectSchema.get(entity.getClass()).findAllValidGetters().stream()
                .filter(ma -> tableSchema.hasMatchedColumn(ma.getPropertyName())).collect(Collectors.toList());
    }

    public final void execute() {
        new StaticSqlExecuter(dbSource).sql(sql()).paramValues(paramValues()).update();
    }

    protected abstract String sql();

    protected abstract Object[] paramValues();

    protected Collection<MethodAnalyzer> getValidGetters() {
        return validGetters;
    }

    protected Collection<MethodAnalyzer> getPkGetters() {
        if (pkGetters == null) {
            pkGetters = validGetters.stream().filter(ma -> ma.isPrimaryKey()).collect(Collectors.toList());
        }
        if (pkGetters.isEmpty()) {
            throw new ObjectSchemeException("Primary key columns not found.");
        }
        return validGetters;
    }

    protected Collection<MethodAnalyzer> getDataGetters() {
        if (dataGetters == null) {
            dataGetters = validGetters.stream().filter(ma -> !ma.isPrimaryKey()).collect(Collectors.toList());
        }
        if (dataGetters.isEmpty()) {
            throw new ObjectSchemeException("Primary key columns not found.");
        }
        return validGetters;
    }

}
