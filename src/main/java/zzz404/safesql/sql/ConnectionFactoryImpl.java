package zzz404.safesql.sql;

import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import zzz404.safesql.ConnectionFactory;
import zzz404.safesql.util.CommonUtils;

public class ConnectionFactoryImpl extends ConnectionFactory {

    private Map<String, TableSchema> tableSchema_map = new HashMap<>();

    public ConnectionFactoryImpl(String name) {
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

    public TableSchema getSchema(String virtualTableName) {
        TableSchema schema = tableSchema_map.get(virtualTableName);
        if (schema == null) {
            try(QuietConnection conn = getQuietConnection(); Statement stmt = conn.createStatement();) {
                schema = TableSchema.query(virtualTableName, snakeFormCompatable, stmt);
            }
            catch (Exception e) {
                throw CommonUtils.wrapToRuntime(e);
            }
            tableSchema_map.put(virtualTableName, schema);
        }
        return schema;
    }

}
