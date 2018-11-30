package zzz404.safesql.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import zzz404.safesql.TableField;
import zzz404.safesql.util.CommonUtils;

public class TableSchema {

    private String virtualTableName;
    private String realTableName;
    private Set<String> realColumnNames;
    private Map<String, String> columnMap;

    public TableSchema(String virtualTableName, String realTableName) {
        this.virtualTableName = virtualTableName;
        this.realTableName = realTableName;
    }

    public void revise(TableField field) {
        String propName = field.getPropertyName();
        field.setRealColumnName(columnMap.get(propName.toLowerCase()));
    }

    public boolean hasColumn(String propertyName, boolean snakeFormCompatable) {
        if(!snakeFormCompatable) {
            return realColumnNames.contains(propertyName);
        }
        else {
            return columnMap.containsKey(propertyName);
        }
    }

    public static TableSchema query(String virtualTableName, boolean snakeFormCompatable, Statement stmt) {
        TableSchema schema;
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM " + virtualTableName);
            schema = new TableSchema(virtualTableName, virtualTableName);
        }
        catch (SQLException e) {
            if (!snakeFormCompatable) {
                throw new RuntimeException(e);
            }
            else {
                String snakeTableName = CommonUtils.camelForm_to_snakeForm(virtualTableName);
                try {
                    rs = stmt.executeQuery("SELECT * FROM " + snakeTableName);
                }
                catch (SQLException e1) {
                    throw new RuntimeException(e);
                }
                schema = new TableSchema(virtualTableName, snakeTableName);
            }
        }
        schema.realColumnNames = getLowerColumnsOfResultSet(new QuietResultSet(rs));
        if (snakeFormCompatable) {
            schema.setColumnMap(schema.realColumnNames);
        }
        return schema;
    }

    private void setColumnMap(Set<String> realColumnNames) {
        columnMap = new HashMap<>();
        for (String realColumnName : realColumnNames) {
            if (!realColumnName.contains("_")) {
                columnMap.put(realColumnName, realColumnName);
            }
            else {
                String noSnake = StringUtils.replace(realColumnName, "_", "");
                if (!columnMap.containsKey(noSnake)) {
                    columnMap.put(noSnake, realColumnName);
                }
            }
        }
    }

    public static Set<String> getLowerColumnsOfResultSet(QuietResultSet rs) {
        HashSet<String> columnsOfResultSet = new HashSet<>();
        QuietResultSetMetaData metaData = rs.getMetaData();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            columnsOfResultSet.add(metaData.getColumnName(i).toLowerCase());
        }
        return columnsOfResultSet;
    }

    public String getVirtualTableName() {
        return virtualTableName;
    }

    public String getRealTableName() {
        return realTableName;
    }

}
