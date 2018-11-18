package zzz404.safesql;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import zzz404.safesql.sql.QuietConnection;

public class QueryContext {

    private static final ThreadLocal<QueryContext> container = new ThreadLocal<>();

    private String name;
    private ConnectionFactoryImpl connFactory;

    private Queue<TableColumn> tableColumns = new LinkedList<>();

    ArrayList<Condition> conditions = new ArrayList<>();
    List<OrderBy> orderBys = new ArrayList<>();

    public QueryContext(String name) {
        this.name = name;
        this.connFactory = ConnectionFactory.get(name);
    }

    public QuietConnection getQuietConnection() {
        return connFactory.getQuietConnection();
    }

    public void addTableColumn(int tableIndex, String columnName) {
        tableColumns.offer(new TableColumn(tableIndex, columnName));
    }

    public TableColumn takeTableColumn() {
        return tableColumns.poll();
    }

    public List<TableColumn> takeAllTableColumns() {
        ArrayList<TableColumn> result = new ArrayList<>(tableColumns);
        tableColumns.clear();
        return result;
    }

    public void replaceLastCondition(Condition cond) {
        conditions.set(conditions.size() - 1, cond);
    }

    public String getName() {
        return name;
    }

    public static QueryContext create(String name) {
        QueryContext ctx = new QueryContext(name);
        container.set(ctx);
        return ctx;
    }

    public static QueryContext get() {
        QueryContext ctx = container.get();
        if(ctx==null) {
            throw new NoQueryContextException();
        }
        return ctx;
    }

    public static void clear() {
        container.set(null);
    }
}
