package zzz404.safesql;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;

public abstract class ConnectionFactory {

    private static Map<String, ConnectionFactoryImpl> map = Collections
            .synchronizedMap(new HashMap<>());

    protected String tablePrefix;
    protected boolean snakeFormCompatable;
    protected ConnectionProvider connectionProvider;

    public static synchronized ConnectionFactory create() {
        return create("");
    }

    public static synchronized ConnectionFactory create(String name) {
        Validate.notNull(name);
        if (map.containsKey(name)) {
            throw new ConfigException(
                    "ConnectionFactory name:" + name + " conflict!");
        }
        ConnectionFactoryImpl factory = new ConnectionFactoryImpl();
        map.put(name, factory);
        return factory;
    }

    public ConnectionFactory setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
        return this;
    }

    public ConnectionFactory setSnakeFormCompatable(
            boolean snakeFormCompatable) {
        this.snakeFormCompatable = snakeFormCompatable;
        return this;
    }

    public ConnectionFactory setConnectionPrivider(
            ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
        return this;
    }

    protected static ConnectionFactoryImpl get() {
        return get("");
    }

    protected static ConnectionFactoryImpl get(String name) {
        return map.get(name);
    }

    static void clearAll() {
        map.clear();
    }
}
