package zzz404.safesql;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.sf.cglib.proxy.Enhancer;
import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.util.CommonUtils;

public abstract class DynamicQuerier extends SqlQuerier {

    protected List<TableColumn> tableColumns = Collections.emptyList();
    protected List<Condition> conditions = Collections.emptyList();
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

    @Override
    protected void setCondValueToPstmt(QuietPreparedStatement pstmt) {
        int i = 1;
        for (Condition cond : conditions) {
            i = cond.setValueToPstmt_and_returnNextIndex(i, pstmt);
        }
    }

    protected abstract String getTablesClause();

    public String buildSql() {
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

    public String buildSql_for_queryCount() {
        String tableName = getTablesClause();
        String sql = "SELECT COUNT(*) FROM " + tableName;
        if (!this.conditions.isEmpty()) {
            sql += " WHERE " + this.conditions.stream().map(Condition::toClause).collect(Collectors.joining(" AND "));
        }
        return sql;
    }

    public abstract Object queryOne();

    public abstract List<?> queryList();

    public abstract Page<?> queryPage();

}
