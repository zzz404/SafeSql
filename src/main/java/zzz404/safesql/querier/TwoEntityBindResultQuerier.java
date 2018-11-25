package zzz404.safesql.querier;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import zzz404.safesql.Page;
import zzz404.safesql.reflection.ThreeObjectPlayer;
import zzz404.safesql.reflection.TwoObjectPlayer;

public class TwoEntityBindResultQuerier<T, U, R> extends BindResultQuerier<R> {

    private TwoEntityQuerier<T, U> querier;

    public TwoEntityBindResultQuerier(TwoEntityQuerier<T, U> querier, Class<R> resultClass) {
        super(querier, resultClass);
    }

    public TwoEntityBindResultQuerier<T, U, R> select(ThreeObjectPlayer<T, U, R> columnsCollector) {
        super.onSelectScope(() -> {
            columnsCollector.play(querier.entity1.getMockedObject(), querier.entity2.getMockedObject(),
                    resultEntity.getMockedObject());
        });
        return this;
    }

    public TwoEntityBindResultQuerier<T, U, R> where(TwoObjectPlayer<T, U> conditionsCollector) {
        querier.where(conditionsCollector);
        return this;
    }

    public TwoEntityBindResultQuerier<T, U, R> groupBy(TwoObjectPlayer<T, U> columnsCollector) {
        querier.groupBy(columnsCollector);
        return this;
    }

    public TwoEntityBindResultQuerier<T, U, R> orderBy(TwoObjectPlayer<T, U> columnsCollector) {
        querier.orderBy(columnsCollector);
        return this;
    }

    public TwoEntityBindResultQuerier<T, U, R> offset(int offset) {
        querier.offset(offset);
        return this;
    }

    public TwoEntityBindResultQuerier<T, U, R> limit(int limit) {
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
