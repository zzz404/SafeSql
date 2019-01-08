package zzz404.safesql.dynamic;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import zzz404.safesql.Page;
import zzz404.safesql.reflection.OneObjectPlayer;
import zzz404.safesql.sql.DbSourceImpl;

public class OneEntityQuerier<T> extends DynamicQuerier {

    Entity<T> entity = null;

    public OneEntityQuerier(DbSourceImpl dbSource, Class<T> clazz) {
        super(dbSource);
        entity = new Entity<>(1, clazz);
        entities.add(entity);
    }

    public <R> OneEntityBindResultQuerier<T, R> to(Class<R> clazz) {
        return new OneEntityBindResultQuerier<>(this, clazz);
    }

    public OneEntityQuerier<T> select(OneObjectPlayer<T> columnsCollector) {
        onSelectScope(() -> {
            columnsCollector.play(entity.getMockedObject());
        });
        return this;
    }

    public OneEntityQuerier<T> where(OneObjectPlayer<T> conditionsCollector) {
        onWhereScope(() -> {
            conditionsCollector.play(entity.getMockedObject());
        });
        return this;
    }

    public OneEntityQuerier<T> groupBy(OneObjectPlayer<T> columnsCollector) {
        onGroupByScope(() -> {
            columnsCollector.play(entity.getMockedObject());
        });
        return this;
    }

    public OneEntityQuerier<T> orderBy(OneObjectPlayer<T> columnsCollector) {
        onOrderByScope(() -> {
            columnsCollector.play(entity.getMockedObject());
        });
        return this;
    }

    @Override
    public OneEntityQuerier<T> offset(int offset) {
        super.offset(offset);
        return this;
    }

    @Override
    public OneEntityQuerier<T> limit(int limit) {
        super.limit(limit);
        return this;
    }

    @Override
    public Optional<T> queryOne() {
        return queryOne(entity.getObjClass());
    }

    @Override
    public List<T> queryList() {
        return queryList(entity.getObjClass());
    }

    @Override
    public Page<T> queryPage() {
        return queryPage(entity.getObjClass());
    }

    public <E> E queryEntityStream(Function<Stream<T>, E> streamReader) {
        return queryStream(entity.getObjClass(), streamReader);
    }

}
