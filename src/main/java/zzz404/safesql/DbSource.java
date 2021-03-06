package zzz404.safesql;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import zzz404.safesql.sql.DbSourceImpl;

/**
 *  for Spring : 
 *  ConnectionFactory.create(name).useConnectionManager(new ConnectionManager() {
 *      @Override
 *      public <T> T underConnection(Function<Connection, T> func) {
 *          return jdbcTemplate.execute((Connection conn) -> func.apply(conn));
 *      }
 *  });
 */
public abstract class DbSource {
    protected String name;
    protected boolean useTablePrefix;
    protected boolean snakeFormCompatable;
    protected ConnectionProvider connectionProvider;
    protected ConnectionManager connectionManager;

    protected static Map<String, DbSourceImpl> map = Collections.synchronizedMap(new HashMap<>());

    protected DbSource(String name) {
        this.name = name;
    }

    public static synchronized DbSource create() {
        return create("");
    }

    public static synchronized DbSource create(String name) {
        Objects.requireNonNull(name);
        if (map.containsKey(name)) {
            throw new ConfigException("ConnectionFactory name:" + name + " conflict!");
        }
        DbSourceImpl ds = new DbSourceImpl(name);
        map.put(name, ds);
        return ds;
    }

    public DbSource useConnectionPrivider(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
        return this;
    }

    public DbSource useConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        return this;
    }

    public DbSource withTablePrefix(boolean tablePrefix) {
        this.useTablePrefix = tablePrefix;
        return this;
    }

    public DbSource snakeFormCompatable(boolean snakeFormCompatable) {
        this.snakeFormCompatable = snakeFormCompatable;
        return this;
    }

    static void clearAll() {
        map.clear();
    }
}
