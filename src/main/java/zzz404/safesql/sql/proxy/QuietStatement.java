package zzz404.safesql.sql.proxy;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLWarning;
import java.sql.Statement;

import zzz404.safesql.util.CommonUtils;

public class QuietStatement implements Statement {
    Statement stmt = null;

    public QuietStatement(Statement stmt) {
        this.stmt = stmt;
    }

    public ResultSet executeQuery(String sql) {
        try {
            return stmt.executeQuery(sql);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void close() {
        try {
            stmt.close();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public <T> T unwrap(Class<T> iface) {
        try {
            return stmt.unwrap(iface);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean isWrapperFor(Class<?> iface) {
        try {
            return stmt.isWrapperFor(iface);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int executeUpdate(String sql) {
        try {
            return stmt.executeUpdate(sql);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getMaxFieldSize() {
        try {
            return stmt.getMaxFieldSize();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setMaxFieldSize(int max) {
        try {
            stmt.setMaxFieldSize(max);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getMaxRows() {
        try {
            return stmt.getMaxRows();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setMaxRows(int max) {
        try {
            stmt.setMaxRows(max);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setEscapeProcessing(boolean enable) {
        try {
            stmt.setEscapeProcessing(enable);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getQueryTimeout() {
        try {
            return stmt.getQueryTimeout();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setQueryTimeout(int seconds) {
        try {
            stmt.setQueryTimeout(seconds);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void cancel() {
        try {
            stmt.cancel();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public SQLWarning getWarnings() {
        try {
            return stmt.getWarnings();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void clearWarnings() {
        try {
            stmt.clearWarnings();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setCursorName(String name) {
        try {
            stmt.setCursorName(name);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean execute(String sql) {
        try {
            return stmt.execute(sql);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public ResultSet getResultSet() {
        try {
            return stmt.getResultSet();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getUpdateCount() {
        try {
            return stmt.getUpdateCount();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean getMoreResults() {
        try {
            return stmt.getMoreResults();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setFetchDirection(int direction) {
        try {
            stmt.setFetchDirection(direction);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getFetchDirection() {
        try {
            return stmt.getFetchDirection();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setFetchSize(int rows) {
        try {
            stmt.setFetchSize(rows);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getFetchSize() {
        try {
            return stmt.getFetchSize();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getResultSetConcurrency() {
        try {
            return stmt.getResultSetConcurrency();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getResultSetType() {
        try {
            return stmt.getResultSetType();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void addBatch(String sql) {
        try {
            stmt.addBatch(sql);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void clearBatch() {
        try {
            stmt.clearBatch();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int[] executeBatch() {
        try {
            return stmt.executeBatch();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Connection getConnection() {
        try {
            return stmt.getConnection();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean getMoreResults(int current) {
        try {
            return stmt.getMoreResults(current);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public ResultSet getGeneratedKeys() {
        try {
            return stmt.getGeneratedKeys();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int executeUpdate(String sql, int autoGeneratedKeys) {
        try {
            return stmt.executeUpdate(sql, autoGeneratedKeys);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int executeUpdate(String sql, int[] columnIndexes) {
        try {
            return stmt.executeUpdate(sql, columnIndexes);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int executeUpdate(String sql, String[] columnNames) {
        try {
            return stmt.executeUpdate(sql, columnNames);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean execute(String sql, int autoGeneratedKeys) {
        try {
            return stmt.execute(sql, autoGeneratedKeys);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean execute(String sql, int[] columnIndexes) {
        try {
            return stmt.execute(sql, columnIndexes);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean execute(String sql, String[] columnNames) {
        try {
            return stmt.execute(sql, columnNames);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getResultSetHoldability() {
        try {
            return stmt.getResultSetHoldability();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean isClosed() {
        try {
            return stmt.isClosed();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setPoolable(boolean poolable) {
        try {
            stmt.setPoolable(poolable);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean isPoolable() {
        try {
            return stmt.isPoolable();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void closeOnCompletion() {
        try {
            stmt.closeOnCompletion();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean isCloseOnCompletion() {
        try {
            return stmt.isCloseOnCompletion();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public long getLargeUpdateCount() {
        try {
            return stmt.getLargeUpdateCount();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setLargeMaxRows(long max) {
        try {
            stmt.setLargeMaxRows(max);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public long getLargeMaxRows() {
        try {
            return stmt.getLargeMaxRows();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public long[] executeLargeBatch() {
        try {
            return stmt.executeLargeBatch();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public long executeLargeUpdate(String sql) {
        try {
            return stmt.executeLargeUpdate(sql);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public long executeLargeUpdate(String sql, int autoGeneratedKeys) {
        try {
            return stmt.executeLargeUpdate(sql, autoGeneratedKeys);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public long executeLargeUpdate(String sql, int[] columnIndexes) {
        try {
            return stmt.executeLargeUpdate(sql, columnIndexes);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public long executeLargeUpdate(String sql, String[] columnNames) {
        try {
            return stmt.executeLargeUpdate(sql, columnNames);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }
}
