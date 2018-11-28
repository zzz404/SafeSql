package zzz404.safesql.querier;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import zzz404.safesql.Entity;
import zzz404.safesql.Page;
import zzz404.safesql.QueryContext;
import zzz404.safesql.sql.OrMapper;

public abstract class BindResultQuerier<R> {

    protected DynamicQuerier querier;
    protected Entity<R> resultEntity;

    protected Map<String, String> columnMap;

    public BindResultQuerier(DynamicQuerier querier, Class<R> resultClass) {
        this.querier = querier;
        resultEntity = new Entity<>(0, resultClass);
    }

    protected void onSelectScope(Runnable collectColumns) {
        querier.onSelectScope(() -> {
            collectColumns.run();
            this.columnMap = QueryContext.get().getColumnMap();
            if (this.columnMap == null) {
                this.columnMap = Collections.emptyMap();
            }
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
        return querier.queryOne(rs -> {
            OrMapper<R> orMapper = querier.getOrMapper(rs, resultEntity.getObjClass());
            return orMapper.mapToObject(columnMap);
        });
    }

    public List<R> queryList() {
        return querier.queryList(rs -> {
            OrMapper<R> orMapper = querier.getOrMapper(rs, resultEntity.getObjClass());
            return orMapper.mapToObject(columnMap);
        });
    }

    public Page<R> queryPage() {
        return querier.queryPage(rs -> {
            OrMapper<R> orMapper = querier.getOrMapper(rs, resultEntity.getObjClass());
            return orMapper.mapToObject(columnMap);
        });
    }

    public <E> E queryEntityStream(Function<Stream<R>, E> streamReader) {
        return querier.queryStream(stream -> {
            Stream<R> objStream = stream.map(rs -> {
                OrMapper<R> orMapper = querier.getOrMapper(rs, resultEntity.getObjClass());
                return orMapper.mapToObject(columnMap);
            });
            return streamReader.apply(objStream);
        });
    }

}