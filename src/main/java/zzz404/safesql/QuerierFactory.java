package zzz404.safesql;

import org.apache.commons.lang3.Validate;

import zzz404.safesql.querier.OneEntityQuerier;
import zzz404.safesql.querier.StaticSqlQuerier;
import zzz404.safesql.querier.TwoEntityQuerier;
import zzz404.safesql.sql.ConnectionFactoryImpl;

public class QuerierFactory {

    ConnectionFactoryImpl connFactory;

    public QuerierFactory(String name) {
        this.connFactory = ConnectionFactory.get(name);
        Validate.notNull(this.connFactory);
    }

    public StaticSqlQuerier sql(String sql) {
        return new StaticSqlQuerier(connFactory).sql(sql);
    }

    public <T> OneEntityQuerier<T> from(Class<T> clazz) {
        return new OneEntityQuerier<>(connFactory, clazz);
    }

    public <T, U> TwoEntityQuerier<T, U> from(Class<T> class1, Class<U> class2) {
        return new TwoEntityQuerier<>(connFactory, class1, class2);
    }
}