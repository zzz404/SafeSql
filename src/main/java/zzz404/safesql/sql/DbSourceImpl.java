package zzz404.safesql.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.apache.commons.collections4.CollectionUtils;

import zzz404.safesql.DbSource;
import zzz404.safesql.dynamic.Entity;
import zzz404.safesql.sql.proxy.EnhancedConnection;
import zzz404.safesql.sql.proxy.QuietPreparedStatement;
import zzz404.safesql.sql.proxy.QuietStatement;
import zzz404.safesql.sql.type.TypedValue;
import zzz404.safesql.util.NoisySupplier;

public class DbSourceImpl extends DbSource {

    private Map<String, TableSchema> tableSchema_map = new HashMap<>();

    public DbSourceImpl(String name) {
        super(name);
    }

    public static DbSourceImpl get(String name) {
        DbSourceImpl dbSource = map.get(name);
        Objects.requireNonNull(dbSource);
        return dbSource;
    }

    public <T> T withConnection(Function<EnhancedConnection, T> func) {
        if (connectionManager != null) {
            return NoisySupplier.getQuietly(
                    () -> connectionManager.underConnection(conn -> func.apply(new EnhancedConnection(conn))));
        }
        else {
            return NoisySupplier.getQuietly(() -> {
                try (EnhancedConnection conn = new EnhancedConnection(connectionProvider.getConnection())) {
                    return func.apply(conn);
                }
            });
        }
    }

    public TableSchema getSchema(String entityName) {
        if (!tableSchema_map.containsKey(entityName)) {
            TableSchema schema = TableSchema.createSchema(entityName, this);
            tableSchema_map.put(entityName, schema);
        }
        return tableSchema_map.get(entityName);
    }

    public String getName() {
        return name;
    }

    public boolean isUseTablePrefix() {
        return useTablePrefix;
    }

    public boolean isSnakeFormCompatable() {
        return snakeFormCompatable;
    }

    public void revise(Entity<?>... entities) {
        if (snakeFormCompatable) {
            for (Entity<?> entity : entities) {
                TableSchema schema = getSchema(entity.getName());

                entity.getFields().forEach(field -> {
                    field.revisedBy(schema);
                });
            }
        }
    }

    public int update(String sql, List<TypedValue<?>> paramValues) {
        return withConnection(conn -> {
            if (CollectionUtils.isEmpty(paramValues)) {
                QuietStatement stmt = conn.createStatement();
                return stmt.executeUpdate(sql);
            }
            else {
                QuietPreparedStatement pstmt = conn.prepareStatement(sql);
                int i = 1;
                for (TypedValue<?> tv : paramValues) {
                    tv.setToPstmt(pstmt, i++);
                }
                return pstmt.executeUpdate();
            }
        });
    }  
}
