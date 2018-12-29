package zzz404.safesql.sql;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import zzz404.safesql.Field;
import zzz404.safesql.util.CommonUtils;

public class TableSchema {

    String virtualTableName;
    String realTableName;
    Map<String, String> prop_real_map = new HashMap<>();
    Map<String, String> snake_real_map = new HashMap<>();
    boolean snakeFormCompatable;
    String autoIncrementColumnName;

    TableSchema(String virtualTableName, String realTableName, boolean snakeFormCompatable) {
        this.virtualTableName = virtualTableName;
        this.realTableName = realTableName;
        this.snakeFormCompatable = snakeFormCompatable;
    }

    public void revise_for_snakeFormCompatable(Field<?> field) {
        String propName = field.getPropertyName();
        String realColumnName = getRealColumnName(propName);
        if (realColumnName != null) {
            field.realColumnName = realColumnName;
        }
    }

    public String getRealTableName() {
        return realTableName;
    }

    public String getRealColumnName(String propName) {
        String prop_lower = propName.toLowerCase();
        if (!prop_real_map.containsKey(prop_lower)) {
            String snaked_propName = CommonUtils.camelForm_to_snakeForm(propName);
            String realColumnName = snake_real_map.get(snaked_propName);
            prop_real_map.put(prop_lower, realColumnName);
        }
        return prop_real_map.get(prop_lower);
    }
    
    void initColumns(QuietResultSetMetaData metaData) {
        Set<String> columnsOfResultSet = getColumns(metaData);
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            if (metaData.isAutoIncrement(i)) {
                autoIncrementColumnName = metaData.getColumnName(i);
                break;
            }
        }
        columnsOfResultSet.forEach(real_columnName -> {
            prop_real_map.put(real_columnName.toLowerCase(), real_columnName);
            if (snakeFormCompatable) {
                String snake_columnName = CommonUtils.camelForm_to_snakeForm(real_columnName);
                if (snake_real_map.containsKey(snake_columnName)) {
                    String pattern = "Columns %s, %s of Table %s are ambiguous on snakeFormCompatable mode!";
                    throw new TableSchemeException(String.format(pattern, snake_real_map.get(snake_columnName),
                            real_columnName, realTableName));
                }
                else {
                    snake_real_map.put(snake_columnName, real_columnName);
                }
            }
        });
    }

    public static Set<String> getColumns(QuietResultSetMetaData metaData) {
        HashSet<String> columnsOfResultSet = new HashSet<>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            columnsOfResultSet.add(metaData.getColumnName(i));
        }
        return columnsOfResultSet;
    }

    public static class NullTableSchema extends TableSchema {
        NullTableSchema(String virtualTableName) {
            super(virtualTableName, virtualTableName, false);
        }

        public void revise_for_snakeFormCompatable(Field<?> field) {
        }

    }

    public String getRealColumnName_of_autoIncrement() {
        return autoIncrementColumnName;
    }
}
