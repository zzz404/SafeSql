package zzz404.safesql;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.lang3.Validate;

import zzz404.safesql.sql.DbSourceImpl;
import zzz404.safesql.util.NoisySupplier;

/**
 * for Spring :
 * ConnectionFactory.create(name).setConnectionPrivider(()->{
 *   return DataSourceUtils.getConnection(dataSource);
 * }).willCloseConnAfterQuery(()->{
 *   return !TransactionSynchronizationManager.isActualTransactionActive();
 * });
 */
public abstract class DbSource {

    protected static Map<String, DbSourceImpl> map = Collections.synchronizedMap(new HashMap<>());

    protected String name;
    protected boolean useTablePrefix;
    protected boolean snakeFormCompatable;
    protected ConnectionProvider connectionProvider;
    protected Supplier<Boolean> willCloseConnAfterQuery = (() -> true);

    public static synchronized DbSource create(ConnectionProvider connectionProvider) {
        return create("", connectionProvider);
    }

    public DbSource(String name) {
        this.name = name;
    }

    public static synchronized DbSource create(String name, ConnectionProvider connectionProvider) {
        Validate.notNull(name);
        Validate.notNull(connectionProvider);
        if (map.containsKey(name)) {
            throw new ConfigException("ConnectionFactory name:" + name + " conflict!");
        }
        DbSourceImpl factory = new DbSourceImpl(name);
        map.put(name, factory);
        factory.connectionProvider = connectionProvider;
        return factory;
    }

    public DbSource setConnectionPrivider() {
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

    public DbSource willCloseConnAfterQuery(NoisySupplier<Boolean> closeConnAfterQuery) {
        this.willCloseConnAfterQuery = NoisySupplier.shutUp(closeConnAfterQuery);
        return this;
    }

    public static DbSourceImpl get(String name) {
        return map.get(name);
    }

}
