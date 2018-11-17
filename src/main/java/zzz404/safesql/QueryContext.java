package zzz404.safesql;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import zzz404.safesql.sql.QuietConnection;

public class QueryContext {

    public static final ThreadLocal<QueryContext> INSTANCE = new ThreadLocal<>();

    private ConnectionFactoryImpl connFactory;

    private Queue<String> columnNames = new LinkedList<>();

    ArrayList<Condition> conditions = new ArrayList<>();
    List<OrderBy> orderBys = new ArrayList<>();

    public QueryContext(String name) {
        this.connFactory = ConnectionFactory.get(name);
    }

    public QuietConnection getQuietConnection() {
        return connFactory.getQuietConnection();
    }

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

    public void clear() {
        columnNames.clear();
        conditions.clear();
        orderBys.clear();
    }
}
