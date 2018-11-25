package zzz404.safesql.querier;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import zzz404.safesql.Page;
import zzz404.safesql.sql.ConnectionFactoryImpl;
import zzz404.safesql.sql.OrMapper;
import zzz404.safesql.sql.QuietConnection;
import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;
import zzz404.safesql.sql.QuietResultSetIterator;
import zzz404.safesql.type.ValueType;
import zzz404.safesql.util.CommonUtils;

public abstract class SqlQuerier {

    ConnectionFactoryImpl connFactory;

    protected int offset = 0;
    protected int limit = 0;

    private transient QuietResultSet rs = null;
    transient OrMapper<?> orMapper = null;

    public SqlQuerier(ConnectionFactoryImpl connFactory) {
        this.connFactory = connFactory;
    }

    public SqlQuerier offset(int offset) {
        this.offset = offset;
        return this;
    }

    public SqlQuerier limit(int limit) {
        this.limit = limit;
        return this;
    }

    protected final void setCondsValueToPstmt(QuietPreparedStatement pstmt) {
        int i = 1;
        for (Object paramValue : paramValues()) {
            ValueType.setValueToPstmt(pstmt, i++, paramValue);
        }
    }

    private QuietPreparedStatement prepareStatement(String sql, QuietConnection conn) {
        if (offset > 0) {
            return conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        }
        else {
            return conn.prepareStatement(sql);
        }
    }

    private <T> T query_then_mapAll(Function<QuietResultSet, T> func) {
        String sql = sql();
        return query_then_mapAll(sql, func);
    }

    private <T> T query_then_mapAll(String sql, Function<QuietResultSet, T> func) {
        QuietConnection conn = connFactory.getQuietConnection();
        try (QuietPreparedStatement pstmt = prepareStatement(sql, conn)) {
            setCondsValueToPstmt(pstmt);
            QuietResultSet rs;
            rs = new QuietResultSet(pstmt.executeQuery());
            return func.apply(rs);
        }
        finally {
            connFactory.closeConnection(conn);
        }
    }

    public final int queryCount() {
        String sql = sql_for_queryCount();
        int count = query_then_mapAll(sql, rs -> {
            rs.next();
            return rs.getInt(1);
        });
        return count;
    }

    public final <T> Optional<T> queryOne(Function<QuietResultSet, T> mapper) {
        return query_then_mapAll(rs -> {
            QuietResultSetIterator iter = new QuietResultSetIterator(rs, offset, 1);
            if (iter.hasNext()) {
                T t = mapper.apply(iter.next());
                return Optional.ofNullable(t);
            }
            else {
                return Optional.empty();
            }
        });
    }

    protected <T> T rsToObject(QuietResultSet rs, Class<T> clazz) {
        OrMapper<T> orMapper = getOrMapper(rs, clazz);
        return orMapper.mapToObject();
    }

    @SuppressWarnings("unchecked")
    protected <T> OrMapper<T> getOrMapper(QuietResultSet rs, Class<T> clazz) {
        if (rs != this.rs || this.orMapper == null) {
            this.orMapper = new OrMapper<>(clazz, rs);
            this.rs = rs;
        }
        return (OrMapper<T>) this.orMapper;
    }

    public final <T> Optional<T> queryOne(Class<T> clazz) {
        return queryOne(rs -> rsToObject(rs, clazz));
    }

    public final void query_then_consumeEach(Consumer<QuietResultSet> consumer) {
        query_then_mapAll(rs -> {
            QuietResultSetIterator iter = new QuietResultSetIterator(rs, offset, limit);
            while (iter.hasNext()) {
                consumer.accept(iter.next());
            }
            return null;
        });
    }

    public final <T> List<T> queryList(Function<QuietResultSet, T> mapper) {
        ArrayList<T> result = new ArrayList<>();
        query_then_consumeEach(rs -> {
            result.add(mapper.apply(rs));
        });
        return result;
    }

    public final <T> List<T> queryList(Class<T> clazz) {
        return queryList(rs -> rsToObject(rs, clazz));
    }

    public final <T> Page<T> queryPage(Function<QuietResultSet, T> mapper) {
        int totalCount = queryCount();
        List<T> result = queryList(mapper);
        return new Page<>(totalCount, result);
    }

    public final <T> Page<T> queryPage(Class<T> clazz) {
        return queryPage(rs -> rsToObject(rs, clazz));
    }

    public final <T> T queryStream(Function<Stream<QuietResultSet>, T> rsStreamReader) {
        return query_then_mapAll(rs -> {
            QuietResultSetIterator iter = new QuietResultSetIterator(rs, offset, limit);
            Stream<QuietResultSet> stream = CommonUtils.iter_to_stream(iter);
            return rsStreamReader.apply(stream);
        });
    }

    public final <T, R> R queryStream(Class<T> clazz, Function<Stream<T>, R> objStreamReader) {
        return queryStream(rsStream -> {
            Stream<T> objStream = rsStream.map(rs -> rsToObject(rs, clazz));
            return objStreamReader.apply(objStream);
        });
    }

    protected abstract String sql();

    protected abstract String sql_for_queryCount();

    protected abstract Object[] paramValues();

    public ConnectionFactoryImpl getConnectionFactory() {
        return connFactory;
    }

}
