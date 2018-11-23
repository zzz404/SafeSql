package zzz404.safesql.querier;

import zzz404.safesql.reflection.OneObjectPlayer;
import zzz404.safesql.reflection.TwoObjectPlayer;

public class OneEntityBindResultQuerier<T, R> extends BindResultQuerier<R> {

    private OneEntityQuerier<T> querier;

    public OneEntityBindResultQuerier(OneEntityQuerier<T> querier, Class<R> resultClass) {
        super(querier, resultClass);
        this.querier = querier;
    }

    public OneEntityBindResultQuerier<T, R> select(TwoObjectPlayer<T, R> columnsCollector) {
        querier.onSelectScope(() -> {
            columnsCollector.play(querier.mockedObject, mockedResultObject);
        });
        return this;
    }

    public OneEntityBindResultQuerier<T, R> where(OneObjectPlayer<T> conditionsCollector) {
        querier.where(conditionsCollector);
        return this;
    }

    public OneEntityBindResultQuerier<T, R> groupBy(OneObjectPlayer<T> columnsCollector) {
        querier.groupBy(columnsCollector);
        return this;
    }

    public OneEntityBindResultQuerier<T, R> orderBy(OneObjectPlayer<T> columnsCollector) {
        querier.orderBy(columnsCollector);
        return this;
    }

}
