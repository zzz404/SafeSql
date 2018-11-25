package zzz404.safesql.querier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;

import net.sf.cglib.proxy.Enhancer;
import zzz404.safesql.AbstractCondition;
import zzz404.safesql.Entity;
import zzz404.safesql.MutualCondition;
import zzz404.safesql.OrderBy;
import zzz404.safesql.Page;
import zzz404.safesql.QueryContext;
import zzz404.safesql.Scope;
import zzz404.safesql.TableField;
import zzz404.safesql.reflection.GetterTracer;
import zzz404.safesql.sql.ConnectionFactoryImpl;
import zzz404.safesql.sql.TableSchema;
import zzz404.safesql.util.CommonUtils;

public abstract class DynamicQuerier extends SqlQuerier {

    protected List<TableField> tableFields = Collections.emptyList();
    protected List<AbstractCondition> conditions = Collections.emptyList();
    protected List<TableField> groupBys = Collections.emptyList();
    protected List<OrderBy> orderBys = Collections.emptyList();

    private Scope currentScope = null;

    public DynamicQuerier(ConnectionFactoryImpl connFactory) {
        super(connFactory);
    }

    protected <T> T createMockedObject(Entity<T> entity) {
        Enhancer en = new Enhancer();
        en.setSuperclass(entity.getObjClass());
        GetterTracer<T> getterLogger = new GetterTracer<>(entity);
        en.setCallback(getterLogger);

        @SuppressWarnings("unchecked")
        T mockedObject = (T) en.create();
        return mockedObject;
    }

    protected void onSelectScope(Runnable collectColumns) {
        checkScope(Scope.select);
        QueryContext.underQueryContext(ctx -> {
            ctx.setScope(Scope.select);

            collectColumns.run();

            this.tableFields = ctx.takeAllTableColumnsUniquely();
        });
    }

    private void checkScope(Scope scope) {
        Scope previousScope = this.currentScope;
        Validate.isTrue(previousScope == null || scope.ordinal() >= previousScope.ordinal());
        currentScope = scope;
    }

    protected void onWhereScope(Runnable collectConditions) {
        checkScope(Scope.where);
        QueryContext.underQueryContext(ctx -> {
            ctx.setScope(Scope.where);

            collectConditions.run();

            this.conditions = ctx.getConditions();
        });
    }

    protected void onGroupByScope(Runnable collectColumns) {
        checkScope(Scope.groupBy);
        QueryContext.underQueryContext(ctx -> {
            ctx.setScope(Scope.groupBy);

            collectColumns.run();

            this.groupBys = ctx.takeAllTableColumnsUniquely();
        });
    }

    protected void onOrderByScope(Runnable collectColumns) {
        checkScope(Scope.orderBy);
        QueryContext.underQueryContext(ctx -> {
            ctx.setScope(Scope.orderBy);

            collectColumns.run();

            this.orderBys = ctx.getOrderBys();
        });
    }

    private String getTablesClause() {
        Set<Integer> allUsedTableIndexes = getAllUsedTableIndexes();
        return Arrays.stream(getEntites()).filter(entity -> allUsedTableIndexes.contains(entity.getIndex()))
                .map(entity -> connFactory.getSchema(entity.getVirtualTableName()).getRealTableName() + " t"
                        + entity.getIndex())
                .collect(Collectors.joining(", "));
    }

    private Set<Integer> getAllUsedTableIndexes() {
        HashSet<Integer> tableIndexes = new HashSet<>();
        tableFields.stream().map(TableField::getEntityIndex).forEach(index -> tableIndexes.add(index));
        conditions.stream().forEach(cond -> {
            tableIndexes.add(cond.getTableColumn().getEntityIndex());
            if (cond instanceof MutualCondition) {
                tableIndexes.add(((MutualCondition) cond).getTableColumn2().getEntityIndex());
            }
        });
        return tableIndexes;
    }

    public String sql() {
        String tableName = getTablesClause();
        String sql = "SELECT " + getColumnsClause() + " FROM " + tableName;
        if (!this.conditions.isEmpty()) {
            sql += " WHERE " + getConditionsClause();
        }
        if (!this.orderBys.isEmpty()) {
            sql += " ORDER BY " + this.orderBys.stream().map(OrderBy::toClause).collect(Collectors.joining(", "));
        }
        return sql;
    }

    private String getConditionsClause() {
        return this.conditions.stream().map(AbstractCondition::toClause).collect(Collectors.joining(" AND "));
    }

    String getColumnsClause() {
        if (tableFields.isEmpty()) {
            return "*";
        }
        if (connFactory.isSnakeFormCompatable()) {
            Map<Entity<?>, List<TableField>> map = tableFields.stream()
                    .collect(Collectors.groupingBy(TableField::getEntity));
            map.forEach((entity, fields) -> {
                TableSchema schema = connFactory.getSchema(entity.getVirtualTableName());
                fields.forEach(schema::revise);
            });
        }
        return CommonUtils.join(tableFields, ", ", TableField::getPrefixedColumnName);
    }

    public String sql_for_queryCount() {
        String tableName = getTablesClause();
        String sql = "SELECT COUNT(*) FROM " + tableName;
        if (!this.conditions.isEmpty()) {
            sql += " WHERE "
                    + this.conditions.stream().map(AbstractCondition::toClause).collect(Collectors.joining(" AND "));
        }
        return sql;
    }

    @Override
    protected Object[] paramValues() {
        ArrayList<Object> paramValues = new ArrayList<>();
        conditions.forEach(cond -> {
            cond.appendValuesTo(paramValues);
        });
        return paramValues.toArray();
    }

    public abstract Object queryOne();

    public abstract List<?> queryList();

    public abstract Page<?> queryPage();

    protected abstract Entity<?>[] getEntites();

}
