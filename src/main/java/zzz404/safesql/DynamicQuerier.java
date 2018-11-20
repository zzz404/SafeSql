package zzz404.safesql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.sf.cglib.proxy.Enhancer;
import zzz404.safesql.util.CommonUtils;

public abstract class DynamicQuerier extends SqlQuerier {

    protected List<TableColumn> tableColumns = Collections.emptyList();
    protected List<Condition> conditions = Collections.emptyList();
    protected List<TableColumn> groupBys = Collections.emptyList();
    protected List<OrderBy> orderBys = Collections.emptyList();

    public DynamicQuerier() {
        this.tableColumns = Arrays.asList(new TableColumn(0, "*"));
    }

    protected <T> T createMockedObject(Class<T> clazz, int tableIndex) {
        Enhancer en = new Enhancer();
        en.setSuperclass(clazz);
        GetterTracer<T> getterLogger = new GetterTracer<>(clazz, tableIndex);
        en.setCallback(getterLogger);

        @SuppressWarnings("unchecked")
        T mockedObject = (T) en.create();
        return mockedObject;
    }

    protected void onSelectScope(Runnable collectColumns) {
        QueryContext ctx = QueryContext.get();
        ctx.scope = Scope.select;

        collectColumns.run();

        this.tableColumns = ctx.takeAllTableColumns();
        ctx.scope = null;
    }
    
    protected void onWhereScope(Runnable collectConditions) {
        QueryContext ctx = QueryContext.get();
        ctx.scope = Scope.where;

        collectConditions.run();

        this.conditions = QueryContext.get().conditions;
        ctx.scope = null;
    }
    
    protected void onGroupByScope(Runnable collectColumns) {
        QueryContext ctx = QueryContext.get();
        ctx.scope = Scope.groupBy;

        collectColumns.run();

        this.groupBys = ctx.takeAllTableColumns();
        ctx.scope = null;
    }
    
    protected void onOrderByScope(Runnable collectColumns) {
        QueryContext ctx = QueryContext.get();
        ctx.scope = Scope.orderBy;

        collectColumns.run();

        this.orderBys = QueryContext.get().orderBys;
        ctx.scope = null;
    }
    
    protected abstract String getTablesClause();

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
        return this.conditions.stream().map(Condition::toClause).collect(Collectors.joining(" AND "));
    }

    String getColumnsClause() {
        return CommonUtils.join(tableColumns, ", ", TableColumn::getPrefixedColumnName);
    }

    public String sql_for_queryCount() {
        String tableName = getTablesClause();
        String sql = "SELECT COUNT(*) FROM " + tableName;
        if (!this.conditions.isEmpty()) {
            sql += " WHERE " + this.conditions.stream().map(Condition::toClause).collect(Collectors.joining(" AND "));
        }
        return sql;
    }

    @Override
    protected Object[] paramValues() {
        ArrayList<Object> paramValues = new ArrayList<>();
        conditions.forEach(cond->{
            cond.appendValuesTo(paramValues);
        });
        return paramValues.toArray();
    }

    public abstract Object queryOne();

    public abstract List<?> queryList();

    public abstract Page<?> queryPage();

}
