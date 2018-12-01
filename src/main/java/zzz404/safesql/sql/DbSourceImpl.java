package zzz404.safesql.sql;

import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zzz404.safesql.DbSource;
import zzz404.safesql.Entity;
import zzz404.safesql.Field;
import zzz404.safesql.util.CommonUtils;

public class DbSourceImpl extends DbSource {

    private Map<String, TableSchema> tableSchema_map = new HashMap<>();

    public DbSourceImpl(String name) {
        super(name);
    }

    public QuietConnection getQuietConnection() {
        return new QuietConnection(connectionProvider.getConnection());
    }

    public void closeConnection(QuietConnection conn) {
        if (willCloseConnAfterQuery.get()) {
            conn.close();
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
            try (QuietConnection conn = getQuietConnection(); Statement stmt = conn.createStatement();) {
                schema = TableSchema.query(virtualTableName, snakeFormCompatable, stmt);
            }
            catch (Exception e) {
                throw CommonUtils.wrapToRuntime(e);
            }
            tableSchema_map.put(virtualTableName, schema);
        }
        return schema;
    }

    public void revise(List<Entity<?>> entities) {
        if (snakeFormCompatable) {
            for (Entity<?> entity : entities) {
                TableSchema schema = getSchema(entity.getVirtualTableName());
                entity.getFields().forEach(schema::revise);
            }
        }
    }

    public void revise(Field field) {
        if (snakeFormCompatable) {
            TableSchema schema = getSchema(field.getEntity().getVirtualTableName());
            schema.revise(field);
        }
    }

    public Entity<?> chooseEntity(List<Entity<?>> entities, String propertyName) {
        for (Entity<?> entity : entities) {
            TableSchema schema = getSchema(entity.getVirtualTableName());
            if (schema.hasColumn(propertyName, false)) {
                return entity;
            }
        }
        if (snakeFormCompatable) {
            for (Entity<?> entity : entities) {
                TableSchema schema = getSchema(entity.getVirtualTableName());
                if (schema.hasColumn(propertyName, true)) {
                    return entity;
                }
            }
        }
        return null;
    }

}
