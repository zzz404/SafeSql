package zzz404.safesql;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import zzz404.safesql.sql.QuietConnection;
import zzz404.safesql.sql.QuietResultSet;
import zzz404.safesql.sql.QuietResultSetMetaData;

public class QueryContext {

    private static final ThreadLocal<QueryContext> container = new ThreadLocal<>();

    private String name;

    Scope scope;
    private Queue<TableColumn> tableColumns = new LinkedList<>();
    ArrayList<Condition> conditions = new ArrayList<>();
    List<OrderBy> orderBys = new ArrayList<>();

    private ConnectionFactoryImpl connFactory;
    QuietResultSet rs;
    Set<String> columnNames_of_resultSet = null;

    public Set<String> getColumnNames(QuietResultSet rs) {
        if (rs == this.rs && columnNames_of_resultSet != null) {
            return columnNames_of_resultSet;
        }
        this.rs = rs;
        columnNames_of_resultSet = new HashSet<>();
        QuietResultSetMetaData metaData = rs.getMetaData();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            columnNames_of_resultSet.add(metaData.getColumnName(i));
        }
        return columnNames_of_resultSet;
    }

    public QueryContext(String name) {
        this.name = name;
        this.connFactory = ConnectionFactory.get(name);
    }

    public QuietConnection getQuietConnection() {
        return connFactory.getQuietConnection();
    }

    public void closeConnection(QuietConnection conn) {
        if (connFactory.willCloseConnAfterQuery.get()) {
            conn.close();
        }
    }

    public void addTableColumn(int tableIndex, String columnName) {
        tableColumns.offer(new TableColumn(tableIndex, columnName));
    }

    public TableColumn takeTableColumn() {
        return tableColumns.poll();
    }

    public List<TableColumn> takeAllTableColumns() {
        ArrayList<TableColumn> result = new ArrayList<>(new LinkedHashSet<>(tableColumns));
        tableColumns.clear();
        return result;
    }

    public boolean hasMoreColumn() {
        return !tableColumns.isEmpty();
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
        if (ctx == null) {
            throw new NoQueryContextException();
        }
        return ctx;
    }

    public static void clear() {
        container.set(null);
    }
}
