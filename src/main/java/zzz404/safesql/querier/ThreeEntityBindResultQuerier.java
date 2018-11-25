package zzz404.safesql.querier;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import zzz404.safesql.Page;
import zzz404.safesql.reflection.FourObjectPlayer;
import zzz404.safesql.reflection.ThreeObjectPlayer;

public class ThreeEntityBindResultQuerier<T, U, V, R> extends BindResultQuerier<R> {

    private ThreeEntityQuerier<T, U, V> querier;

    public ThreeEntityBindResultQuerier(ThreeEntityQuerier<T, U, V> querier, Class<R> resultClass) {
        super(querier, resultClass);
    }

    public ThreeEntityBindResultQuerier<T, U, V, R> select(FourObjectPlayer<T, U, V, R> columnsCollector) {
        super.onSelectScope(() -> {
            columnsCollector.play(querier.entity1.getMockedObject(), querier.entity2.getMockedObject(),
                    querier.entity3.getMockedObject(), resultEntity.getMockedObject());
        });
        return this;
    }

    public ThreeEntityBindResultQuerier<T, U, V, R> where(ThreeObjectPlayer<T, U, V> conditionsCollector) {
        querier.where(conditionsCollector);
        return this;
    }

    public ThreeEntityBindResultQuerier<T, U, V, R> groupBy(ThreeObjectPlayer<T, U, V> columnsCollector) {
        querier.groupBy(columnsCollector);
        return this;
    }

    public ThreeEntityBindResultQuerier<T, U, V, R> orderBy(ThreeObjectPlayer<T, U, V> columnsCollector) {
        querier.orderBy(columnsCollector);
        return this;
    }

    public ThreeEntityBindResultQuerier<T, U, V, R> offset(int offset) {
        querier.offset(offset);
        return this;
    }

    public ThreeEntityBindResultQuerier<T, U, V, R> limit(int limit) {
        querier.limit(limit);
        return this;
    }

    public Optional<R> queryOne() {
        return querier.queryOne(resultEntity.getObjClass());
    }

    public List<R> queryList() {
        return querier.queryList(resultEntity.getObjClass());
    }

    public Page<R> queryPage() {
        return querier.queryPage(resultEntity.getObjClass());
    }

    public <E> E queryEntityStream(Function<Stream<R>, E> streamReader) {
        return querier.queryStream(resultEntity.getObjClass(), streamReader);
    }

}
