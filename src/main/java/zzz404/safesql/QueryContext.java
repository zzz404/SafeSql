package zzz404.safesql;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class QueryContext {

    public static final ThreadLocal<QueryContext> INSTANCE = new ThreadLocal<>();

    private Queue<String> columnNames = new LinkedList<>();
    ArrayList<Condition> conditions = new ArrayList<>();
    List<OrderBy> orderBys = new ArrayList<>();

    public void addColumnName(String columnName) {
        columnNames.offer(columnName);
    }

    public String takeColumnName() {
        return columnNames.poll();
    }

    public List<String> takeAllColumnNames() {
        ArrayList<String> result = new ArrayList<>(columnNames);
        columnNames.clear();
        return result;
    }

    public void replaceLastCondition(Condition cond) {
        conditions.set(conditions.size() - 1, cond);
    }

}
