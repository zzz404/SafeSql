package zzz404.safesql.sql;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;

import zzz404.safesql.DbSourceContext;
import zzz404.safesql.Page;
import zzz404.safesql.SqlQueryException;
import zzz404.safesql.sql.proxy.EnhancedConnection;
import zzz404.safesql.sql.proxy.QuietPreparedStatement;
import zzz404.safesql.sql.proxy.QuietResultSet;
import zzz404.safesql.sql.proxy.QuietResultSetIterator;
import zzz404.safesql.sql.proxy.QuietStatement;
import zzz404.safesql.sql.type.TypedValue;
import zzz404.safesql.util.CommonUtils;

public abstract class SqlQuerier {

    protected DbSourceImpl dbSource;

    protected int offset = 0;
    protected int limit = 0;

    private transient QuietResultSet rs = null;
    private transient OrMapper orMapper = null;

    public SqlQuerier(DbSourceImpl dbSource) {
        this.dbSource = dbSource;
    }

    public SqlQuerier offset(int offset) {
        this.offset = offset;
        return this;
    }

    public SqlQuerier limit(int limit) {
        this.limit = limit;
        return this;
    }

    private <T> T query_then_mapAll(Function<QuietResultSet, T> func) {
        String sql = sql();
        return query_then_mapAll(sql, func);
    }

    private <T> T query_then_mapAll(String sql, Function<QuietResultSet, T> func) {
        List<TypedValue<?>> paramValues = paramValues();

        try {
            return DbSourceContext.withConnection(dbSource, conn -> {
                if (CollectionUtils.isEmpty(paramValues)) {
                    QuietStatement stmt = createStatement(conn);
                    QuietResultSet rs = new QuietResultSet(stmt.executeQuery(sql));
                    return func.apply(rs);
                }
                else {
                    QuietPreparedStatement pstmt = prepareStatement(sql, conn);
                    int i = 1;
                    for (TypedValue<?> paramValue : paramValues) {
                        paramValue.setToPstmt(pstmt, i++);
                    }
                    QuietResultSet rs;
                    rs = new QuietResultSet(pstmt.executeQuery());
                    return func.apply(rs);
                }
            });
        }
        catch (RuntimeException e) {
            throw new SqlQueryException(sql, paramValues, e);
        }
    }

    private QuietStatement createStatement(EnhancedConnection conn) {
        if (offset > 0) {
            return conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        }
        else {
            return conn.createStatement();
        }
    }

    private QuietPreparedStatement prepareStatement(String sql, EnhancedConnection conn) {
        if (offset > 0) {
            return conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        }
        else {
            return conn.prepareStatement(sql);
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

    public <T> Optional<T> queryOne(Function<QuietResultSet, T> mapper) {
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
        if (TypedValue.supportType(clazz)) {
            return TypedValue.valueOf(clazz).readFirstFromRs(rs).getValue();
        }
        return getOrMapper(rs).mapToObject(clazz, false);
    }

    protected OrMapper getOrMapper(QuietResultSet rs) {
        if (rs != this.rs || this.orMapper == null) {
            this.orMapper = new OrMapper(rs, dbSource);
            this.rs = rs;
        }
        return this.orMapper;
    }

    public <T> Optional<T> queryOne(Class<T> clazz) {
        return queryOne(rs -> rsToObject(rs, clazz));
    }

    public void query_then_consumeEach(Consumer<QuietResultSet> consumer) {
        query_then_mapAll(rs -> {
            QuietResultSetIterator iter = new QuietResultSetIterator(rs, offset, limit);
            while (iter.hasNext()) {
                consumer.accept(iter.next());
            }
            return null;
        });
    }

    public <T> List<T> queryList_by_mapEach(Function<QuietResultSet, T> mapper) {
        ArrayList<T> result = new ArrayList<>();
        query_then_consumeEach(rs -> {
            result.add(mapper.apply(rs));
        });
        return result;
    }

    public <T> List<T> queryList(Class<T> clazz) {
        return queryList_by_mapEach(rs -> rsToObject(rs, clazz));
    }

    public <T> Page<T> queryPage_by_mapEach(Function<QuietResultSet, T> mapper) {
        int totalCount = queryCount();
        List<T> result = queryList_by_mapEach(mapper);
        return new Page<>(totalCount, result);
    }

    public <T> Page<T> queryPage(Class<T> clazz) {
        return queryPage_by_mapEach(rs -> rsToObject(rs, clazz));
    }

    public <T> T queryStream(Function<Stream<QuietResultSet>, T> rsStreamReader) {
        return query_then_mapAll(rs -> {
            QuietResultSetIterator iter = new QuietResultSetIterator(rs, offset, limit);
            Stream<QuietResultSet> stream = CommonUtils.iter_to_stream(iter);
            return rsStreamReader.apply(stream);
        });
    }

    public <T, R> R queryStream(Class<T> clazz, Function<Stream<T>, R> objStreamReader) {
        return queryStream(rsStream -> {
            Stream<T> objStream = rsStream.map(rs -> rsToObject(rs, clazz));
            return objStreamReader.apply(objStream);
        });
    }

    public abstract String sql();

    protected abstract String sql_for_queryCount();

    protected abstract List<TypedValue<?>> paramValues();

}
