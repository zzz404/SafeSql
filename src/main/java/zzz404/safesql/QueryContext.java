package zzz404.safesql;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

import org.apache.commons.lang3.Validate;

public class QueryContext {

    private static final ThreadLocal<QueryContext> container = new ThreadLocal<>();

    private Scope scope;
    private Queue<TableColumn> tableColumns = null;
    private List<Condition> conditions = null;
    private List<OrderBy> orderBys = null;

    private QueryContext() {
    }

    public static void underQueryContext(Consumer<QueryContext> consumer) {
        try {
            QueryContext ctx = new QueryContext();
            container.set(ctx);
            consumer.accept(ctx);
        }
        finally {
            container.remove();
        }
    }

    public static QueryContext get() {
        QueryContext ctx = container.get();
        Validate.notNull(ctx);
        return ctx;
    }

    public void addTableColumn(int tableIndex, String columnName) {
        if (tableColumns == null) {
            tableColumns = new LinkedList<>();
        }
        tableColumns.offer(new TableColumn(tableIndex, columnName));
    }

    public TableColumn takeTableColumn() {
        Validate.notNull(tableColumns);
        return tableColumns.poll();
    }

    public List<TableColumn> takeAllTableColumnsUniquely() {
        ArrayList<TableColumn> result = new ArrayList<>(new LinkedHashSet<>(tableColumns));
        tableColumns.clear();
        return result;
    }

    public boolean hasMoreColumn() {
        return !tableColumns.isEmpty();
    }

    public void addCondition(Condition cond) {
        if (conditions == null) {
            conditions = new ArrayList<>();
        }
        conditions.add(cond);
    }

    public void replaceLastCondition(Condition cond) {
        conditions.set(conditions.size() - 1, cond);
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public void addOrderBy(String prefixedColumnName, boolean isAsc) {
        if (orderBys == null) {
            orderBys = new ArrayList<>();
        }
        OrderBy orderBy = new OrderBy(prefixedColumnName, isAsc);
        orderBys.add(orderBy);
    }

    public List<OrderBy> getOrderBys() {
        return orderBys;
    }

}
