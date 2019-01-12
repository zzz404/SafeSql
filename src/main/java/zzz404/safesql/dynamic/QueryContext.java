package zzz404.safesql.dynamic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class QueryContext {

    private static final ThreadLocal<QueryContext> container = new ThreadLocal<>();

    private Scope scope;
    private LinkedList<FieldImpl<?>> fields = null;
    private LinkedList<AbstractCondition> conditions = null;
    private List<OrderBy> orderBys = null;

    Entity<?> resultEntity;

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
        Objects.requireNonNull(ctx);
        return ctx;
    }

    public void addTableField(FieldImpl<?> field) {
        if (fields == null) {
            fields = new LinkedList<>();
        }
        fields.offer(field);
    }

    public FieldImpl<?> takeField() {
        Objects.requireNonNull(fields);
        return fields.poll();
    }

    public FieldImpl takeLastField() {
        Objects.requireNonNull(fields);
        return fields.removeLast();
    }

    public List<FieldImpl<?>> takeAllTableFieldsUniquely() {
        if (fields == null) {
            return Collections.emptyList();
        }
        ArrayList<FieldImpl<?>> result = new ArrayList<>(new LinkedHashSet<>(fields));
        fields.clear();
        return result;
    }

    public FieldImpl<?> getLastField() {
        return fields.getLast();
    }

    public boolean hasMoreColumn() {
        return !fields.isEmpty();
    }

    public void addCondition(AbstractCondition cond) {
        if (conditions == null) {
            conditions = new LinkedList<>();
        }
        conditions.add(cond);
    }

    public void reaplaceLastCondition(OrCondition cond) {
        conditions.removeLast();
        conditions.add(cond);
    }

    public List<AbstractCondition> getConditions() {
        return conditions != null ? conditions : Collections.emptyList();
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
        orderBys.add(orderBy);
    }

    public List<OrderBy> getOrderBys() {
        return orderBys != null ? orderBys : Collections.emptyList();
    }

}
