package zzz404.safesql.querier;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import zzz404.safesql.Entity;
import zzz404.safesql.Page;
import zzz404.safesql.reflection.TwoObjectPlayer;
import zzz404.safesql.sql.DbSourceImpl;
import zzz404.safesql.sql.QuietResultSet;
import zzz404.safesql.util.Tuple2;

public class TwoEntityQuerier<T, U> extends DynamicQuerier {

    protected Entity<T> entity1 = null;
    protected Entity<U> entity2 = null;

    public TwoEntityQuerier(DbSourceImpl dbSource, Class<T> class1, Class<U> class2) {
        super(dbSource);
        entities.add(entity1 = new Entity<>(1, class1));
        entities.add(entity2 = new Entity<>(2, class2));
    }

    public TwoEntityQuerier<T, U> select(TwoObjectPlayer<T, U> columnsCollector) {
        onSelectScope(() -> {
            columnsCollector.play(entity1.getMockedObject(), entity2.getMockedObject());
        });
        return this;
    }

    public TwoEntityQuerier<T, U> where(TwoObjectPlayer<T, U> columnsCollector) {
        onWhereScope(() -> {
            columnsCollector.play(entity1.getMockedObject(), entity2.getMockedObject());
        });
        return this;
    }

    public TwoEntityQuerier<T, U> groupBy(TwoObjectPlayer<T, U> columnsCollector) {
        onGroupByScope(() -> {
            columnsCollector.play(entity1.getMockedObject(), entity2.getMockedObject());
        });
        return this;
    }

    public TwoEntityQuerier<T, U> orderBy(TwoObjectPlayer<T, U> columnsCollector) {
        onOrderByScope(() -> {
            columnsCollector.play(entity1.getMockedObject(), entity2.getMockedObject());
        });
        return this;
    }

    @Override
    public TwoEntityQuerier<T, U> offset(int offset) {
        super.offset(offset);
        return this;
    }

    @Override
    public TwoEntityQuerier<T, U> limit(int limit) {
        super.limit(limit);
        return this;
    }

    @Override
    public Optional<Tuple2<T, U>> queryOne() {
        return queryOne(rs -> rsToTuple(rs));
    }

    @Override
    public List<Tuple2<T, U>> queryList() {
        return queryList_by_mapEach(rs -> rsToTuple(rs));
    }

    @Override
    public Page<Tuple2<T, U>> queryPage() {
        return queryPage_by_mapEach(rs -> rsToTuple(rs));
    }

    public <E> E queryEntitiesStream(Function<Stream<Tuple2<T, U>>, E> tupleStreamReader) {
        return queryStream(rsStream -> {
            Stream<Tuple2<T, U>> tupleStream = rsStream.map(rs -> rsToTuple(rs));
            return tupleStreamReader.apply(tupleStream);
        });
    }

    protected Tuple2<T, U> rsToTuple(QuietResultSet rs) {
        T t = entity1.mapToObject(rs, getFieldsOfEntity(entity1));
        U u = entity2.mapToObject(rs, getFieldsOfEntity(entity2));
        return new Tuple2<>(t, u);
    }

}
