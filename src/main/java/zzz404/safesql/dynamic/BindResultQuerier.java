package zzz404.safesql.dynamic;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import zzz404.safesql.Page;
import zzz404.safesql.util.NoisyRunnable;

public abstract class BindResultQuerier<R> {

    protected DynamicQuerier querier;
    protected Entity<R> resultEntity;

    public BindResultQuerier(DynamicQuerier querier, Class<R> resultClass) {
        this.querier = querier;
        resultEntity = new Entity<>(0, resultClass);
    }

    protected void onSelectScope(NoisyRunnable collectColumns) {
        querier.onSelectScope(() -> {
            QueryContext.get().resultEntity = resultEntity;
            collectColumns.run();
        });
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
