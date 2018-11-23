package zzz404.safesql.querier;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import zzz404.safesql.Page;

public abstract class BindResultQuerier<R> {

    protected DynamicQuerier querier;
    protected Class<R> resultClass;
    protected R mockedResultObject;

    public BindResultQuerier(DynamicQuerier querier, Class<R> resultClass) {
        this.querier = querier;
        this.mockedResultObject = querier.createMockedObject(resultClass, 0);
    }

    public BindResultQuerier<R> offset(int offset) {
        querier.offset(offset);
        return this;
    }

    public BindResultQuerier<R> limit(int limit) {
        querier.limit(limit);
        return this;
    }

    public Optional<R> queryOne() {
        return querier.queryOne(resultClass);
    }

    public List<R> queryList() {
        return querier.queryList(resultClass);
    }

    public Page<R> queryPage() {
        return querier.queryPage(resultClass);
    }

    public <E> E queryEntityStream(Function<Stream<R>, E> streamReader) {
        return querier.queryStream(resultClass, streamReader);
    }

}
