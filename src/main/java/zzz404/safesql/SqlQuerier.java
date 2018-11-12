package zzz404.safesql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import zzz404.safesql.sql.ResultSetAnalyzer;

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

    public final <E> Stream<E> queryStream() {
        return null;
    }

    protected abstract String buildSql();

    protected abstract String buildSql_for_queryCount();

    void setCondValueToPstmt(PreparedStatement pstmt) {
        int i = 1;
        for (Condition cond : conditions) {
            i = cond.setValueToPstmt_and_returnNextIndex(i, pstmt);
        }
    }

    public abstract Object queryOne();

    public <T> Optional<T> queryOne(Class<T> clazz) {
        String sql = buildSql();
        Connection conn = ConnectionProvider.instance.getConnection();
        try (PreparedStatement pstmt = prepareStatement(sql, conn)) {
            setCondValueToPstmt(pstmt);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                return Optional.empty();
            }
            else {
                T o = new ResultSetAnalyzer(rs).readToObject(clazz);
                return Optional.of(o);
            }
        }
        catch (Exception e) {
            throw Utils.throwRuntime(e);
        }
    }

    private PreparedStatement prepareStatement(String sql, Connection conn) {
        try {
            if (offset > 0) {
                return conn.prepareStatement(sql);
            }
            else {
                return conn.prepareStatement(sql,
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
            }
        }
        catch (Exception e) {
            throw Utils.throwRuntime(e);
        }
    }

    public <T> List<T> queryList(Class<T> clazz) {
        String sql = buildSql();
        Connection conn = ConnectionProvider.instance.getConnection();
        try (PreparedStatement pstmt = prepareStatement(sql, conn)) {
            setCondValueToPstmt(pstmt);
            ResultSet rs = pstmt.executeQuery();
            List<T> result = new ArrayList<>();
            ResultSetAnalyzer rsAnalyzer = new ResultSetAnalyzer(rs);
            int i = 0;
            while (rs.next()) {
                if (limit > 0 && ++i > limit) {
                    break;
                }
                T o = rsAnalyzer.readToObject(clazz);
                result.add(o);
            }
            return result;
        }
        catch (Exception e) {
            throw Utils.throwRuntime(e);
        }
    }

    public int queryCount() {
        String sql = buildSql_for_queryCount();
        Connection conn = ConnectionProvider.instance.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            setCondValueToPstmt(pstmt);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
        catch (Exception e) {
            throw Utils.throwRuntime(e);
        }
    }

    public abstract Page<?> queryPage();
    
    public <T> Page<T> queryPage(Class<T> clazz) {
        int totalCount = queryCount();
        List<T> result = queryList(clazz);
        return new Page<>(totalCount, result);
    }
}
