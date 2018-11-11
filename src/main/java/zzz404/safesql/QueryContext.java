package zzz404.safesql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class QueryContext {

    static ThreadLocal<QueryContext> instance = new ThreadLocal<>();

    private ArrayList<Condition> conditions = new ArrayList<>();
    private List<OrderBy> orderBys = new ArrayList<>();

    public void addCondition(Condition cond) {
        conditions.add(cond);
    }

    public List<Condition> buildConditions(List<String> fields) {
        Iterator<String> field_iter = fields.iterator();

        for (Condition cond : conditions) {
            cond.fillField(field_iter);
        }
        return conditions;
    }

    public void replaceLastCondition(Condition cond) {
        conditions.set(conditions.size() - 1, cond);
    }

    public void addOrderBy(OrderBy orderBy) {
        orderBys.add(orderBy);
    }

    public List<OrderBy> buildOrderBys(List<String> fields) {
        Iterator<String> field_iter = fields.iterator();

        for (OrderBy orderBy : orderBys) {
            orderBy.field = field_iter.next();
        }
        return orderBys;
    }

}
