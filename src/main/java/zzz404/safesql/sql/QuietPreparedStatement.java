package zzz404.safesql.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLType;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import zzz404.safesql.util.CommonUtils;

public class QuietPreparedStatement implements PreparedStatement {
    PreparedStatement pstmt;

    public QuietPreparedStatement(PreparedStatement pstmt) {
        this.pstmt = pstmt;
    }

    public void close() {
        try {
            pstmt.close();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public ResultSet executeQuery(String sql) {
        try {
            return pstmt.executeQuery(sql);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public ResultSet executeQuery() {
        try {
            return pstmt.executeQuery();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public <T> T unwrap(Class<T> iface) {
        try {
            return pstmt.unwrap(iface);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean isWrapperFor(Class<?> iface) {
        try {
            return pstmt.isWrapperFor(iface);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int executeUpdate(String sql) {
        try {
            return pstmt.executeUpdate(sql);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int executeUpdate() {
        try {
            return pstmt.executeUpdate();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setNull(int parameterIndex, int sqlType) {
        try {
            pstmt.setNull(parameterIndex, sqlType);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getMaxFieldSize() {
        try {
            return pstmt.getMaxFieldSize();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setBoolean(int parameterIndex, boolean x) {
        try {
            pstmt.setBoolean(parameterIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setByte(int parameterIndex, byte x) {
        try {
            pstmt.setByte(parameterIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setMaxFieldSize(int max) {
        try {
            pstmt.setMaxFieldSize(max);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setShort(int parameterIndex, short x) {
        try {
            pstmt.setShort(parameterIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getMaxRows() {
        try {
            return pstmt.getMaxRows();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setInt(int parameterIndex, int x) {
        try {
            pstmt.setInt(parameterIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setMaxRows(int max) {
        try {
            pstmt.setMaxRows(max);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setLong(int parameterIndex, long x) {
        try {
            pstmt.setLong(parameterIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setFloat(int parameterIndex, float x) {
        try {
            pstmt.setFloat(parameterIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setEscapeProcessing(boolean enable) {
        try {
            pstmt.setEscapeProcessing(enable);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setDouble(int parameterIndex, double x) {
        try {
            pstmt.setDouble(parameterIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getQueryTimeout() {
        try {
            return pstmt.getQueryTimeout();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setBigDecimal(int parameterIndex, BigDecimal x) {
        try {
            pstmt.setBigDecimal(parameterIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setQueryTimeout(int seconds) {
        try {
            pstmt.setQueryTimeout(seconds);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setString(int parameterIndex, String x) {
        try {
            pstmt.setString(parameterIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setBytes(int parameterIndex, byte[] x) {
        try {
            pstmt.setBytes(parameterIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void cancel() {
        try {
            pstmt.cancel();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setDate(int parameterIndex, Date x) {
        try {
            pstmt.setDate(parameterIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public SQLWarning getWarnings() {
        try {
            return pstmt.getWarnings();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setTime(int parameterIndex, Time x) {
        try {
            pstmt.setTime(parameterIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setTimestamp(int parameterIndex, Timestamp x) {
        try {
            pstmt.setTimestamp(parameterIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void clearWarnings() {
        try {
            pstmt.clearWarnings();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setCursorName(String name) {
        try {
            pstmt.setCursorName(name);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setAsciiStream(int parameterIndex, InputStream x, int length) {
        try {
            pstmt.setAsciiStream(parameterIndex, x, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    @SuppressWarnings("deprecation")
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) {
        try {
            pstmt.setUnicodeStream(parameterIndex, x, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean execute(String sql) {
        try {
            return pstmt.execute(sql);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setBinaryStream(int parameterIndex, InputStream x, int length) {
        try {
            pstmt.setBinaryStream(parameterIndex, x, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public ResultSet getResultSet() {
        try {
            return pstmt.getResultSet();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getUpdateCount() {
        try {
            return pstmt.getUpdateCount();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void clearParameters() {
        try {
            pstmt.clearParameters();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean getMoreResults() {
        try {
            return pstmt.getMoreResults();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType) {
        try {
            pstmt.setObject(parameterIndex, x, targetSqlType);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setFetchDirection(int direction) {
        try {
            pstmt.setFetchDirection(direction);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setObject(int parameterIndex, Object x) {
        try {
            pstmt.setObject(parameterIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getFetchDirection() {
        try {
            return pstmt.getFetchDirection();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setFetchSize(int rows) {
        try {
            pstmt.setFetchSize(rows);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getFetchSize() {
        try {
            return pstmt.getFetchSize();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean execute() {
        try {
            return pstmt.execute();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getResultSetConcurrency() {
        try {
            return pstmt.getResultSetConcurrency();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getResultSetType() {
        try {
            return pstmt.getResultSetType();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void addBatch(String sql) {
        try {
            pstmt.addBatch(sql);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void addBatch() {
        try {
            pstmt.addBatch();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setCharacterStream(int parameterIndex, Reader reader, int length) {
        try {
            pstmt.setCharacterStream(parameterIndex, reader, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void clearBatch() {
        try {
            pstmt.clearBatch();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int[] executeBatch() {
        try {
            return pstmt.executeBatch();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setRef(int parameterIndex, Ref x) {
        try {
            pstmt.setRef(parameterIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setBlob(int parameterIndex, Blob x) {
        try {
            pstmt.setBlob(parameterIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setClob(int parameterIndex, Clob x) {
        try {
            pstmt.setClob(parameterIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setArray(int parameterIndex, Array x) {
        try {
            pstmt.setArray(parameterIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Connection getConnection() {
        try {
            return pstmt.getConnection();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public ResultSetMetaData getMetaData() {
        try {
            return pstmt.getMetaData();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setDate(int parameterIndex, Date x, Calendar cal) {
        try {
            pstmt.setDate(parameterIndex, x, cal);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean getMoreResults(int current) {
        try {
            return pstmt.getMoreResults(current);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setTime(int parameterIndex, Time x, Calendar cal) {
        try {
            pstmt.setTime(parameterIndex, x, cal);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public QuietResultSet getGeneratedKeys() {
        try {
            return new QuietResultSet(pstmt.getGeneratedKeys());
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) {
        try {
            pstmt.setTimestamp(parameterIndex, x, cal);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int executeUpdate(String sql, int autoGeneratedKeys) {
        try {
            return pstmt.executeUpdate(sql, autoGeneratedKeys);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setNull(int parameterIndex, int sqlType, String typeName) {
        try {
            pstmt.setNull(parameterIndex, sqlType, typeName);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int executeUpdate(String sql, int[] columnIndexes) {
        try {
            return pstmt.executeUpdate(sql, columnIndexes);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setURL(int parameterIndex, URL x) {
        try {
            pstmt.setURL(parameterIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public ParameterMetaData getParameterMetaData() {
        try {
            return pstmt.getParameterMetaData();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setRowId(int parameterIndex, RowId x) {
        try {
            pstmt.setRowId(parameterIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int executeUpdate(String sql, String[] columnNames) {
        try {
            return pstmt.executeUpdate(sql, columnNames);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setNString(int parameterIndex, String value) {
        try {
            pstmt.setNString(parameterIndex, value);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setNCharacterStream(int parameterIndex, Reader value, long length) {
        try {
            pstmt.setNCharacterStream(parameterIndex, value, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean execute(String sql, int autoGeneratedKeys) {
        try {
            return pstmt.execute(sql, autoGeneratedKeys);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setNClob(int parameterIndex, NClob value) {
        try {
            pstmt.setNClob(parameterIndex, value);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setClob(int parameterIndex, Reader reader, long length) {
        try {
            pstmt.setClob(parameterIndex, reader, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean execute(String sql, int[] columnIndexes) {
        try {
            return pstmt.execute(sql, columnIndexes);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setBlob(int parameterIndex, InputStream inputStream, long length) {
        try {
            pstmt.setBlob(parameterIndex, inputStream, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setNClob(int parameterIndex, Reader reader, long length) {
        try {
            pstmt.setNClob(parameterIndex, reader, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean execute(String sql, String[] columnNames) {
        try {
            return pstmt.execute(sql, columnNames);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setSQLXML(int parameterIndex, SQLXML xmlObject) {
        try {
            pstmt.setSQLXML(parameterIndex, xmlObject);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) {
        try {
            pstmt.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getResultSetHoldability() {
        try {
            return pstmt.getResultSetHoldability();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean isClosed() {
        try {
            return pstmt.isClosed();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setPoolable(boolean poolable) {
        try {
            pstmt.setPoolable(poolable);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setAsciiStream(int parameterIndex, InputStream x, long length) {
        try {
            pstmt.setAsciiStream(parameterIndex, x, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean isPoolable() {
        try {
            return pstmt.isPoolable();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void closeOnCompletion() {
        try {
            pstmt.closeOnCompletion();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setBinaryStream(int parameterIndex, InputStream x, long length) {
        try {
            pstmt.setBinaryStream(parameterIndex, x, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean isCloseOnCompletion() {
        try {
            return pstmt.isCloseOnCompletion();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setCharacterStream(int parameterIndex, Reader reader, long length) {
        try {
            pstmt.setCharacterStream(parameterIndex, reader, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public long getLargeUpdateCount() {
        try {
            return pstmt.getLargeUpdateCount();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setLargeMaxRows(long max) {
        try {
            pstmt.setLargeMaxRows(max);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setAsciiStream(int parameterIndex, InputStream x) {
        try {
            pstmt.setAsciiStream(parameterIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public long getLargeMaxRows() {
        try {
            return pstmt.getLargeMaxRows();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setBinaryStream(int parameterIndex, InputStream x) {
        try {
            pstmt.setBinaryStream(parameterIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public long[] executeLargeBatch() {
        try {
            return pstmt.executeLargeBatch();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setCharacterStream(int parameterIndex, Reader reader) {
        try {
            pstmt.setCharacterStream(parameterIndex, reader);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setNCharacterStream(int parameterIndex, Reader value) {
        try {
            pstmt.setNCharacterStream(parameterIndex, value);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public long executeLargeUpdate(String sql) {
        try {
            return pstmt.executeLargeUpdate(sql);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setClob(int parameterIndex, Reader reader) {
        try {
            pstmt.setClob(parameterIndex, reader);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public long executeLargeUpdate(String sql, int autoGeneratedKeys) {
        try {
            return pstmt.executeLargeUpdate(sql, autoGeneratedKeys);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setBlob(int parameterIndex, InputStream inputStream) {
        try {
            pstmt.setBlob(parameterIndex, inputStream);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setNClob(int parameterIndex, Reader reader) {
        try {
            pstmt.setNClob(parameterIndex, reader);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public long executeLargeUpdate(String sql, int[] columnIndexes) {
        try {
            return pstmt.executeLargeUpdate(sql, columnIndexes);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setObject(int parameterIndex, Object x, SQLType targetSqlType, int scaleOrLength) {
        try {
            pstmt.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public long executeLargeUpdate(String sql, String[] columnNames) {
        try {
            return pstmt.executeLargeUpdate(sql, columnNames);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setObject(int parameterIndex, Object x, SQLType targetSqlType) {
        try {
            pstmt.setObject(parameterIndex, x, targetSqlType);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public long executeLargeUpdate() {
        try {
            return pstmt.executeLargeUpdate();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

}
