package zzz404.safesql;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.lang3.Validate;

import zzz404.safesql.util.NoisySupplier;

public abstract class ConnectionFactory {

    static Map<String, ConnectionFactoryImpl> map = Collections.synchronizedMap(new HashMap<>());

    protected boolean tablePrefix;
    protected boolean snakeFormCompatable;
    protected ConnectionProvider connectionProvider;
    protected Supplier<Boolean> closeConnAfterQuery = (()->true);

    public static synchronized ConnectionFactory create() {
        return create("");
    }

    public static synchronized ConnectionFactory create(String name) {
        Validate.notNull(name);
        if (map.containsKey(name)) {
            throw new ConfigException("ConnectionFactory name:" + name + " conflict!");
        }
        ConnectionFactoryImpl factory = new ConnectionFactoryImpl();
        map.put(name, factory);
        return factory;
    }

    public ConnectionFactory withTablePrefix(boolean tablePrefix) {
        this.tablePrefix = tablePrefix;
        return this;
    }

    public ConnectionFactory snakeFormCompatable(boolean snakeFormCompatable) {
        this.snakeFormCompatable = snakeFormCompatable;
        return this;
    }

    public ConnectionFactory setConnectionPrivider(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
        return this;
    }

    public ConnectionFactory willCloseConnAfterQuery(NoisySupplier<Boolean> closeConnAfterQuery) {
        this.closeConnAfterQuery = NoisySupplier.shutUp( closeConnAfterQuery);
        return this;
    }

    protected static ConnectionFactoryImpl get(String name) {
        return map.get(name);
    }

}
