package zzz404.safesql.dynamic;

import zzz404.safesql.reflection.ThreeObjectPlayer;
import zzz404.safesql.reflection.TwoObjectPlayer;

public class TwoEntityBindResultQuerier<T, U, R> extends BindResultQuerier<R> {

    private TwoEntityQuerier<T, U> querier;

    public TwoEntityBindResultQuerier(TwoEntityQuerier<T, U> querier, Class<R> resultClass) {
        super(querier, resultClass);
        this.querier = querier;
    }

    public TwoEntityBindResultQuerier<T, U, R> select(ThreeObjectPlayer<T, U, R> columnsCollector) {
        super.onSelectScope(() -> {
            columnsCollector.play(querier.entity1.getMockedObject_for_traceGetter(), querier.entity2.getMockedObject_for_traceGetter(),
                    resultEntity.getMockedObject_for_traceGetter());
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

}
