package zzz404.safesql;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.lang3.Validate;

import zzz404.safesql.util.NoisySupplier;

/**
 * for Spring :
 * ConnectionFactory.create(name).setConnectionPrivider(()->{
 *   return DataSourceUtils.getConnection(dataSource);
 * }).willCloseConnAfterQuery(()->{
 *   return !TransactionSynchronizationManager.isActualTransactionActive();
 * });
 */
public abstract class ConnectionFactory {

    static Map<String, ConnectionFactoryImpl> map = Collections.synchronizedMap(new HashMap<>());

    protected String name;
    protected boolean useTablePrefix;
    protected boolean snakeFormCompatable;
    protected ConnectionProvider connectionProvider;
    protected Supplier<Boolean> willCloseConnAfterQuery = (() -> true);

    public static synchronized ConnectionFactory create(ConnectionProvider connectionProvider) {
        return create("", connectionProvider);
    }

    public static synchronized ConnectionFactory create(String name, ConnectionProvider connectionProvider) {
        Validate.notNull(name);
        Validate.notNull(connectionProvider);
        if (map.containsKey(name)) {
            throw new ConfigException("ConnectionFactory name:" + name + " conflict!");
        }
        ConnectionFactoryImpl factory = new ConnectionFactoryImpl();
        factory.name = name;
        map.put(name, factory);
        factory.connectionProvider = connectionProvider;
        return factory;
    }

    public ConnectionFactory setConnectionPrivider() {
        return this;
    }

    public ConnectionFactory withTablePrefix(boolean tablePrefix) {
        this.useTablePrefix = tablePrefix;
        return this;
    }

    public ConnectionFactory snakeFormCompatable(boolean snakeFormCompatable) {
        this.snakeFormCompatable = snakeFormCompatable;
        return this;
    }

    public ConnectionFactory willCloseConnAfterQuery(NoisySupplier<Boolean> closeConnAfterQuery) {
        this.willCloseConnAfterQuery = NoisySupplier.shutUp(closeConnAfterQuery);
        return this;
    }

}
