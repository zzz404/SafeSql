package zzz404.safesql;

import zzz404.safesql.querier.OneEntityQuerier;
import zzz404.safesql.querier.StaticSqlQuerier;
import zzz404.safesql.querier.TwoEntityQuerier;

public class QuerierFactory {
    String name;

    public QuerierFactory(String name) {
        this.name = name;
    }

    public StaticSqlQuerier sql(String sql) {
        return new StaticSqlQuerier(name).sql(sql);
    }

    public <T> OneEntityQuerier<T> from(Class<T> clazz) {
        return new OneEntityQuerier<>(name, clazz);
    }

    public <T, U> TwoEntityQuerier<T, U> from(Class<T> class1, Class<U> class2) {
        return new TwoEntityQuerier<>(name, class1, class2);
    }
}