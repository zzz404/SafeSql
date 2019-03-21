package zzz404.safesql;

import zzz404.safesql.dynamic.DynamicDeleter;
import zzz404.safesql.dynamic.DynamicInserter;
import zzz404.safesql.dynamic.DynamicObjectDeleter;
import zzz404.safesql.dynamic.DynamicObjectInserter;
import zzz404.safesql.dynamic.DynamicObjectUpdater;
import zzz404.safesql.dynamic.DynamicUpdater;
import zzz404.safesql.dynamic.OneEntityQuerier;
import zzz404.safesql.dynamic.ThreeEntityQuerier;
import zzz404.safesql.dynamic.TwoEntityQuerier;
import zzz404.safesql.sql.DbSourceImpl;
import zzz404.safesql.sql.StaticSqlExecuter;
import zzz404.safesql.util.NoisySupplier;

public class QuerierFactory {
    DbSourceImpl dbSource;

    public QuerierFactory(String name) {
        dbSource = DbSourceImpl.get(name);
    }

    public StaticSqlExecuter sql(String sql) {
        return new StaticSqlExecuter(dbSource).sql(sql);
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

    public <T> DynamicInserter<T> insert(Class<T> clazz) {
        return new DynamicInserter<>(clazz, dbSource);
    }

    public <T> DynamicUpdater<T> update(Class<T> clazz) {
        return new DynamicUpdater<>(clazz, dbSource);
    }

    public <T> DynamicDeleter<T> delete(Class<T> clazz) {
        return new DynamicDeleter<>(clazz, dbSource);
    }

    public <T> T quickInsert(T entity) {
        return quickInsert(entity, false);
    }

    public <T> T quickInsert(T entity, boolean assignPkValue) {
        new DynamicObjectInserter(entity, dbSource, assignPkValue).execute();
        return entity;
    }

    public <T> T quickUpdate(T entity) {
        new DynamicObjectUpdater(entity, dbSource).execute();
        return entity;
    }

    public void quickDelete(Object entity) {
        new DynamicObjectDeleter(entity, dbSource).execute();
    }
}