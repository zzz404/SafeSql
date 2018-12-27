package zzz404.safesql.sql;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import zzz404.safesql.DbSource;
import zzz404.safesql.Entity;
import zzz404.safesql.sql.TableSchema.NullTableSchema;
import zzz404.safesql.util.CommonUtils;
import zzz404.safesql.util.NoisySupplier;
import zzz404.safesql.util.Tuple2;

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

    public String getRealTableName(String virtualTableName) {
        if (!snakeFormCompatable) {
            return useTablePrefix ? name + virtualTableName : virtualTableName;
        }
        else {
            TableSchema schema = getSchema(virtualTableName);
            return schema.getRealTableName();
        }
    }

    public TableSchema getSchema(String virtualTableName) {
        if (!tableSchema_map.containsKey(virtualTableName)) {
            TableSchema schema = createSchema(virtualTableName);
            tableSchema_map.put(virtualTableName, schema);
        }
        return tableSchema_map.get(virtualTableName);
    }

    protected TableSchema createSchema(String virtualTableName) {
        return withConnection(conn -> {
            Tuple2<QuietResultSetMetaData, String> tuple;
            try {
                tuple = queryMetaData_and_realTableName__for_snakeCompatable(virtualTableName, conn.createStatement());
            }
            catch (RuntimeException e) {
                return new NullTableSchema(virtualTableName);
            }
            TableSchema tableSchema = new TableSchema(virtualTableName, tuple.second(), snakeFormCompatable);
            tableSchema.initColumns(tuple.first());
            return tableSchema;
        });
    }

    private Tuple2<QuietResultSetMetaData, String> queryMetaData_and_realTableName__for_snakeCompatable(
            String virtualTableName, QuietStatement stmt) {
        QuietResultSetMetaData metaData;
        String tableName = useTablePrefix ? name + virtualTableName : virtualTableName;
        try {
            metaData = queryMetaData(tableName, stmt);
            return new Tuple2<>(metaData, tableName);
        }
        catch (RuntimeException e) {
            tableName = CommonUtils.camelForm_to_snakeForm(virtualTableName);
            if (useTablePrefix) {
                tableName = name + tableName;
            }
            metaData = queryMetaData(tableName, stmt);

            return new Tuple2<>(metaData, tableName);
        }
    }

    @SuppressWarnings("resource")
    private QuietResultSetMetaData queryMetaData(String tableName, QuietStatement stmt) {
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
        return new QuietResultSet(rs).getMetaData();
    }

    public void revise(Entity<?>... entities) {
        if (snakeFormCompatable) {
            for (Entity<?> entity : entities) {
                TableSchema schema = getSchema(entity.getVirtualTableName());
                entity.getFields().forEach(schema::revise_for_snakeFormCompatable);
            }
        }
    }

    public String getName() {
        return name;
    }

}
