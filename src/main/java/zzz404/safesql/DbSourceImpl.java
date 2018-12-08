package zzz404.safesql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import zzz404.safesql.sql.EnhancedConnection;
import zzz404.safesql.sql.QuietStatement;
import zzz404.safesql.sql.TableSchema;

public class DbSourceImpl extends DbSource {

    private Map<String, TableSchema> tableSchema_map = new HashMap<>();

    public DbSourceImpl(String name) {
        super(name);
    }

    protected <T> T withConnection(Function<EnhancedConnection, T> func) {
        if (connectionManager != null) {
            return connectionManager.underConnection(conn -> func.apply(new EnhancedConnection(conn)));
        }
        else {
            try (EnhancedConnection conn = new EnhancedConnection(connectionProvider.getConnection())) {
                return func.apply(conn);
            }
        }
    }

    public String getRealTableName(String virtualTableName) {
        if (!snakeFormCompatable) {
            return virtualTableName;
        }
        else {
            return getSchema(virtualTableName).getRealTableName();
        }
    }

    public TableSchema getSchema(String virtualTableName) {
        TableSchema schema = tableSchema_map.get(virtualTableName);
        if (schema == null) {
            schema = withConnection(conn -> {
                try (QuietStatement stmt = conn.createStatement()) {
                    return TableSchema.createByQuery(virtualTableName, snakeFormCompatable, stmt);
                }
            });
            tableSchema_map.put(virtualTableName, schema);
        }
        return schema;
    }

    public void revise(List<Entity<?>> entities) {
        if (snakeFormCompatable) {
            for (Entity<?> entity : entities) {
                TableSchema schema = getSchema(entity.getVirtualTableName());
                entity.getFields().forEach(schema::revise_for_snakeFormCompatable);
            }
        }
    }

    public void revise(Field field) {
        if (snakeFormCompatable) {
            TableSchema schema = getSchema(field.getEntity().getVirtualTableName());
            schema.revise_for_snakeFormCompatable(field);
        }
    }

}
