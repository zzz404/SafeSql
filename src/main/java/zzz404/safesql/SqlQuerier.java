package zzz404.safesql;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public abstract class SqlQuerier {

    protected List<String> fields = Collections.emptyList();
    protected List<Condition> conditions = Collections.emptyList();
    protected List<OrderBy> orderBys = Collections.emptyList();

    protected Integer offset;
    protected Integer limit;

    public SqlQuerier offset(int offset) {
        this.offset = offset;
        return this;
    }

    public SqlQuerier limit(int limit) {
        this.limit = limit;
        return this;
    }

    public final <E> Stream<E> queryStream() {
        return null;
    }

    protected abstract String buildSql();

}
