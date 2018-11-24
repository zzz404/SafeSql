package zzz404.safesql;

import zzz404.safesql.querier.OneEntityQuerier;
import zzz404.safesql.querier.StaticSqlQuerier;
import zzz404.safesql.querier.TwoEntityQuerier;

public class QuerierFactory {

    ConnectionFactoryImpl connFactory;

    public QuerierFactory(String name) {
        this.connFactory = ConnectionFactory.get(name);
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