package zzz404.safesql.dynamic;

import zzz404.safesql.reflection.FourObjectPlayer;
import zzz404.safesql.reflection.ThreeObjectPlayer;

public class ThreeEntityBindResultQuerier<T, U, V, R> extends BindResultQuerier<R> {

    private ThreeEntityQuerier<T, U, V> querier;

    public ThreeEntityBindResultQuerier(ThreeEntityQuerier<T, U, V> querier, Class<R> resultClass) {
        super(querier, resultClass);
        this.querier = querier;
    }

    public ThreeEntityBindResultQuerier<T, U, V, R> select(FourObjectPlayer<T, U, V, R> columnsCollector) {
        super.onSelectScope(() -> {
            columnsCollector.play(querier.entity1.getMockedObject_for_traceGetter(), querier.entity2.getMockedObject_for_traceGetter(),
                    querier.entity3.getMockedObject_for_traceGetter(), resultEntity.getMockedObject_for_traceGetter());
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

}
