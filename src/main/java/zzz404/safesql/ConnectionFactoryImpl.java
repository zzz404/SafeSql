package zzz404.safesql;

import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import zzz404.safesql.sql.QuietConnection;
import zzz404.safesql.util.CommonUtils;
import zzz404.safesql.util.NoisyRunnable;

public class ConnectionFactoryImpl extends ConnectionFactory {

    private Map<String, String> map = new HashMap<>();

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

    public String getRealTableName(String tableName) {
        String realTableName = map.get(tableName);
        if (realTableName == null) {
            realTableName = tableName;
            if (useTablePrefix) {
                realTableName = name + realTableName;
            }
            if (snakeFormCompatable) {
                realTableName = chooseTableName(realTableName);
            }
            map.put(tableName, realTableName);
        }
        return realTableName;
    }

    private String chooseTableName(String tableName) {
        try (QuietConnection conn = getQuietConnection(); Statement stmt = conn.createStatement()) {
            if (NoisyRunnable.withoutException(() -> stmt.executeQuery("SELECT * FROM " + tableName))) {
                return tableName;
            }
            else {
                String snakeTableName = CommonUtils.camelForm_to_snakeForm(tableName);
                if (NoisyRunnable.withoutException(() -> stmt.executeQuery("SELECT * FROM " + snakeTableName))) {
                    return snakeTableName;
                }
            }
            return tableName;
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }
}
