package zzz404.safesql;

import java.util.stream.Stream;

public abstract class SqlQuerier {

    private Integer offset;
    private Integer limit;

    public SqlQuerier offset(int offset) {
        this.offset = offset;
        return this;
    }

    public SqlQuerier limit(int limit) {
        this.limit = limit;
        return this;
    }

    public final <E> Stream<E> queryStream() {
        build();
        return null;
    }

    protected abstract SqlQuerier build();

}
