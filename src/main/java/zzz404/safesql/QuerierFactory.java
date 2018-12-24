package zzz404.safesql;

import zzz404.safesql.querier.OneEntityQuerier;
import zzz404.safesql.querier.SqlDeleter;
import zzz404.safesql.querier.SqlInserter;
import zzz404.safesql.querier.SqlUpdater;
import zzz404.safesql.querier.StaticSqlQuerier;
import zzz404.safesql.querier.ThreeEntityQuerier;
import zzz404.safesql.querier.TwoEntityQuerier;
import zzz404.safesql.sql.DbSourceImpl;
import zzz404.safesql.util.NoisySupplier;

public class QuerierFactory {
    DbSourceImpl dbSource;

    public QuerierFactory(String name) {
        dbSource = DbSource.get(name);
    }

    public StaticSqlQuerier sql(String sql) {
        return new StaticSqlQuerier(dbSource).sql(sql);
    }

    public <T> OneEntityQuerier<T> from(Class<T> clazz) {
        return new OneEntityQuerier<>(dbSource, clazz);
    }

    public <T, U> TwoEntityQuerier<T, U> from(Class<T> class1, Class<U> class2) {
        return new TwoEntityQuerier<>(dbSource, class1, class2);
    }

    public <T, U, V> ThreeEntityQuerier<T, U, V> from(Class<T> class1, Class<U> class2, Class<V> class3) {
        return new ThreeEntityQuerier<>(dbSource, class1, class2, class3);
    }

    public <T> T withTheSameConnection(NoisySupplier<T> supplier) {
        return DbSourceContext.withDbSource(dbSource, NoisySupplier.shutUp(supplier));
    }

    public <T> T insert(T entity) {
        return new SqlInserter<>(dbSource, entity).execute();
    }

    public <T> SqlUpdater<T> update(T entity) {
        return new SqlUpdater<>(dbSource, entity);
    }

    public <T> SqlDeleter<T> delete(Class<T> clazz) {
        return new SqlDeleter<>(dbSource, clazz);
    }
}