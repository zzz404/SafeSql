package zzz404.safesql.sql;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import zzz404.safesql.sql.proxy.EnhancedConnection;
import zzz404.safesql.sql.proxy.QuietResultSet;
import zzz404.safesql.sql.proxy.QuietStatement;
import zzz404.safesql.util.CommonUtils;

public class TableSchema {
    private String entityName;
    private String tablePrefix;
    private DbSourceImpl dbSource;

    private String tableName;
    private String lcTableName;
    private String lcColumnNameOfAutoIncrement;
    private Set<String> lcColumnNames;
    private Map<String, String> prop_column_map = new HashMap<>();

    protected TableSchema(String entityName, String tablePrefix, DbSourceImpl dbSource) {
        this.entityName = entityName;
        this.tablePrefix = tablePrefix;
        this.dbSource = dbSource;
        init();
    }

    protected void init() {
        dbSource.withConnection(conn -> {
            ResultSet rs = loadTableName(conn);
            loadColumns(new QuietResultSet(rs));
            return null;
        });
    }

    private ResultSet loadTableName(EnhancedConnection conn) {
        setTableName(tablePrefix + entityName);
        QuietStatement stmt = conn.createStatement();
        try {
            return stmt.executeQuery("SELECT * FROM " + tableName);
        }
        catch (RuntimeException e) {
            if (!dbSource.isSnakeFormCompatable()) {
                throw new TableNotFoundException(e, tableName);
            }
            String tb1 = tableName;
            setTableName(tablePrefix + CommonUtils.camelForm_to_snakeForm(entityName));
            try {
                return stmt.executeQuery("SELECT * FROM " + tableName);
            }
            catch (RuntimeException e1) {
                throw new TableNotFoundException(e1, tb1, tableName);
            }
        }
    }

    private void setTableName(String tableName) {
        this.tableName = tableName;
        this.lcTableName = tableName.toLowerCase();
    }

    private void loadColumns(QuietResultSet rs) {
        ResultSetAnalyzer rsAnalyzer = new ResultSetAnalyzer(rs);
        this.lcColumnNameOfAutoIncrement = rsAnalyzer.getLcColumnOfAutoIncrement();
        this.lcColumnNames = rsAnalyzer.getAllColumns().stream().map(c -> c.lcColumn).collect(Collectors.toSet());
    }

    public String getColumnName(String propName) {
        if (!hasMatchedColumn(propName)) {
            throw new ColumnNotFoundException(propName, tableName);
        }
        return prop_column_map.get(propName);
    }

    public boolean hasMatchedColumn(String propName) {
        if (!prop_column_map.containsKey(propName)) {
            prop_column_map.put(propName, findColumnName(propName));
        }
        return prop_column_map.get(propName) != null;
    }

    private String findColumnName(String propName) {
        if (lcColumnNames.contains(propName.toLowerCase())) {
            return propName;
        }
        if (dbSource.isSnakeFormCompatable()) {
            String snakeColumnName = CommonUtils.camelForm_to_snakeForm(propName);
            if (lcColumnNames.contains(snakeColumnName)) {
                return snakeColumnName;
            }
        }
        return null;
    }

    public String getTableName() {
        return tableName;
    }

    public String getLcTableName() {
        return lcTableName;
    }

    public String getColumnName_of_autoIncrement() {
        return lcColumnNameOfAutoIncrement;
    }

    static TableSchema createSchema(String entityName, DbSourceImpl dbSource) {
        String tablePrefix = dbSource.isUseTablePrefix() ? dbSource.getName() : "";
        return new TableSchema(entityName, tablePrefix, dbSource);
    }

}
