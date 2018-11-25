package zzz404.safesql.querier;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import zzz404.safesql.Entity;
import zzz404.safesql.Page;
import zzz404.safesql.reflection.ThreeObjectPlayer;
import zzz404.safesql.sql.ConnectionFactoryImpl;
import zzz404.safesql.sql.OrMapper;
import zzz404.safesql.sql.QuietResultSet;
import zzz404.safesql.util.Tuple3;

public class ThreeEntityQuerier<T, U, V> extends DynamicQuerier {

    Entity<T> entity1 = null;
    Entity<U> entity2 = null;
    Entity<V> entity3 = null;

    private transient QuietResultSet rs;
    private transient OrMapper<T> orMapper1;
    private transient OrMapper<U> orMapper2;
    private transient OrMapper<V> orMapper3;

    public ThreeEntityQuerier(ConnectionFactoryImpl connFactory, Class<T> class1, Class<U> class2, Class<V> class3) {
        super(connFactory);
        entity1 = new Entity<>(1, class1);
        entity2 = new Entity<>(2, class2);
        entity3 = new Entity<>(3, class3);
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
        return queryList(rs -> rsToTuple(rs));
    }

    @Override
    public Page<Tuple3<T, U, V>> queryPage() {
        return queryPage(rs -> rsToTuple(rs));
    }

    public <E> E queryEntitiesStream(Function<Stream<Tuple3<T, U, V>>, E> tupleStreamReader) {
        return queryStream(rsStream -> {
            Stream<Tuple3<T, U, V>> tupleStream = rsStream.map(rs -> rsToTuple(rs));
            return tupleStreamReader.apply(tupleStream);
        });
    }

    private Tuple3<T, U, V> rsToTuple(QuietResultSet rs) {
        if (rs != this.rs || orMapper2 == null) {
            TableColumnSeparater separater = new TableColumnSeparater(this.tableFields);
            Set<String> columns1 = separater.getColumnsOfTable(1);
            Set<String> columns2 = separater.getColumnsOfTable(2);
            Set<String> columns3 = separater.getColumnsOfTable(3);
            orMapper1 = getOrMapper(rs, entity1.getObjClass()).selectColumns(columns1);
            orMapper2 = getOrMapper(rs, entity2.getObjClass()).selectColumns(columns2);
            orMapper3 = getOrMapper(rs, entity3.getObjClass()).selectColumns(columns3);
            this.rs = rs;
        }
        T t = orMapper1.mapToObject();
        U u = orMapper2.mapToObject();
        V v = orMapper3.mapToObject();

        return new Tuple3<>(t, u, v);
    }

    @Override
    protected Entity<?>[] getEntites() {
        return new Entity[] { entity1, entity2, entity3 };
    }

}
