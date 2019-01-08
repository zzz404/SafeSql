package zzz404.safesql.dynamic;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import zzz404.safesql.Page;
import zzz404.safesql.reflection.ThreeObjectPlayer;
import zzz404.safesql.sql.DbSourceImpl;
import zzz404.safesql.sql.proxy.QuietResultSet;
import zzz404.safesql.util.Tuple3;

public class ThreeEntityQuerier<T, U, V> extends DynamicQuerier {

    Entity<T> entity1 = null;
    Entity<U> entity2 = null;
    Entity<V> entity3 = null;

    public ThreeEntityQuerier(DbSourceImpl connFactory, Class<T> class1, Class<U> class2, Class<V> class3) {
        super(connFactory);
        entities.add(entity1 = new Entity<>(1, class1));
        entities.add(entity2 = new Entity<>(2, class2));
        entities.add(entity3 = new Entity<>(3, class3));
    }

    public <R> ThreeEntityBindResultQuerier<T, U, V, R> to(Class<R> clazz) {
        return new ThreeEntityBindResultQuerier<>(this, clazz);
    }

    public ThreeEntityQuerier<T, U, V> select(ThreeObjectPlayer<T, U, V> columnsCollector) {
        onSelectScope(() -> {
            columnsCollector.play(entity1.getMockedObject(), entity2.getMockedObject(), entity3.getMockedObject());
        });
        return this;
    }

    public ThreeEntityQuerier<T, U, V> where(ThreeObjectPlayer<T, U, V> columnsCollector) {
        onWhereScope(() -> {
            columnsCollector.play(entity1.getMockedObject(), entity2.getMockedObject(), entity3.getMockedObject());
        });
        return this;
    }

    public ThreeEntityQuerier<T, U, V> groupBy(ThreeObjectPlayer<T, U, V> columnsCollector) {
        onGroupByScope(() -> {
            columnsCollector.play(entity1.getMockedObject(), entity2.getMockedObject(), entity3.getMockedObject());
        });
        return this;
    }

    public ThreeEntityQuerier<T, U, V> orderBy(ThreeObjectPlayer<T, U, V> columnsCollector) {
        onOrderByScope(() -> {
            columnsCollector.play(entity1.getMockedObject(), entity2.getMockedObject(), entity3.getMockedObject());
        });
        return this;
    }

    @Override
    public ThreeEntityQuerier<T, U, V> offset(int offset) {
        super.offset(offset);
        return this;
    }

    @Override
    public ThreeEntityQuerier<T, U, V> limit(int limit) {
        super.limit(limit);
        return this;
    }

    @Override
    public Optional<Tuple3<T, U, V>> queryOne() {
        return queryOne(rs -> rsToTuple(rs));
    }

    @Override
    public List<Tuple3<T, U, V>> queryList() {
        return queryList_by_mapEach(rs -> rsToTuple(rs));
    }

    @Override
    public Page<Tuple3<T, U, V>> queryPage() {
        return queryPage_by_mapEach(rs -> rsToTuple(rs));
    }

    public <E> E queryEntitiesStream(Function<Stream<Tuple3<T, U, V>>, E> tupleStreamReader) {
        return queryStream(rsStream -> {
            Stream<Tuple3<T, U, V>> tupleStream = rsStream.map(rs -> rsToTuple(rs));
            return tupleStreamReader.apply(tupleStream);
        });
    }

    protected Tuple3<T, U, V> rsToTuple(QuietResultSet rs) {
        T t = rsToObject(rs, entity1);
        U u = rsToObject(rs, entity2);
        V v = rsToObject(rs, entity3);
        return new Tuple3<>(t, u, v);
    }

}
