package zzz404.safesql.dynamic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;

import zzz404.safesql.Page;
import zzz404.safesql.sql.DbSourceImpl;
import zzz404.safesql.sql.OrMapper;
import zzz404.safesql.sql.SqlQuerier;
import zzz404.safesql.sql.proxy.QuietResultSet;
import zzz404.safesql.sql.type.TypedValue;
import zzz404.safesql.util.CommonUtils;
import zzz404.safesql.util.NoisyRunnable;

public abstract class DynamicQuerier extends SqlQuerier {

    protected List<Entity<?>> entities = new ArrayList<>();
    protected List<FieldImpl> fields = Collections.emptyList();
    protected List<AbstractCondition> conditions = Collections.emptyList();
    protected List<FieldImpl> groupBys = Collections.emptyList();
    protected List<OrderBy> orderBys = Collections.emptyList();

    private Scope currentScope = null;

    public DynamicQuerier(DbSourceImpl dbSource) {
        super(dbSource);
    }

    public abstract <R> BindResultQuerier<R> to(Class<R> clazz);

    protected void onSelectScope(NoisyRunnable collectColumns) {
        checkScope(Scope.select);
        QueryContext.underQueryContext(ctx -> {
            ctx.setScope(Scope.select);

            NoisyRunnable.runQuietly(() -> collectColumns.run());

            fields = ctx.takeAllTableFieldsUniquely();
            fields.forEach(FieldImpl::checkType);
        });
    }

    private void checkScope(Scope scope) {
        Scope previousScope = this.currentScope;
        Validate.isTrue(previousScope == null || scope.ordinal() >= previousScope.ordinal());
        currentScope = scope;
    }

    protected void onWhereScope(NoisyRunnable collectConditions) {
        checkScope(Scope.where);
        QueryContext.underQueryContext(ctx -> {
            ctx.setScope(Scope.where);

            NoisyRunnable.runQuietly(() -> collectConditions.run());

            this.conditions = ctx.getConditions();
        });
    }

    protected void onGroupByScope(NoisyRunnable collectColumns) {
        checkScope(Scope.groupBy);
        QueryContext.underQueryContext(ctx -> {
            ctx.setScope(Scope.groupBy);

            NoisyRunnable.runQuietly(() -> collectColumns.run());

            this.groupBys = ctx.takeAllTableFieldsUniquely();
        });
    }

    protected void onOrderByScope(NoisyRunnable collectColumns) {
        checkScope(Scope.orderBy);
        QueryContext.underQueryContext(ctx -> {
            ctx.setScope(Scope.orderBy);

            NoisyRunnable.runQuietly(() -> collectColumns.run());

            this.orderBys = ctx.getOrderBys();
        });
    }

    String getTablesClause() {
        return entities.stream().map(entity -> {
            String realTableName = dbSource.getSchema(entity.getName()).getTableName();
            return realTableName + " t" + entity.getIndex();
        }).collect(Collectors.joining(", "));
    }

    String getColumnsClause() {
        if (fields.isEmpty()) {
            return "*";
        }
        return CommonUtils.join(fields, ", ", FieldImpl::getColumnClause);
    }

    String getWhereClause() {
        return this.conditions.stream().map(AbstractCondition::toClause).collect(Collectors.joining(" AND "));
    }

    String getGroupByClause() {
        return this.groupBys.stream().map(FieldImpl::getPrefixedColumnName).collect(Collectors.joining(", "));
    }

    String getOrderByClause() {
        return this.orderBys.stream().map(OrderBy::toClause).collect(Collectors.joining(", "));
    }

    public String sql() {
        dbSource.revise(entities.toArray(new Entity[entities.size()]));
        String tableName = getTablesClause();
        String sql = "SELECT " + getColumnsClause() + " FROM " + tableName;
        if (!this.conditions.isEmpty()) {
            sql += " WHERE " + getWhereClause();
        }
        if (!this.groupBys.isEmpty()) {
            sql += " GROUP BY " + getGroupByClause();
        }
        if (!this.orderBys.isEmpty()) {
            sql += " ORDER BY " + getOrderByClause();
        }
        return sql;
    }

    protected String sql_for_queryCount() {
        String sql = "SELECT COUNT(*) FROM " + getTablesClause();
        if (!this.conditions.isEmpty()) {
            sql += " WHERE "
                    + this.conditions.stream().map(AbstractCondition::toClause).collect(Collectors.joining(" AND "));
        }
        if (!this.groupBys.isEmpty()) {
            sql += " GROUP BY " + getGroupByClause();
        }
        return sql;
    }

    @Override
    protected List<TypedValue<?>> paramValues() {
        ArrayList<TypedValue<?>> paramValues = new ArrayList<>();
        conditions.forEach(cond -> {
            cond.appendValuesTo(paramValues);
        });
        return paramValues;
    }

    public abstract Object queryOne();

    public abstract List<?> queryList();

    public abstract Page<?> queryPage();

    protected <E> E rsToObject(QuietResultSet rs, Entity<E> entity) {
        return new OrMapper(rs, dbSource).mapToObject(entity.getObjClass(), true);
    }

}
