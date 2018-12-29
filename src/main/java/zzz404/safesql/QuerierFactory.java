package zzz404.safesql;

import zzz404.safesql.querier.OneEntityQuerier;
import zzz404.safesql.querier.DynamicDeleter;
import zzz404.safesql.querier.DynamicInserter;
import zzz404.safesql.querier.DynamicUpdater;
import zzz404.safesql.querier.ThreeEntityQuerier;
import zzz404.safesql.querier.TwoEntityQuerier;
import zzz404.safesql.sql.DbSourceImpl;
import zzz404.safesql.sql.StaticSqlExecuterImpl;
import zzz404.safesql.util.NoisySupplier;

public class QuerierFactory {
    DbSourceImpl dbSource;

    public QuerierFactory(String name) {
        dbSource = DbSourceImpl.get(name);
    }

    public StaticSqlExecuterImpl sql(String sql) {
        return new StaticSqlExecuterImpl(dbSource).sql(sql);
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
        return new DynamicInserter<>(dbSource, entity).execute();
    }

    public <T> DynamicUpdater<T> update(T entity) {
        return new DynamicUpdater<>(dbSource, entity);
    }

    public <T> DynamicDeleter<T> delete(Class<T> clazz) {
        return new DynamicDeleter<>(dbSource, clazz);
    }
}