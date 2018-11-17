package zzz404.safesql;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import net.sf.cglib.proxy.Enhancer;
import zzz404.safesql.sql.QuietConnection;
import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;
import zzz404.safesql.sql.QuietResultSetIterator;

public abstract class SqlQuerier {

    protected List<String> columnNames = Collections.emptyList();
    protected List<Condition> conditions = Collections.emptyList();
    protected List<OrderBy> orderBys = Collections.emptyList();

    protected int offset = 0;
    protected int limit = 0;

    public SqlQuerier offset(int offset) {
        this.offset = offset;
        return this;
    }

    public SqlQuerier limit(int limit) {
        this.limit = limit;
        return this;
    }

    protected <T> T createMockedObject(Class<T> clazz) {
        Enhancer en = new Enhancer();
        en.setSuperclass(clazz);
        GetterTracer<T> getterLogger = new GetterTracer<>(clazz);
        en.setCallback(getterLogger);

        @SuppressWarnings("unchecked")
        T mockedObject = (T) en.create();
        return mockedObject;
    }

    protected abstract String buildSql();

    protected abstract String buildSql_for_queryCount();

    void setCondValueToPstmt(QuietPreparedStatement pstmt) {
        int i = 1;
        for (Condition cond : conditions) {
            i = cond.setValueToPstmt_and_returnNextIndex(i, pstmt);
        }
    }

    private QuietPreparedStatement prepareStatement(String sql,
            QuietConnection conn) {
        if (offset > 0) {
            return conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
        }
        else {
            return conn.prepareStatement(sql);
        }
    }

    private <T> T query_then_mapAll(Function<QuietResultSet, T> func) {
        String sql = buildSql();

        try (QuietConnection conn = ConnectionFactory.get()
                .getQuietConnection();
                QuietPreparedStatement pstmt = prepareStatement(sql, conn)) {
            setCondValueToPstmt(pstmt);
            QuietResultSet rs = pstmt.executeQuery();
            return func.apply(rs);
        }
    }

    protected <T> T rsToObject(QuietResultSet rs, Class<T> clazz) {
        // TODO
        return null;
    }

    public final int queryCount() {
        String sql = buildSql_for_queryCount();

        try (QuietConnection conn = ConnectionFactory.get()
                .getQuietConnection();
                QuietPreparedStatement pstmt = conn.prepareStatement(sql)) {
            setCondValueToPstmt(pstmt);
            QuietResultSet rs = pstmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
    }

    public final <T> Optional<T> queryOne(Function<QuietResultSet, T> func) {
        return query_then_mapAll(rs -> {
            QuietResultSetIterator iter = new QuietResultSetIterator(rs, offset,
                    1);
            if (iter.hasNext()) {
                T t = func.apply(iter.next());
                return Optional.ofNullable(t);
            }
            else {
                return Optional.empty();
            }
        });
    }

    public final <T> Optional<T> queryOne(Class<T> clazz) {
        return queryOne(rs -> rsToObject(rs, clazz));
    }

    public abstract Object queryOne();

    public final void query_then_consumeEach(
            Consumer<QuietResultSet> consumer) {
        query_then_mapAll(rs -> {
            QuietResultSetIterator iter = new QuietResultSetIterator(rs, offset,
                    limit);
            while (iter.hasNext()) {
                consumer.accept(rs);
            }
            return null;
        });
    }

    public final <T> List<T> queryList(Class<T> clazz) {
        ArrayList<T> result = new ArrayList<>();
        query_then_consumeEach(rs -> {
            result.add(rsToObject(rs, clazz));
        });
        return result;
    }

    public abstract List<?> queryList();

    public final <T> Page<T> queryPage(Class<T> clazz) {
        int totalCount = queryCount();
        List<T> result = queryList(clazz);
        return new Page<>(totalCount, result);
    }

    public abstract Page<?> queryPage();

    public final <T> T queryRsStream(
            Function<Stream<QuietResultSet>, T> rsStreamReader) {
        return query_then_mapAll(rs -> {
            QuietResultSetIterator iter = new QuietResultSetIterator(rs, offset,
                    limit);
            Stream<QuietResultSet> stream = CommonUtils.iter_to_stream(iter);
            return rsStreamReader.apply(stream);
        });
    }

    public final <T, E> T queryStream(Class<E> clazz,
            Function<Stream<E>, T> objStreamReader) {
        return queryRsStream(rsStream -> {
            Stream<E> objStream = rsStream.map(rs -> rsToObject(rs, clazz));
            return objStreamReader.apply(objStream);
        });
    }

}
