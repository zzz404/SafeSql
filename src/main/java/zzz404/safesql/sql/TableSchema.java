package zzz404.safesql.sql;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import zzz404.safesql.Field;
import zzz404.safesql.util.CommonUtils;

public class TableSchema {

    private String virtualTableName;
    private String realTableName;
    Set<String> realColumnNames;
    Map<String, String> prop_real_map = new HashMap<>();
    Map<String, String> snake_real_map = new HashMap<>();
    boolean snakeFormCompatable;

    TableSchema(String virtualTableName, String realTableName, boolean snakeFormCompatable) {
        this.virtualTableName = virtualTableName;
        this.realTableName = realTableName;
        this.snakeFormCompatable = snakeFormCompatable;
    }

    public void revise(Field field) {
        String propName = field.getPropertyName();
        String matchedColumn = getMatchedRealColumn(propName.toLowerCase());
        if (matchedColumn != null) {
            field.setRealColumnName(matchedColumn);
        }
    }

    public String getMatchedRealColumn(String propertyName) {
        String prop_lower = propertyName.toLowerCase();
        if (snakeFormCompatable && !prop_real_map.containsKey(prop_lower)) {
            String snake = CommonUtils.camelForm_to_snakeForm(propertyName);
            String real = snake_real_map.get(snake);
            if (real != null) {
                prop_real_map.put(prop_lower, real);
            }
            return real;
        }
        return prop_real_map.get(prop_lower);
    }

    public static TableSchema createByQuery(String virtualTableName, boolean snakeFormCompatable, QuietStatement stmt) {
        TableSchema schema = new TableSchema(virtualTableName, virtualTableName, snakeFormCompatable);
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM " + virtualTableName);
        }
        catch (RuntimeException e) {
            if (!snakeFormCompatable) {
                throw e;
            }
            else {
                String snakeTableName = CommonUtils.camelForm_to_snakeForm(virtualTableName);
                rs = stmt.executeQuery("SELECT * FROM " + snakeTableName);
                schema.realTableName = snakeTableName;
            }
        }
        schema.realColumnNames = getLowerColumnsOfResultSet(new QuietResultSet(rs));
        schema.realColumnNames.forEach(real_columnName -> {
            schema.prop_real_map.put(real_columnName, real_columnName);
            if (snakeFormCompatable) {
                String snake_columnName = CommonUtils.camelForm_to_snakeForm(real_columnName);
                if (schema.snake_real_map.containsKey(snake_columnName)) {
                    String pattern = "Columns %s, %s of Table %s are ambiguous on snakeFormCompatable mode!";
                    throw new TableSchemeException(String.format(pattern, schema.snake_real_map.get(snake_columnName),
                            real_columnName, schema.realTableName));
                }
                else {
                    schema.snake_real_map.put(snake_columnName, real_columnName);
                }
            }
        });
        return schema;
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
