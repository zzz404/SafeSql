package zzz404.safesql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;

import org.apache.commons.lang3.Validate;

public class QueryContext {

    private static final ThreadLocal<QueryContext> container = new ThreadLocal<>();

    private Scope scope;
    private Queue<Field> fields = null;
    private List<AbstractCondition> conditions = null;
    private List<OrderBy> orderBys = null;

    private Map<String, String> columnMap = null;

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

    public void addTableField(Field field) {
        if (fields == null) {
            fields = new LinkedList<>();
        }
        fields.offer(field);
    }

    public Field takeTableField() {
        Validate.notNull(fields);
        return fields.poll();
    }

    public List<Field> takeAllTableFieldsUniquely() {
        if (fields == null) {
            return Collections.emptyList();
        }
        ArrayList<Field> result = new ArrayList<>(new LinkedHashSet<>(fields));
        fields.clear();
        return result;
    }

    public void addColumnMapping(String fromColumn, String toColumn) {
        if (columnMap == null) {
            columnMap = new HashMap<>();
        }
        columnMap.put(fromColumn, toColumn);
    }

    public boolean hasMoreColumn() {
        return !fields.isEmpty();
    }

    public void addCondition(AbstractCondition cond) {
        if (conditions == null) {
            conditions = new ArrayList<>();
        }
        conditions.add(cond);
    }

    public void replaceLastCondition(AbstractCondition cond) {
        conditions.set(conditions.size() - 1, cond);
    }

    public List<AbstractCondition> getConditions() {
        return conditions;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public void addOrderBy(OrderBy orderBy) {
        if (orderBys == null) {
            orderBys = new ArrayList<>();
        }
        //OrderBy orderBy = new OrderBy(prefixedColumnName, isAsc);
        orderBys.add(orderBy);
    }

    public List<OrderBy> getOrderBys() {
        return orderBys;
    }

    public Map<String, String> getColumnMap() {
        return columnMap;
    }

}
